package ee.ttu.idu0080.raamatupood.types;

import java.io.Serializable;
import java.math.BigDecimal;

public class Toode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer kood;
	private String nimetus;
	private BigDecimal hind;
	
	public Integer getKood() {
		return kood;
	}
	public void setKood(Integer kood) {
		this.kood = kood;
	}
	
	public String getNimetus() {
		return nimetus;
	}
	public void setNimetus(String nimetus) {
		this.nimetus = nimetus;
	}
	
	public BigDecimal getHind() {
		return hind;
	}
	public void setHind(BigDecimal hind) {
		this.hind = hind;
	}
}
