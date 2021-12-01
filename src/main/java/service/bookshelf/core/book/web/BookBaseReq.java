package service.bookshelf.core.book.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import service.bookshelf.base.BaseReq;
import service.bookshelf.core.author.web.AuthorBaseReq;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;

@Schema(description = "Request книги")
public class BookBaseReq extends BaseReq {

    @Schema(description = "Название", example = "Книга")
    @NotBlank
    @Size(min = 1, max = 255)
    private String title;

    @Schema(description = "Год", example = "2021")
    @NotNull
    @Max(4000)
    @Min(-4000)
    private int year;

    @Schema(description = "Список авторов")
    @Valid
    @NotNull
    @JsonProperty("authors")
    private Set<AuthorBaseReq> authorBaseReq;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) { this.title = title; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public Set<AuthorBaseReq> getAuthorBaseReq() { return authorBaseReq; }

    public void setAuthorBaseReq(Set<AuthorBaseReq> authorBaseReq) { this.authorBaseReq = authorBaseReq; }

}
