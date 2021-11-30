package service.bookshelf.core.author.web;

import io.swagger.v3.oas.annotations.media.Schema;
import service.bookshelf.core.book.web.BookView;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "Response автора")
public class AuthorView {

    @Schema(description = "Идентификатор")
    private long id;

    @Schema(description = "Имя автора")
    private String name;

    @Schema(description = "Фамилия автора")
    private String surname;

    @Schema(description = "Список книг автора")
    private Set<BookView> books = new HashSet<>();

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Set<BookView> getBooks() {
        return this.books;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBooks(Set<BookView> books) {
        this.books = books;
    }
}
