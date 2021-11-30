package service.bookshelf.core.book;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import service.bookshelf.core.author.Author;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bs_book")
@Audited
public class Book {

    @Id
    @Column(name = "id")
    @GenericGenerator(
            name = "bs_book_id_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "bs_book_id_seq"),
                    @org.hibernate.annotations.Parameter(name= "INCREMENT", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "MINVALUE", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "MAXVALUE", value = "2147483647"),
                    @org.hibernate.annotations.Parameter(name = "CACHE", value = "1")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bs_book_id_seq")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private int year;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
    private Set<Author> authors = new HashSet<>();

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public Set<Author> getAuthors() { return authors; }

    public void setAuthors(Set<Author> authors) { this.authors = authors; }

}
