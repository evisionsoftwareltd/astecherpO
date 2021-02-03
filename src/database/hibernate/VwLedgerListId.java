package database.hibernate;

// Generated Jan 11, 2011 10:26:51 AM by Hibernate Tools 3.3.0.GA

/**
 * VwLedgerListId generated by hbm2java
 */
public class VwLedgerListId implements java.io.Serializable {

	private String r;
	private String h;
	private String g;
	private String s;
	private String l;
	private String ledgerId;
	private Integer slno;

	public VwLedgerListId() {
	}

	public VwLedgerListId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public VwLedgerListId(String r, String h, String g, String s, String l,
			String ledgerId, Integer slno) {
		this.r = r;
		this.h = h;
		this.g = g;
		this.s = s;
		this.l = l;
		this.ledgerId = ledgerId;
		this.slno = slno;
	}

	public String getR() {
		return this.r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getH() {
		return this.h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getG() {
		return this.g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getS() {
		return this.s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getL() {
		return this.l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getLedgerId() {
		return this.ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Integer getSlno() {
		return this.slno;
	}

	public void setSlno(Integer slno) {
		this.slno = slno;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VwLedgerListId))
			return false;
		VwLedgerListId castOther = (VwLedgerListId) other;

		return ((this.getR() == castOther.getR()) || (this.getR() != null
				&& castOther.getR() != null && this.getR().equals(
				castOther.getR())))
				&& ((this.getH() == castOther.getH()) || (this.getH() != null
						&& castOther.getH() != null && this.getH().equals(
						castOther.getH())))
				&& ((this.getG() == castOther.getG()) || (this.getG() != null
						&& castOther.getG() != null && this.getG().equals(
						castOther.getG())))
				&& ((this.getS() == castOther.getS()) || (this.getS() != null
						&& castOther.getS() != null && this.getS().equals(
						castOther.getS())))
				&& ((this.getL() == castOther.getL()) || (this.getL() != null
						&& castOther.getL() != null && this.getL().equals(
						castOther.getL())))
				&& ((this.getLedgerId() == castOther.getLedgerId()) || (this
						.getLedgerId() != null
						&& castOther.getLedgerId() != null && this
						.getLedgerId().equals(castOther.getLedgerId())))
				&& ((this.getSlno() == castOther.getSlno()) || (this.getSlno() != null
						&& castOther.getSlno() != null && this.getSlno()
						.equals(castOther.getSlno())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getR() == null ? 0 : this.getR().hashCode());
		result = 37 * result + (getH() == null ? 0 : this.getH().hashCode());
		result = 37 * result + (getG() == null ? 0 : this.getG().hashCode());
		result = 37 * result + (getS() == null ? 0 : this.getS().hashCode());
		result = 37 * result + (getL() == null ? 0 : this.getL().hashCode());
		result = 37 * result
				+ (getLedgerId() == null ? 0 : this.getLedgerId().hashCode());
		result = 37 * result
				+ (getSlno() == null ? 0 : this.getSlno().hashCode());
		return result;
	}

}
