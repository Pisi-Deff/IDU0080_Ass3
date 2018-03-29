package ee.ttu.idu0080.raamatupood.types;

import java.io.Serializable;
import java.util.List;

public class Tellimus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<TellimuseRida> tellimuseRida;

	public List<TellimuseRida> getTellimuseRida() {
		return tellimuseRida;
	}
	public void setTellimuseRida(List<TellimuseRida> tellimuseRida) {
		this.tellimuseRida = tellimuseRida;
	}
}
