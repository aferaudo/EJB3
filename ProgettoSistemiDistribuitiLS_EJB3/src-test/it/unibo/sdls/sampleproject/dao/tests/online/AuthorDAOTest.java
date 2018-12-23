package it.unibo.sdls.sampleproject.dao.tests.online;

import java.util.HashSet;
import java.util.List;

import it.unibo.sdls.sampleproject.dao.Author;
import it.unibo.sdls.sampleproject.dao.AuthorDAO;
import it.unibo.sdls.sampleproject.dao.Book;
import it.unibo.sdls.sampleproject.dao.BookDAO;
import it.unibo.sdls.sampleproject.dao.DAOFactory;
import it.unibo.sdls.sampleproject.dao.Publisher;
import it.unibo.sdls.sampleproject.dao.PublisherDAO;
import it.unibo.sdls.sampleproject.dao.ejb3.EJB3DaoFactory;

import org.junit.Assert;
import org.junit.Test;

public class AuthorDAOTest {
	
	static DAOFactory ejb3DAOFactory = DAOFactory.getDAOFactory(EJB3DaoFactory.class.getName());
	static AuthorDAO authorDAO = ejb3DAOFactory.getAuthorDAO();
	static BookDAO bookDAO = ejb3DAOFactory.getBookDAO();
	static PublisherDAO publisherDAO = ejb3DAOFactory.getPublisherDAO();
	
	@Test
	public void addAuthor(){
		final String authorName = "Stefano";
		Author author = new Author(authorName);
		int authorId = authorDAO.insertAuthor(author);
		
		//Test relativi alle routine di reperimento Entity
		Assert.assertTrue(authorId != 0);
		Assert.assertEquals(authorDAO.findAuthorByName(authorName).getId(), authorId);
		Assert.assertEquals(authorDAO.findAllAuthors().size(), 1 );
		
		//Test: fallimento inserimento autori omonimi
		int newAuthorId = 0;
		try{
			newAuthorId = authorDAO.insertAuthor(author);
			
			//la precedente istruzione avrebbe dovuto lanciare una eccezione 
			//(IntegrityViolation: la colonna "name" dell'autore autore ha vincolo "unique") 
			Assert.fail();
		}
		catch (Exception e) {

		}
		Assert.assertEquals(newAuthorId, 0);
		Assert.assertEquals(authorDAO.findAllAuthors().size(), 1 );
		
		//Test: rimozione autore per id
		newAuthorId = authorDAO.removeAuthorById(authorId);
		Assert.assertEquals(newAuthorId, authorId );
		Assert.assertEquals(authorDAO.findAllAuthors().size(), 0 );
		
		
		authorId = authorDAO.insertAuthor(author);
		//Test: rimozione autore per nome
		newAuthorId = authorDAO.removeAuthorByName(authorName);
		Assert.assertEquals(newAuthorId, authorId );
		Assert.assertEquals(authorDAO.findAllAuthors().size(), 0 );
	}
	
	@Test
	public void addAuthor_andRelatedBooks(){
		final String authorName = "Stefano";
		Author author = new Author(authorName);
		
		Book firstBook = new Book();
		firstBook.setTitle("EJB3s rule!");
		firstBook.setIsbn10("0123456789");
		firstBook.setIsbn13("0123456789123");
		HashSet<Author> firstBookAuthors = new HashSet<Author>();
		firstBookAuthors.add(author);
		firstBook.setAuthors(firstBookAuthors);
		firstBook.setPublisher(new Publisher("firstPublisher"));
		
		Book secondBook = new Book();
		secondBook.setTitle("EJB3s rule (most of the times...) - Second edition");
		secondBook.setIsbn10("1123456789");
		secondBook.setIsbn13("1123456789123");
		HashSet<Author> secondBookAuthors = new HashSet<Author>();
		secondBookAuthors.add(author);
		secondBook.setAuthors(secondBookAuthors);
		secondBook.setPublisher(new Publisher("secondPublisher"));
		
		HashSet<Book> books = new HashSet<Book>();
		books.add(firstBook);
		books.add(secondBook);
		author.setBooks(books);
		
		int authorId = authorDAO.insertAuthor(author);
		author = authorDAO.findAuthorById(authorId);//reperisco l'oggetto salvato su DB 
		
		//Test relativi alle routine di reperimento Entity
		Assert.assertTrue(authorId != 0);
		Assert.assertEquals(authorDAO.findAuthorByName(authorName).getId(), authorId);
		Assert.assertEquals(authorDAO.findAllAuthors().size(), 1 );
		

		
		Assert.assertNotNull( bookDAO );
		Assert.assertNotNull( bookDAO.getAllBooks() );
		//Test relativi alle routine di reperimento dei libri
		Assert.assertEquals( bookDAO.getAllBooks().size(), 2);
		
		try {
			//Questa istruzione dovrebbe fallire perché il metodo getAllBooks non esegue una fetch join, quindi i campi relativi ad autori e 
			//publisher non sono valorizzati
			bookDAO.getAllBooks().get(0).getAuthors().toArray(new Author[]{})[0].getName();
			Assert.fail();
		}
		catch (Exception e) {
			
		}
		//Queste istruzioni non falliscono, perché i metodi bookDAO.getAllBooksByAuthor() e bookDAO.getAllBooks() effettuano una FETCH JOIN
		//ricaricando quindi in memoria anche le strutture dati relative agli oggetti Author e Publisher
		Assert.assertEquals( bookDAO.getAllBooksByAuthor(author).get(0).getAuthors().toArray(new Author[]{})[0].getName(), authorName);
		Assert.assertEquals( bookDAO.getAllBooksByAuthor(author).get(1).getAuthors().toArray(new Author[]{})[0].getName(), authorName);
		
		
		//Cancellazione dell'autore
		int newAuthorId = authorDAO.removeAuthorByName(authorName);
		Assert.assertEquals(newAuthorId, authorId );
		Assert.assertEquals(authorDAO.findAllAuthors().size(), 0 );
		
		List<Book> remainingBooks = bookDAO.getAllBooks();
		
		//La cancellazione dell'autore non cancella i libri
		Assert.assertEquals( remainingBooks.size(), 2);
		
		
		//Cancellazione esplicita dei libri
		bookDAO.deleteBook(remainingBooks.get(0).getId());
		bookDAO.deleteBook(remainingBooks.get(1).getId());
		Assert.assertEquals( bookDAO.getAllBooks().size(), 0);
		
		publisherDAO.removePublisherByName("firstPublisher");
		publisherDAO.removePublisherByName("secondPublisher");
	}

}
