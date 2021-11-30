package service.bookshelf.core.book.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import service.bookshelf.core.author.Author;
import service.bookshelf.core.book.Book;
import service.bookshelf.core.book.web.BookView;

import java.util.*;

@Component
public class BookToBookViewConverter implements Converter<Book, BookView> {

    @Override
    public BookView convert(@NonNull Book book) {
        BookView view = new BookView();
        view.setId(book.getId());
        view.setTitle(book.getTitle());
        view.setYear(book.getYear());
        Set<String> authorsNames = new HashSet<>();
        Set<Author> authors = book.getAuthors();
        authors.forEach(author -> authorsNames.add(author.getName() + " " + author.getSurname()));
        view.setAuthors(authorsNames);
        return view;
    }
}
