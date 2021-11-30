package service.bookshelf.core.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndSurname(String name, String surname);
}
