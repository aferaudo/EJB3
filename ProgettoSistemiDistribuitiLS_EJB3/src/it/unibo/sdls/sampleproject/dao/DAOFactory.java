package it.unibo.sdls.sampleproject.dao;

public abstract class DAOFactory {
	
	// ---------------------------------------------------------------------------
	
	public static DAOFactory getDAOFactory(String whichFactory) {
		try {
			return (DAOFactory) Class.forName(whichFactory).newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// ---------------------------------------------------------------------------
	
	public abstract AuthorDAO getAuthorDAO();
	
	public abstract BookDAO getBookDAO();
	
	public abstract PublisherDAO getPublisherDAO();
	
}

