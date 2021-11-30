package service.bookshelf.core.author.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import service.bookshelf.core.author.Author;
import service.bookshelf.core.author.web.AuthorView;
import service.bookshelf.core.book.Book;
import service.bookshelf.core.book.converter.BookToBookViewConverter;
import service.bookshelf.core.book.web.BookView;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthorToAuthorViewConverter implements Converter<Author, AuthorView> {

    private final BookToBookViewConverter bookToBookViewConverter;
    @Override
    public AuthorView convert(@NonNull Author author) {
        AuthorView view = new AuthorView();
        view.setId(author.getId());
        view.setName(author.getName());
        view.setSurname(author.getSurname());
        Set<BookView> views = new HashSet<>();
        Set<Book> books = author.getBooks();
        books.forEach(book -> {
            BookView bookView = bookToBookViewConverter.convert(book);
            views.add(bookView);
        });
        view.setBooks(views);
        return view;
    }
}
