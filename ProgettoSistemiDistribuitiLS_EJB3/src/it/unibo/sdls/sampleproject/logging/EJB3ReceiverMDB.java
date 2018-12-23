package it.unibo.sdls.sampleproject.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "java:/jms/queue/MyQueue"
        )
})       
public class EJB3ReceiverMDB implements MessageListener{
	public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            
            try {
            	LOGGER.log(Level.INFO, "Message driven bean message was found: " + textMessage.getText());
            	
            }catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
		}
	}

}
