package it.unibo.sdls.sampleproject.dao.ejb3;


import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import it.unibo.sdls.sampleproject.dao.Publisher;
import it.unibo.sdls.sampleproject.dao.PublisherDAO;
import it.unibo.sdls.sampleproject.logging.Interceptor;



@Remote(PublisherDAO.class)
@Stateless 
public class EJB3PublisherSB implements PublisherDAO{

	@PersistenceContext(unitName="MySQLDSUnit")
	EntityManager em;
	
	@Interceptors(Interceptor.class)
	public int insertPublisher(Publisher publisher) {
		if(publisher == null) {
			//TxDAOFactory.//LOGGER.log( Level.WARNING, "Publishers: insert Publisher error (NullPointerException)");
			return -1;
		}

		/*Dopo la persist l'oggetto publisher diventerà managed e il parametro id verrà modificato automaticamente
		 * considerando il mapping*/
		em.persist(publisher);
		
		//LOGGER.log(Level.INFO, "Publishers: insert of " + publisher.getId() + " " + publisher.getName() + " is completed!");
		return publisher.getId();
	}

	public Publisher findPublisherByName(String name) {
		if(name == null) {
			//LOGGER.log( Level.WARNING, "Publishers: findPublisherByName error (NullPointerException)");
			return null;
		}
		
		/*Recupero il publisher*/
		Publisher p = (Publisher) em.createQuery("FROM Publisher p WHERE p.name=:name")
				.setParameter("name", name)
				.getSingleResult();
		//LOGGER.log( Level.INFO, "Publishers: find by name Publisher success " + p.getId() + " " + p.getName());
		
		return p;
	}

	public Publisher findPublisherById(int id) {
		if(id < 0) {
			//LOGGER.log( Level.WARNING, "Publishers: findPublisherByName error (id < 0)");
			return null;
		}
		
		/*Recupero il publisher*/
		Publisher p = em.find(Publisher.class, id);
		//LOGGER.log( Level.INFO, "Publishers: find by id Publisher success " + p.getId() + " " + p.getName());
		
		return p;
	}

	public int removePublisherByName(String name) {
			if(name == null) {
				//LOGGER.log( Level.WARNING, "Publishers: remove by name Publisher error (NullPointerException)");
				return -1;
			}
			
			/*Recupero il publisher da eliminare*/
			Publisher p = (Publisher) em.createQuery("FROM Publisher p WHERE p.name=:name")
					.setParameter("name", name)
					.getSingleResult();
			
			em.remove(p);
			
			//LOGGER.log( Level.INFO, "Publisher: remove Publisher success " + name);
			
			return 0;
	}

	public int removePublisherById(int id) {
		if(id < 0) {
			//LOGGER.log( Level.WARNING, "Publishers: remove by id Publisher error (id < 0)");
			return -1;
		}
		
		/*Recupero il publisher da eliminare*/
		Publisher p = em.find(Publisher.class, id);
		em.remove(p);
		
		//LOGGER.log( Level.INFO, "Publisher: remove Author success " + id);
		
		return 0;
	}

	public List<Publisher> findAllPublishers() {
		
		/*E' possibile usare la suppress warnings
		 * perché siamo sicuri che ciò che mi ritornerà sarà una lista
		 * di publisher oppure un unico valore*/
		@SuppressWarnings("unchecked")
		List<Publisher> result = (List<Publisher>) em.createQuery("FROM Publisher")
		.getResultList();
		
		return result;
	}
	
	
}
