package it.unibo.sdls.sampleproject.dao;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="books")
public class Book implements Serializable{
	
	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 6476221132667629595L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) //permette di generare automaticamente gli id in sequenza
	@Column(name = "id", updatable = false, nullable = false)
	protected int id;
	protected String title;
	protected String isbn10;
	protected String isbn13;
	
	/*Essendo una relazione many_to_many provoca la creazione di 
	 * un ulteriore tabella quindi in questo caso bisogna dirgli che mi devi
	 * restituire quel set di autori che hanno scritto me libro per farlo*/
	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name="authors_books", 
		joinColumns={@JoinColumn(name="books_id")},
		inverseJoinColumns={@JoinColumn(name="authors_id")})
	protected Set<Author> authors;
	
	/*Questo mi dice che la relazione è bidirezionale e 
	 * che la foreign key nella tabella books di publisher
	 * avrà come nome publisher_id*/
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="publisher_id")
	protected Publisher publisher;
	
	// ------------------------------------------
	// Constructors
	// ------------------------------------------
	
	// default
	public Book() {}
	
	// ------------------------------------------
	// Getter and Setter methods
	// ------------------------------------------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}	
	
}
