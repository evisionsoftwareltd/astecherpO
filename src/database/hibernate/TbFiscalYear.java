package database.hibernate;

// Generated Jan 11, 2011 10:26:51 AM by Hibernate Tools 3.3.0.GA

import java.util.Date;

/**
 * TbFiscalYear generated by hbm2java
 */
public class TbFiscalYear implements java.io.Serializable {

	private int slNo;
	private int autoId;
	private Date opDate;
	private Date clDate;
	private int runningFlag;

	public TbFiscalYear() {
	}

	public TbFiscalYear(int slNo, int autoId, int runningFlag) {
		this.slNo = slNo;
		this.autoId = autoId;
		this.runningFlag = runningFlag;
	}

	public TbFiscalYear(int slNo, int autoId, Date opDate, Date clDate,
			int runningFlag) {
		this.slNo = slNo;
		this.autoId = autoId;
		this.opDate = opDate;
		this.clDate = clDate;
		this.runningFlag = runningFlag;
	}

	public int getSlNo() {
		return this.slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public int getAutoId() {
		return this.autoId;
	}

	public void setAutoId(int autoId) {
		this.autoId = autoId;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public Date getClDate() {
		return this.clDate;
	}

	public void setClDate(Date clDate) {
		this.clDate = clDate;
	}

	public int getRunningFlag() {
		return this.runningFlag;
	}

	public void setRunningFlag(int runningFlag) {
		this.runningFlag = runningFlag;
	}

}
