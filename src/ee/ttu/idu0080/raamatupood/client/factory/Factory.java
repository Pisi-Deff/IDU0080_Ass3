package ee.ttu.idu0080.raamatupood.client.factory;

public class Factory {

	public static void main(String[] args) {
		FactoryProducer producer = new FactoryProducer();
		FactoryConsumer consumer = new FactoryConsumer(producer);
		consumer.run();
	}

}
