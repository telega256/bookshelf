package service.bookshelf.core.author;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.bookshelf.core.author.converter.AuthorToAuthorViewConverter;
import service.bookshelf.core.author.web.AuthorBaseReq;
import service.bookshelf.core.author.web.AuthorView;
import service.bookshelf.core.book.Book;
import service.bookshelf.core.book.BookRepo;
import service.bookshelf.error.EntityAlreadyExistsException;
import service.bookshelf.error.ApplicationException;
import service.bookshelf.error.EntityNotFoundException;
import service.bookshelf.error.UploadImageException;
import service.bookshelf.util.MessageUtil;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;
    private final MessageUtil messageUtil;
    private final AuthorToAuthorViewConverter authorToAuthorViewConverter;

    public Author findAuthorByNameAndSurnameOrThrow(String name, String surname) {
        return authorRepo.findByNameAndSurname(name, surname)
                .orElseThrow(() -> new EntityNotFoundException(messageUtil.getMessage("author.NotFound", name, surname)));
    }

    public Author findAuthorByIdOrThrow(Long id) {
        return authorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtil.getMessage("author.Id.NotFound", id)));
    }

    public AuthorView getAuthor(Long id) {
        Author author = findAuthorByIdOrThrow(id);
        return authorToAuthorViewConverter.convert(author);
    }

    public Page<AuthorView> findAllAuthors(Pageable pageable) {
        Page<Author> authors = authorRepo.findAll(pageable);
        List<AuthorView> authorViews = new ArrayList<>();
        authors.forEach(author -> {
            AuthorView authorView = authorToAuthorViewConverter.convert(author);
            authorViews.add(authorView);
        });
        return new PageImpl<>(authorViews, pageable, authors.getTotalElements());
    }

    public AuthorView create(AuthorBaseReq authorBaseReq) {
        Author authorSave;
        try{
            // Проверяем есть ли в реестре автор с таким же именем и фамилией
            this.findAuthorByNameAndSurnameOrThrow(authorBaseReq.getName(),authorBaseReq.getSurname());
            // Если есть, то кидаем исключение
            throw new EntityAlreadyExistsException(
                    messageUtil.getMessage("author.AlreadyExists",
                            authorBaseReq.getName(),
                            authorBaseReq.getSurname()));
        }
        catch(EntityNotFoundException ex){
            // Если такого же автора в реестре нет, то создаем его
            Author author = new Author();
            this.prepare(author, authorBaseReq);
            authorSave = authorRepo.save(author);
        }

        return authorToAuthorViewConverter.convert(authorSave);
    }

    @Transactional
    public void delete(Long id) {
        Author author = this.findAuthorByIdOrThrow(id);
        // Провряем ссылки на автора
        Set<Book> books = author.getBooks();
        if(!books .isEmpty()) {
            books.forEach(book -> {
                // Если у книги несколько авторов, то удаляем ссылку
                if(book.getAuthors().size() > 1) {
                    book.getAuthors().remove(author);
                }
                else // Если у книги только один автор, то удаляем книгу
                   bookRepo.delete(book);
            });
        }
        authorRepo.delete(author);
    }

    public AuthorView update(Long id, AuthorBaseReq authorBaseReq) {
        Author author = this.findAuthorByIdOrThrow(id);
        try {
            // Проверяем есть ли автор с таким же именем и фамилией, но другим id
            Author authorOld = this.findAuthorByNameAndSurnameOrThrow(
                    authorBaseReq.getName(), authorBaseReq.getSurname());
            if (authorOld.getId() != author.getId()) {
                // Если есть автор с таким же именем и фамилией, но другим id, то кидаем исключение
                throw new EntityAlreadyExistsException(
                        messageUtil.getMessage("author.AlreadyExists",
                                authorBaseReq.getName(),
                                authorBaseReq.getSurname()));
            }
        }
        catch(EntityNotFoundException ex){/* Пустой блок catch */}
        // Обновляем автора
        Author newAuthor = this.prepare(author, authorBaseReq);
        Author authorForSave = authorRepo.save(newAuthor);
        return authorToAuthorViewConverter.convert(authorForSave);
    }

    public Author prepare(Author author, AuthorBaseReq authorBaseReq) {
        author.setName(authorBaseReq.getName());
        author.setSurname(authorBaseReq.getSurname());
        return author;
    }

    public void uploadImage(Long id, MultipartFile image){
        Author author = this.findAuthorByIdOrThrow(id);
        try {
            if(image.getSize() == 0) {
                throw new Exception();
            }
            author.setImage(image.getBytes());
        }
        catch (Exception ex){
            throw new UploadImageException(messageUtil.getMessage(
                    "author.Error.Upload"));
        }
        authorRepo.save(author);
    }

    public byte[] getImage(Long id) {
        byte[] image = this.findAuthorByIdOrThrow(id).getImage();
        if(image == null){
            throw new ApplicationException(
                    messageUtil.getMessage("author.Image.NotFound", id));
        }
        return image;
    }

}
