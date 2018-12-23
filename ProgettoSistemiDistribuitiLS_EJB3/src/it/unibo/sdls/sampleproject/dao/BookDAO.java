package it.unibo.sdls.sampleproject.dao;

import java.util.List;

public interface BookDAO {
	
	public int addBook(Book book);
	
	public int deleteBook(int id);
	
	public Book getBookById(int id);
	
	public Book getBookByISBN10(String isbn10);
	
	public Book getBookByISBN13(String isbn13);
	
	public Book getBookByTitle(String title);
	
	public List<Book> getAllBooks();
	
	public List<Book> getAllBooksByAuthor(Author author);
	
	public List<Book> getAllBooksByPublisher(Publisher publisher);

}
