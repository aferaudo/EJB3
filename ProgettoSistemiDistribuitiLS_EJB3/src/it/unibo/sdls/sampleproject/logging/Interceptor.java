package it.unibo.sdls.sampleproject.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class Interceptor {

	@AroundInvoke
	public Object sendMessage(InvocationContext ctx) throws Exception {
		//java:/jms/queue/MyQueue
		//java:/myJmsTest/MyConnectionFactory
		
		InitialContext context = new InitialContext();
		
		/*Recupero valori da JNDI*/
		Queue coda = (Queue) context.lookup("java:/jms/queue/MyQueue");
		QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("java:/myJmsTest/MyConnectionFactory");
		
		/*Creo il canale di comunicazion*/
		QueueConnection conn = factory.createQueueConnection();
		
		/*Recupero la sessione per l'invio del messaggio*/
		QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		
		/*Recupero il sender*/
		QueueSender sender = session.createSender(coda);
		
		/*Inizio il flusso dei messaggi*/
		conn.start();
		
		TextMessage message = session.createTextMessage();
		
		if (ctx.getMethod().getName().equals("insertAuthor")) {
			
			System.out.println("Add Author");
			message.setText("An author was added");
			sender.send(message);
			
		}else if(ctx.getMethod().getName().equals("insertPublisher")) {
			
			System.out.println("Add Publisher");
			message.setText("A publisher was added");
			sender.send(message);
			
		}else if(ctx.getMethod().getName().equals("addBook")) {
			
			System.out.println("Add Book");
			message.setText("A book was added");
			sender.send(message);
			
		}
		
		sender.close();
		conn.close();
		
		return ctx.proceed();
	}
	
}
