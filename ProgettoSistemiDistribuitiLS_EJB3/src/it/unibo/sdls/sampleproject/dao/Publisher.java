package it.unibo.sdls.sampleproject.dao;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="publishers")
public class Publisher implements Serializable{
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6423572771817481733L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) //permette di generare automaticamente gli id in sequenza
	@Column(name = "id", updatable = false, nullable = false)
	protected int id;
	protected String name;
	
	@OneToMany(mappedBy="publisher")
	protected Set<Book> books;
	
	// ------------------------------------------
	// Constructors
	// ------------------------------------------
	public Publisher() {}
	
	public Publisher(String name) {
		this.name = name;
	}
	
	// ------------------------------------------
	// Getter and Setter methods
	// ------------------------------------------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	
}
