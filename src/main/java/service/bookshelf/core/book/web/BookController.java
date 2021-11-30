package service.bookshelf.core.book.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.bookshelf.core.author.web.AuthorBaseReq;
import service.bookshelf.core.book.BookService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Operation( summary = "Получить список всех книг")
    @GetMapping
    @ResponseBody
    public Page<BookView> getAllBooks(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return bookService.getAllBooks(pageable);
    }

    @Operation( summary = "Получить книгу по идентификатору")
    @GetMapping("/id/{id}")
    @ResponseBody
    public BookView getBookById(@Parameter(description = "Идентификатор") @PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation( summary = "Получить список книг по названию")
    @GetMapping("/title/{title}")
    @ResponseBody
    public Page<BookView> getBooksByTitle(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "Название книги") @PathVariable @NotBlank @Size(max = 255)  String title) {
        return bookService.getBooksByTitle(title, pageable);
    }

    @Operation( summary = "Получить список книг по имени и фамилии автора")
    @GetMapping("/author")
    @ResponseBody
    public Page<BookView> getBooksByAuthor(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @Valid @RequestBody AuthorBaseReq authorBaseReq) {
        return bookService.getBooksByAuthor(pageable, authorBaseReq);
    }

    @Operation( summary = "Создать новую книгу")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BookView create(@Valid @RequestBody BookBaseReq bookBaseReq) {
        return bookService.create(bookBaseReq);
    }

    @Operation( summary = "Удалить книгу")
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "Идентификатор") @PathVariable Long id){
        bookService.delete(id);
    }

    @Operation( summary = "Изменить книгу")
    @PutMapping("/id/{id}")
    public BookView update(@Parameter(description = "Идентификатор") @PathVariable(name = "id") Long id,
                           @RequestBody @Valid BookBaseReq bookBaseReq) {
        return bookService.update(id, bookBaseReq);
    }

    @Operation( summary = "Получить историю изменений книги по идентификатору")
    @GetMapping("/history/id/{id}")
    public List<BookHistoryView> getBookHistory(@Parameter(description = "Идентификатор") @PathVariable Long id){
        return bookService.getBookHistory(id);
    }
}
