package service.bookshelf.core.author.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.bookshelf.core.author.AuthorService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    @Operation( summary = "Получить список всех авторов")
    @GetMapping
    @ResponseBody
    public Page<AuthorView> getAllAuthors(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return authorService.findAllAuthors(pageable);
    }

    @Operation( summary = "Получить автора по идентификатору")
    @GetMapping("/id/{id}")
    @ResponseBody
    public AuthorView getAuthor(@Parameter(description = "Идентификатор") @PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @Operation( summary = "Создать автора")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AuthorView create(@RequestBody @Valid AuthorBaseReq authorBaseReq) {
        return authorService.create(authorBaseReq);
    }

    @Operation( summary = "Удалить автора")
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "Идентификатор") @PathVariable Long id){
        authorService.delete(id);
    }

    @Operation( summary = "Изменить автора")
    @PutMapping("/id/{id}")
    public AuthorView update(
            @Parameter(description = "Идентификатор") @PathVariable(name = "id") Long id,
            @RequestBody @Valid AuthorBaseReq authorBaseReq) {
        return authorService.update(id, authorBaseReq);
    }

    @Operation( summary = "Загрузить изображение для автора")
    @PostMapping("/image/{id}")
    public void uploadImage(
            @Parameter(description = "Идентификатор") @PathVariable(name = "id") Long id,
            @Parameter(description = "Изображение до 2 МБ") @RequestPart("image") MultipartFile image) {
        authorService.uploadImage(id, image);
    }

    @Operation( summary = "Получить изображение для автора")
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "Идентификатор") @PathVariable(name = "id") Long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<> (authorService.getImage(id), headers, HttpStatus.OK);
    }
}
