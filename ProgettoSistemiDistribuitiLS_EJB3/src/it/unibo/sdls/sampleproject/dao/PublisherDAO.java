package it.unibo.sdls.sampleproject.dao;

import java.util.List;

public interface PublisherDAO {
	
	public int insertPublisher(Publisher publisher);
	
	public Publisher findPublisherByName(String name);
	
	public Publisher findPublisherById(int id);
	
	public List<Publisher> findAllPublishers();

	public int removePublisherByName(String name);
	
	public int removePublisherById(int id);
	

}
