package ee.ttu.idu0080.raamatupood.client.shop;

import java.time.LocalDateTime;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.client.Consts;
import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;
import ee.ttu.idu0080.raamatupood.types.Tellimus;

public class ShopProducer {
	private static final Logger LOG = Logger.getLogger(ShopProducer.class);
	
	private static final long SLEEP_TIME = 3 * 1000; // 3s
	private static final long TTL = 60 * 1000; // 60s
	private static final String TARGET = EmbeddedBroker.URL;
	
	public void run(List<Tellimus> orders) {
		Connection connection = null;
		try {
			LOG.info("shop producer initializing");
			LOG.info("Connecting to URL: " + TARGET);
			LOG.debug("Sleeping between publish " + SLEEP_TIME + " ms");
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

			Destination destination = session.createQueue(Consts.QUEUE_NEW_ORDERS);
			MessageProducer producer = session.createProducer(destination);

			// producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.setTimeToLive(TTL);
			sendLoop(session, producer, orders);
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
	
	private void sendLoop(Session session, MessageProducer producer, List<Tellimus> orders)
			throws JMSException, InterruptedException {
		for (int i = 0; i < orders.size(); i++) {
			ObjectMessage objMsg = session.createObjectMessage(orders.get(i));
			producer.send(objMsg);
			
			TextMessage msg = session.createTextMessage("msg " + i + " sent at " + LocalDateTime.now().toString());
			LOG.debug(msg.getText());
			producer.send(msg);
			
			Thread.sleep(SLEEP_TIME);
		}
	}
}
