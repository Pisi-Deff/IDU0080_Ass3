package ee.ttu.idu0080.raamatupood.client.factory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.client.Consts;
import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;

public class FactoryProducer {
	private static final Logger LOG = Logger.getLogger(FactoryProducer.class);
	
	private static final long TTL = 60 * 1000; // 60s
	private static final String TARGET = EmbeddedBroker.URL;
	
	public void run(String message) {
		Connection connection = null;
		try {
			LOG.info("factory producer initializing");
			LOG.info("Connecting to URL: " + TARGET);
			if (TTL != 0) {
				LOG.debug("Messages time to live " + TTL + " ms");
			}

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				TARGET
			);
			connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue(Consts.QUEUE_ORDER_RESPONSES);
			MessageProducer producer = session.createProducer(destination);

			// producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.setTimeToLive(TTL);
			
			sendMsg(session, producer, message);
		} catch (Exception e) {
			LOG.fatal("unexpected error", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Throwable t) {}
			}
		}
	}

	private void sendMsg(Session session, MessageProducer producer, String message)
			throws JMSException {
		TextMessage msg = session.createTextMessage(message);
		producer.send(msg);
	}
}
