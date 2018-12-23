package it.unibo.sdls.sampleproject.dao;

import java.util.List;

public interface AuthorDAO {
	
	public int insertAuthor(Author author);
	
	public int removeAuthorByName(String name);
	
	public int removeAuthorById(int id);
	
	public Author findAuthorByName(String name);
	
	public Author findAuthorById(int id);
	
	public List<Author> findAllAuthors();

}
