package it.unibo.sdls.sampleproject.dao.ejb3;



import javax.naming.InitialContext;



import it.unibo.sdls.sampleproject.dao.AuthorDAO;
import it.unibo.sdls.sampleproject.dao.BookDAO;
import it.unibo.sdls.sampleproject.dao.DAOFactory;
import it.unibo.sdls.sampleproject.dao.PublisherDAO;

public class EJB3DaoFactory extends DAOFactory{

	
	@Override
	public AuthorDAO getAuthorDAO() {
		try {
			InitialContext context = new InitialContext();
			AuthorDAO result = (AuthorDAO)context.lookup("java:global/Progetto_EJB3/Progetto_EJB3.jar/EJB3AuthorSB");
			return result;
		} catch (Exception e) {
			//logger.error("Error looking up PublisherDAO",e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BookDAO getBookDAO() {
		try {
			InitialContext context = new InitialContext();
			BookDAO result = (BookDAO)context.lookup("java:global/Progetto_EJB3/Progetto_EJB3.jar/EJB3BookSB");
			return result;
		} catch (Exception e) {
			//logger.error("Error looking up PublisherDAO",e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PublisherDAO getPublisherDAO() {
		try {
			InitialContext context = new InitialContext();
			PublisherDAO result = (PublisherDAO)context.lookup("java:global/Progetto_EJB3/Progetto_EJB3.jar/EJB3PublisherSB");
			return result;
		} catch (Exception e) {
			//logger.error("Error looking up PublisherDAO",e);
			e.printStackTrace();
		}
		return null;
	}

}
