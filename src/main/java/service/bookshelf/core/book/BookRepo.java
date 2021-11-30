package service.bookshelf.core.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import service.bookshelf.core.author.Author;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> , RevisionRepository<Book, Long, Integer> {

    Page<Book> findBooksByTitle(String title, Pageable pageable);
    Page<Book> findBooksByAuthors(Author authors, Pageable pageable);
    Optional<Book> findByTitleAndYear(String title, int year);
}
