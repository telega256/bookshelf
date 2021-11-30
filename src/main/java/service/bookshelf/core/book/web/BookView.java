package service.bookshelf.core.book.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Response книги")
public class BookView {

    @Schema(description = "Идентификатор")
    private long id;

    @Schema(description = "Название")
    private String title;

    @Schema(description = "Год")
    private int year;

    @Schema(description = "Список авторов(Имя и фамилия)")
    private Set<String> authors;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() { return this.title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public Set<String> getAuthors() { return authors; }

    public void setAuthors(Set<String> authors) { this.authors = authors; }
}

