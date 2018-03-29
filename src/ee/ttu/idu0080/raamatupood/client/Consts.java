package ee.ttu.idu0080.raamatupood.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ee.ttu.idu0080.raamatupood.types.Tellimus;
import ee.ttu.idu0080.raamatupood.types.TellimuseRida;
import ee.ttu.idu0080.raamatupood.types.Toode;

public class Consts {
	public static final String QUEUE_NEW_ORDERS = "tellimus.edastamine";
	public static final String QUEUE_ORDER_RESPONSES = "tellimus.vastus";
	
	public static final List<Tellimus> ORDERS = new ArrayList<>();
	static {
		Toode t1 = new Toode();
		t1.setHind(BigDecimal.valueOf(1234, 2));
		t1.setNimetus("leib");
		t1.setKood(7);
		
		Toode t2 = new Toode();
		t2.setHind(BigDecimal.valueOf(99));
		t2.setNimetus("sai");
		t2.setKood(42);
		
		Toode t3 = new Toode();
		t3.setHind(BigDecimal.valueOf(395, 1));
		t3.setNimetus("pirukas");
		t3.setKood(99);
		
		
		
		Tellimus o1 = new Tellimus();
		ORDERS.add(o1);
		List<TellimuseRida> rows1 = new ArrayList<>();
		o1.setTellimuseRida(rows1);
		
		TellimuseRida r11 = new TellimuseRida();
		r11.setToode(t1);
		r11.setKogus(3l);
		rows1.add(r11);
		
		TellimuseRida r12 = new TellimuseRida();
		r12.setToode(t3);
		r12.setKogus(1l);
		rows1.add(r12);
		
		
		
		Tellimus o2 = new Tellimus();
		ORDERS.add(o2);
		List<TellimuseRida> rows2 = new ArrayList<>();
		o2.setTellimuseRida(rows2);
		
		TellimuseRida r21 = new TellimuseRida();
		r21.setToode(t2);
		r21.setKogus(5l);
		rows2.add(r21);
		
		
		
		Tellimus o3 = new Tellimus();
		ORDERS.add(o3);
		List<TellimuseRida> rows3 = new ArrayList<>();
		o3.setTellimuseRida(rows3);
		
		TellimuseRida r31 = new TellimuseRida();
		r31.setToode(t2);
		r31.setKogus(5l);
		rows3.add(r31);
		
		TellimuseRida r32 = new TellimuseRida();
		r32.setToode(t2);
		r32.setKogus(5l);
		rows3.add(r32);
		
		TellimuseRida r33 = new TellimuseRida();
		r33.setToode(t2);
		r33.setKogus(5l);
		rows3.add(r33);
	}
}
