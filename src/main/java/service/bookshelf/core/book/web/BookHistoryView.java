package service.bookshelf.core.book.web;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.history.RevisionMetadata;

import java.time.Instant;
import java.util.Optional;

@Schema(description = "Response изменения книги")
public class BookHistoryView {

    @Schema(description = "Время изменения")
    private Optional<Instant> timestamp;
    @Schema(description = "Тип изменения")
    private RevisionMetadata.RevisionType revisionType;
    @Schema(description = "Изменение книги")
    private BookView bookView;

    public BookHistoryView(Optional<Instant> timestamp, RevisionMetadata.RevisionType revisionType, BookView bookView)
    {
        this.timestamp = timestamp;
        this.revisionType = revisionType;
        this.bookView = bookView;
    }
    public BookView getBookView() { return bookView; }

    public void setBookView(BookView bookView) { this.bookView = bookView; }

    public Optional<Instant> getTimestamp() { return timestamp; }

    public void setTimestamp(Optional<Instant> timestamp) { this.timestamp = timestamp; }

    public RevisionMetadata.RevisionType getRevisionType() { return revisionType; }

    public void setRevisionType(RevisionMetadata.RevisionType revisionType) { this.revisionType = revisionType; }

}
