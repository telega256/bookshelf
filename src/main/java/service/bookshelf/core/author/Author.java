package service.bookshelf.core.author;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import service.bookshelf.core.book.Book;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@Table(name = "bs_author")
public class Author {

    @Id
    @Column(name = "id")
    @GenericGenerator(
            name = "bs_author_id_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "bs_author_id_seq"),
                    @org.hibernate.annotations.Parameter(name= "INCREMENT", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "MINVALUE", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "MAXVALUE", value = "2147483647"),
                    @org.hibernate.annotations.Parameter(name = "CACHE", value = "1")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bs_author_id_seq")
    private long id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @NotAudited
    @Column(name = "image")
    private byte[] image;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,})
    @JoinTable(name = "bs_author_book",
            joinColumns = { @JoinColumn(name = "id_author") },
            inverseJoinColumns = { @JoinColumn(name = "id_book") })
    private Set<Book> books = new HashSet<>();

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) { this.surname = surname; }

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }

    public Set<Book> getBooks() { return books; }

    public void setBooks(Set<Book> books) { this.books = books; }
}
