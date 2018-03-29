package ee.ttu.idu0080.raamatupood.client.factory;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.client.Consts;
import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;
import ee.ttu.idu0080.raamatupood.types.Tellimus;

public class FactoryConsumer {
	private static final Logger LOG = Logger.getLogger(FactoryConsumer.class);
	
	private static final String TARGET = EmbeddedBroker.URL;
	private static final long SLEEP_TIME = 2 * 1000; // 2s
	
	private FactoryProducer producer;
	
	public FactoryConsumer(FactoryProducer producer) {
		this.producer = producer;
	}

	public void run() {
		Connection connection = null;
		try {
			LOG.info("factory consumer initializing");
			LOG.info("Connecting to URL: " + TARGET);
			LOG.info("Consuming queue : " + Consts.QUEUE_NEW_ORDERS);

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				TARGET
			);
			connection = connectionFactory.createConnection();
			connection.setExceptionListener(new ExceptionListenerImpl());

			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(Consts.QUEUE_NEW_ORDERS);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListenerImpl());
		} catch (Exception e) {
			LOG.fatal("Consumer: unexpected error", e);
		}
	}
	
	public void respond(Serializable serializable) {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {}
		
		if (!(serializable instanceof Tellimus)) {
			producer.run("Invalid request");
			return;
		}
		
		Tellimus t = (Tellimus) serializable;
		
		if (t.getTellimuseRida() == null || t.getTellimuseRida().isEmpty()) {
			producer.run("Empty request");
			return;
		}
		
		BigDecimal total = t.getTellimuseRida()
			.stream()
			.map(tr -> tr.getToode().getHind().multiply(BigDecimal.valueOf(tr.getKogus(), 0)))
			.reduce((a, b) -> a.add(b))
			.get();
		
		// total price ; possible errors
		producer.run("Total: " + total.toPlainString());
	}
	
	class MessageListenerImpl implements javax.jms.MessageListener {
		public void onMessage(Message message) {
			try {
				if (message instanceof ObjectMessage) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					String msg = objectMessage.getObject().toString();
					LOG.info("Received: " + msg);
					
					respond(objectMessage.getObject());
				} else {
					LOG.info("Consumer Received: " + message);
				}

			} catch (JMSException e) {
				LOG.warn("Consumer Caught: ", e);
			}
		}
	}

	class ExceptionListenerImpl implements javax.jms.ExceptionListener {
		public void onException(JMSException ex) {
			LOG.error("JMS Exception occured. Shutting down client.", ex);
		}
	}
}
