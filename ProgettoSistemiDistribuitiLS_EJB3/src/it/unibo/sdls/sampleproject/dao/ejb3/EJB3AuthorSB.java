package it.unibo.sdls.sampleproject.dao.ejb3;


import java.util.List;


import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import it.unibo.sdls.sampleproject.dao.Author;
import it.unibo.sdls.sampleproject.dao.AuthorDAO;
import it.unibo.sdls.sampleproject.logging.Interceptor;


@Remote(AuthorDAO.class)
@Stateless
public class EJB3AuthorSB implements AuthorDAO {

	@PersistenceContext(unitName = "MySQLDSUnit")
	EntityManager em;

	@Interceptors(Interceptor.class)
	public int insertAuthor(Author author) {

		if (author == null) {
			//LOGGER.log(Level.WARNING, "Authors: insert Author error (NullPointerException)");
			return -1;
		}

		em.persist(author);
		// devo recuperare l'id associato all'autore che ho appena inserito nel db

		//LOGGER.log(Level.INFO, "Author: insert of " + author.getId() + " is completed!");

		return author.getId();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int removeAuthorByName(String name) {
		if (name == null) {
			//LOGGER.log(Level.WARNING, "Authors: remove by name Author error (NullPointerException)");
			return -1;
		}

		/* Recupero l'autore da eliminare */
		Author a = (Author) em.createQuery("FROM Author a WHERE a.name=:name").setParameter("name", name)
				.getSingleResult();

		em.remove(a);

		//LOGGER.log(Level.INFO, "Authors: remove Author success " + name);

		return 0;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int removeAuthorById(int id) {
		if (id < 0) {
			//LOGGER.log(Level.WARNING, "Authors: remove by id Author error (id < 0)");
			return -1;
		}

		/* Recupero l'autore da eliminare */
		Author a = em.find(Author.class, id);
		em.remove(a);

		//LOGGER.log(Level.INFO, "Authors: remove Author success" + id);
		return 0;
	}

	public Author findAuthorByName(String name) {
		if (name == null) {
			//LOGGER.log(Level.WARNING, "Authors: findAuthorByName Author error (NullPointerException)");
			return null;
		}
		/* Recupero l'autore */
		Author a = (Author) em.createQuery("FROM Author a WHERE a.name=:name").setParameter("name", name)
				.getSingleResult();

		//LOGGER.log(Level.INFO, "Authors: find author by name success " + a.getId() + " " + a.getName());

		return a;
	}

	public Author findAuthorById(int id) {
		if (id < 0) {
			//LOGGER.log(Level.WARNING, "Authors: findAuthorByName Author error (id < 0)");
			return null;
		}

		/* Recupero l'autore */
		Author a = em.find(Author.class, id);
		//LOGGER.log(Level.INFO, "Authors: remove Author success " + a.getId() + " " + a.getName());

		return a;
	}

	public List<Author> findAllAuthors() {

		/*
		 * E' possibile usare la suppress warnings perché siamo sicuri che ciò che mi
		 * ritornerà sarà una lista di autori oppure un unico valore
		 */
		@SuppressWarnings("unchecked")
		List<Author> result = (List<Author>) em.createQuery("FROM Author").getResultList();

		return result;
	}

}
