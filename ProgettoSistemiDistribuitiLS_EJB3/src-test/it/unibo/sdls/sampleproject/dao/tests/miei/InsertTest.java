package it.unibo.sdls.sampleproject.dao.tests.miei;

import it.unibo.sdls.sampleproject.dao.Author;
import it.unibo.sdls.sampleproject.dao.AuthorDAO;
import it.unibo.sdls.sampleproject.dao.DAOFactory;
import it.unibo.sdls.sampleproject.dao.ejb3.EJB3DaoFactory;

public class InsertTest {

	public static void main(String[] args) {
		
		DAOFactory ejb3DAOFactory = DAOFactory.getDAOFactory(EJB3DaoFactory.class.getName());
		AuthorDAO authorDAO = ejb3DAOFactory.getAuthorDAO();
		Author a = new Author();
		a.setName("Angelo");
		authorDAO.insertAuthor(a);
		
	}

}
