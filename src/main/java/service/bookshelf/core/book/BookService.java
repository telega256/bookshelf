package service.bookshelf.core.book;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.bookshelf.core.author.Author;
import service.bookshelf.core.author.AuthorService;
import service.bookshelf.core.author.web.AuthorBaseReq;
import service.bookshelf.core.author.web.AuthorView;
import service.bookshelf.core.book.converter.BookToBookViewConverter;
import service.bookshelf.core.book.web.BookBaseReq;
import service.bookshelf.core.book.web.BookHistoryView;
import service.bookshelf.core.book.web.BookView;
import service.bookshelf.error.EntityAlreadyExistsException;
import service.bookshelf.error.EntityNotFoundException;
import service.bookshelf.util.MessageUtil;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    private final AuthorService authorService;
    private final MessageUtil messageUtil;
    private final BookToBookViewConverter bookToBookViewConverter;

    public Book findBookByIdOrThrow(Long id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtil.getMessage("book.Id.NotFound", id)));
    }

    private Page<Book> findBooksByTitleOrThrow(String title, Pageable pageable ) {
        Page<Book> books = bookRepo.findBooksByTitle(title, pageable);
        if(books.getTotalElements() != 0) {
            return books;
        }
        else {
            throw new EntityNotFoundException(messageUtil.getMessage("book.Title.NotFound", title));
        }
    }

    private Book findBookByTitleAndYearOrThrow(String title, int year) {
        return bookRepo.findByTitleAndYear(title, year)
                .orElseThrow(() -> new EntityNotFoundException(messageUtil.getMessage("book.Title.Year.NotFound", title, year)));
    }

    private Page<Book> findBooksByAuthorOrThrow(Author author, Pageable pageable) {
        Page<Book> books = bookRepo.findBooksByAuthors(author, pageable);
        if(books.getTotalElements() != 0) {
            return books;
        }
        else {
            throw new EntityNotFoundException(messageUtil.getMessage("books.NotFound"));
        }
    }
    public List<BookHistoryView> getBookHistory(Long id) {
        List<BookHistoryView> bookHistoryViews = new ArrayList<>();
        bookRepo.findRevisions(id).get().forEach(revision -> {
            BookHistoryView bookHistoryView = new BookHistoryView(
                    revision.getRevisionInstant(),
                    revision.getMetadata().getRevisionType(),
                    bookToBookViewConverter.convert(revision.getEntity())
            );
            bookHistoryViews.add(bookHistoryView);
        });
        if(bookHistoryViews.isEmpty()){
            throw new EntityNotFoundException(messageUtil.getMessage("book.History.NotFound", id));
        }
        return bookHistoryViews;
    }
    public BookView getBookById(Long id) {
        Book book = findBookByIdOrThrow(id);
        return bookToBookViewConverter.convert(book);
    }

    public Page<BookView> getBooksByTitle(String title, Pageable pageable) {
        Page<Book> books = findBooksByTitleOrThrow(title, pageable);
        List<BookView> bookViews = new ArrayList<>();
        books.forEach(book -> {
            BookView bookView = bookToBookViewConverter.convert(book);
            bookViews.add(bookView);
        });
        return new PageImpl<>(bookViews, pageable, bookViews.size());
    }

    public Page<BookView> getBooksByAuthor(Pageable pageable, @NotNull AuthorBaseReq authorBaseReq) {

        Author author = authorService.findAuthorByNameAndSurnameOrThrow(
                authorBaseReq.getName(),
                authorBaseReq.getSurname());

        Page<Book> books = this.findBooksByAuthorOrThrow(author, pageable);
        List<BookView> bookViews = new ArrayList<>();
        books.forEach(book -> {
            BookView bookView = bookToBookViewConverter.convert(book);
            bookViews.add(bookView);
        });
        return new PageImpl<>(bookViews, pageable, bookViews.size());
    }

    public Page<BookView> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepo.findAll(pageable);
        List<BookView> bookViews = new ArrayList<>();
        books.forEach(book -> {
            BookView bookView = bookToBookViewConverter.convert(book);
            bookViews.add(bookView);
        });
        return new PageImpl<>(bookViews, pageable, books.getTotalElements());
    }

    public BookView create(BookBaseReq bookBaseReq) {
        Book bookSave;
        try{
            // Сначала проверяем существует ли книга с таким же названием и годом
            this.findBookByTitleAndYearOrThrow(bookBaseReq.getTitle(), bookBaseReq.getYear());
            // Если существует, то кидаем исключение
            throw new EntityAlreadyExistsException(messageUtil.getMessage(
                    "book.AlreadyExists",
                    bookBaseReq.getTitle(),
                    bookBaseReq.getYear()));
        }
        catch(EntityNotFoundException ex){
            // Если не существует такой же книги, то создаем
            Book book = new Book();
            this.prepare(book, bookBaseReq);
            bookSave = bookRepo.save(book);
        }
        return bookToBookViewConverter.convert(bookSave);
    }

    @Transactional
    public void delete(Long id) {
        Book book = this.findBookByIdOrThrow(id);
        Set<Author> authors = book.getAuthors();
        if(!authors .isEmpty()) {
            authors.forEach(author -> author.getBooks().remove(book));
        }
        bookRepo.delete(book);
    }

    public BookView update(Long id, BookBaseReq bookBaseReq) {
        Book book = this.findBookByIdOrThrow(id);
        try {
            // Проверяем есть ли книга с таким же названием, годом, но другим id
            Book bookOld = this.findBookByTitleAndYearOrThrow(bookBaseReq.getTitle(), bookBaseReq.getYear());
            if (bookOld.getId() != book.getId()) {
                // Если есть книга с таким же названием и годом, но другим id, то кидаем исключение
                throw new EntityAlreadyExistsException(
                        messageUtil.getMessage("book.AlreadyExists",
                                bookBaseReq.getTitle(),
                                bookBaseReq.getYear()));
            }
        }
        catch(EntityNotFoundException ex){/* Пустой блок catch */}
        // Обновляем содержание книги
        Book newBook = this.prepare(book, bookBaseReq);
        Book bookSave = bookRepo.save(newBook);
        return bookToBookViewConverter.convert(bookSave);
    }

    private Book prepare(Book book, BookBaseReq bookBaseReq) {
        book.setTitle(bookBaseReq.getTitle());
        book.setYear(bookBaseReq.getYear());

        // Удаляем все ссылки на книгу у авторов,
        // если такая книга уже есть в каталоге
        if(book.getId() > 0) {
            Set<Author> authors = book.getAuthors();
            if(!authors .isEmpty()) {
                authors.forEach(author -> author.getBooks().remove(book));
            }
        }

        // Добавляем авторов для книги
        Set<Author> authors = new HashSet<>();
        bookBaseReq.getAuthorBaseReq().forEach(authorBaseReq -> {
            Author author;
            try{
                // Сначала проверяем существует ли автор в реестре
                author = authorService.findAuthorByNameAndSurnameOrThrow(
                        authorBaseReq.getName(),
                        authorBaseReq.getSurname());
            }
            catch(EntityNotFoundException ex){
                // Если не существует, то создаем его
                AuthorView authorView = authorService.create(authorBaseReq);
                author = authorService.findAuthorByIdOrThrow(authorView.getId());
            }
            authors.add(author);
            author.getBooks().add(book);
        });
        book.setAuthors(authors);
        return book;
    }
}
