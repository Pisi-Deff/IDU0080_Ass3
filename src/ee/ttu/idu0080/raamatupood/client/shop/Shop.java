package ee.ttu.idu0080.raamatupood.client.shop;

import ee.ttu.idu0080.raamatupood.client.Consts;

public class Shop {

	public static void main(String[] args) {
		ShopConsumer consumer = new ShopConsumer();
		consumer.run();
		
		ShopProducer producer = new ShopProducer();
		producer.run(Consts.ORDERS);
	}

}
