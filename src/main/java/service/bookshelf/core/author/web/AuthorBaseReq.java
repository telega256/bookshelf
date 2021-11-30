package service.bookshelf.core.author.web;

import io.swagger.v3.oas.annotations.media.Schema;
import service.bookshelf.base.BaseReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "Request автора")
public class AuthorBaseReq extends BaseReq {

    @Schema(description = "Имя", example = "Иван")
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @Schema(description = "Фамилия", example = "Иванов")
    @NotBlank
    @Size(min = 1, max = 255)
    private String surname;

    public AuthorBaseReq(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
