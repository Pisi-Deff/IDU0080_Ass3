package ee.ttu.idu0080.raamatupood.client.shop;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.client.Consts;
import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;

public class ShopConsumer {
	private static final Logger LOG = Logger.getLogger(ShopConsumer.class);
	
	private static final String TARGET = EmbeddedBroker.URL;

	public void run() {
		Connection connection = null;
		try {
			LOG.info("shop consumer initializing");
			LOG.info("Connecting to URL: " + TARGET);
			LOG.info("Consuming queue : " + Consts.QUEUE_ORDER_RESPONSES);

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
			Destination destination = session.createQueue(Consts.QUEUE_ORDER_RESPONSES);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListenerImpl());
		} catch (Exception e) {
			LOG.fatal("unexpected error", e);
		}
	}
	class MessageListenerImpl implements javax.jms.MessageListener {
		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					TextMessage txtMsg = (TextMessage) message;
					String msg = txtMsg.getText();
					LOG.info("Received: " + msg);
				} else {
					LOG.info("Received: " + message);
				}

			} catch (JMSException e) {
				LOG.warn("Caught: ", e);
			}
		}
	}

	class ExceptionListenerImpl implements javax.jms.ExceptionListener {
		public void onException(JMSException ex) {
			LOG.error("JMS Exception occured. Shutting down client.", ex);
		}
	}
}
