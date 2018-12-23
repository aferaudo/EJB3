package it.unibo.sdls.sampleproject.dao.tests.online;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.unibo.sdls.sampleproject.dao.Publisher;
import it.unibo.sdls.sampleproject.dao.AuthorDAO;
import it.unibo.sdls.sampleproject.dao.Book;
import it.unibo.sdls.sampleproject.dao.BookDAO;
import it.unibo.sdls.sampleproject.dao.DAOFactory;
import it.unibo.sdls.sampleproject.dao.PublisherDAO;
import it.unibo.sdls.sampleproject.dao.ejb3.EJB3DaoFactory;

public class PublisherDAOTest {
	static DAOFactory ejb3DAOFactory = DAOFactory.getDAOFactory(EJB3DaoFactory.class.getName());
	static AuthorDAO authorDAO = ejb3DAOFactory.getAuthorDAO();
	static BookDAO bookDAO = ejb3DAOFactory.getBookDAO();
	static PublisherDAO publisherDAO = ejb3DAOFactory.getPublisherDAO();
	 
	
	@Test
	public void addPublishers_with_books(){
		final String publisherName1 = "myTestPublisher1";
		final String publisherName2 = "myTestPublisher2";
		Publisher publisher1 = new Publisher(publisherName1);
		Publisher publisher2 = new Publisher(publisherName2);
		
		int publisher2Id = publisherDAO.insertPublisher(publisher2);
		
		Book book = new Book();
		book.setTitle("EJB3s rule!");
		book.setIsbn10("2123456789");
		book.setIsbn13("2123456789123");
		book.setPublisher(publisher1);
		
		int bookId = bookDAO.addBook(book);
		book = bookDAO.getBookById(bookId);
		Assert.assertEquals(book.getId(),bookId);
		
		List<Book> books = bookDAO.getAllBooksByPublisher(book.getPublisher());
		Assert.assertTrue(book.getPublisher().getId() > 0 );
		Assert.assertNotNull(books);
		Assert.assertEquals(books.size(), 1);
		
		Assert.assertEquals(publisherDAO.findAllPublishers().size(),2);
		Assert.assertNotNull(publisherDAO.findPublisherById(book.getPublisher().getId()));
		Assert.assertEquals(publisherDAO.findPublisherById(book.getPublisher().getId()).getName(),publisherName1);
		Assert.assertEquals(publisherDAO.findPublisherByName(publisherName1).getName(),publisherName1);
		Assert.assertNotNull(publisherDAO.findPublisherById(publisher2Id));
		Assert.assertEquals(publisherDAO.findPublisherByName(publisherName2).getName(),publisherName2);
		
		//rimozione
		bookDAO.deleteBook(book.getId());
		Assert.assertEquals(bookDAO.getAllBooksByPublisher(book.getPublisher()).size(), 0);
		
		publisherDAO.removePublisherById(book.getPublisher().getId());
		publisherDAO.removePublisherById(publisher2Id);
		Assert.assertEquals(publisherDAO.findAllPublishers().size(), 0);
		
		
	}
}
