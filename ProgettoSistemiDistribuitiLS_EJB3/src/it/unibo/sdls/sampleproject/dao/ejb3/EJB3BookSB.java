package it.unibo.sdls.sampleproject.dao.ejb3;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unibo.sdls.sampleproject.dao.Author;

import it.unibo.sdls.sampleproject.dao.Book;
import it.unibo.sdls.sampleproject.dao.BookDAO;
import it.unibo.sdls.sampleproject.dao.Publisher;
import it.unibo.sdls.sampleproject.logging.Interceptor;

@Remote(BookDAO.class)
@Stateless
public class EJB3BookSB implements BookDAO {


	@PersistenceContext(unitName = "MySQLDSUnit")
	EntityManager em;
	

	@Interceptors(Interceptor.class)
	public int addBook(Book book) {
		if (book == null)
			// insert logger
			return -1;

		
//		Publisher p = em.find(Publisher.class, book.getPublisher().getId());
//		
//		if (p == null)
//			// insert logger
//			return -1;
//		
		book.setPublisher(em.merge(book.getPublisher()));
		
		Set<Author> authors = new HashSet<Author>();
		for (Author author : book.getAuthors()) {
			//Author a = em.find(Author.class, author.getId());
			authors.add(em.merge(author));
		}
		book.setAuthors(authors);
		em.persist(book);
		
		return book.getId();

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int deleteBook(int id) {
		if (id < 0)
			// insert logger
			return -1;

		/*Recupero il libro da rimuovere*/
		Book toRemove = em.find(Book.class, id);
		em.remove(toRemove);
		
		return 0;
	}

	public Book getBookById(int id) {
		Book result = null;
		if (id < 0)
			// insert logger
			return result;

		result = em.find(Book.class, id);
		//LOGGER.log(Level.INFO, "Books: book " + result.getTitle() + " " + result.getId() + " is the result!" );
		return result;

	}

	public Book getBookByISBN10(String isbn10) {
		Book result = null;
		if (isbn10 == null)
			// insert logger
			return result;

		result = (Book) em.createQuery("FROM Book b WHERE b.isbn10=:isbn10")
				.setParameter("isbn10", isbn10)
				.getSingleResult();
		
		//LOGGER.log(Level.INFO, "Books: book " + result.getTitle() + " " + result.getId() + " is the result!" );
		return result;
		
	}

	public Book getBookByISBN13(String isbn13) {
		Book result = null;
		if (isbn13 == null)
			// insert logger
			return result;

		result = (Book) em.createQuery("FROM Book b WHERE b.isbn13=:isbn13")
				.setParameter("isbn13", isbn13)
				.getSingleResult();
		return result;
	}

	public Book getBookByTitle(String title) {
		Book result = null;
		if (title == null)
			// insert logger
			return result;
		result = (Book) em.createQuery("FROM Book b WHERE b.title=:title")
				.setParameter("title", title)
				.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Book> getAllBooks() {
		List<Book> result = new ArrayList<Book>();
		
		result = (List<Book>) em.createQuery("FROM Book b").getResultList();
		return result;
	}

	
	@SuppressWarnings("unchecked")
	public List<Book> getAllBooksByAuthor(Author author) {
		if (author != null){
			em.merge(author); // riattacco l'autore al contesto di persistenza con una merge
			return em.createQuery("SELECT DISTINCT (b) FROM Book b JOIN FETCH b.authors JOIN FETCH b.publisher WHERE :author MEMBER OF b.authors").
				setParameter("author", author).getResultList();
		}
		else 
			return em.createQuery("SELECT DISTINCT (b) FROM Book b JOIN FETCH b.authors JOIN FETCH b.publisher").getResultList();
	}

	public List<Book> getAllBooksByPublisher(Publisher publisher) {
		
		@SuppressWarnings("unchecked")
		/*Modificare perché l'oggetto restituito è un oggetto query!!!!*/
		List<Book> result = (List<Book>) em.createQuery("SELECT b FROM Book b WHERE b.publisher_id=:publisher")
		.setParameter("publisher", publisher.getId())
		.getResultList();
		
		return result;
	}

}
