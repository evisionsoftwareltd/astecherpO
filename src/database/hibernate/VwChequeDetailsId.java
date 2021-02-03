package database.hibernate;

// Generated Jan 11, 2011 10:26:51 AM by Hibernate Tools 3.3.0.GA

import java.util.Date;

/**
 * VwChequeDetailsId generated by hbm2java
 */
public class VwChequeDetailsId implements java.io.Serializable {

	private String chequeNo;
	private Date chequeDate;
	private String partyAccountDetails;
	private String voucherNo;
	private String bankId;
	private Date clearanceDate;
	private String bankName;
	private String branchName;
	private int mrNo;
	private int payNo;
	private Integer pbankId;
	private Integer pbranchId;

	public VwChequeDetailsId() {
	}

	public VwChequeDetailsId(String chequeNo, Date chequeDate,
			String voucherNo, String bankId, int mrNo, int payNo) {
		this.chequeNo = chequeNo;
		this.chequeDate = chequeDate;
		this.voucherNo = voucherNo;
		this.bankId = bankId;
		this.mrNo = mrNo;
		this.payNo = payNo;
	}

	public VwChequeDetailsId(String chequeNo, Date chequeDate,
			String partyAccountDetails, String voucherNo, String bankId,
			Date clearanceDate, String bankName, String branchName, int mrNo,
			int payNo, Integer pbankId, Integer pbranchId) {
		this.chequeNo = chequeNo;
		this.chequeDate = chequeDate;
		this.partyAccountDetails = partyAccountDetails;
		this.voucherNo = voucherNo;
		this.bankId = bankId;
		this.clearanceDate = clearanceDate;
		this.bankName = bankName;
		this.branchName = branchName;
		this.mrNo = mrNo;
		this.payNo = payNo;
		this.pbankId = pbankId;
		this.pbranchId = pbranchId;
	}

	public String getChequeNo() {
		return this.chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChequeDate() {
		return this.chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getPartyAccountDetails() {
		return this.partyAccountDetails;
	}

	public void setPartyAccountDetails(String partyAccountDetails) {
		this.partyAccountDetails = partyAccountDetails;
	}

	public String getVoucherNo() {
		return this.voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Date getClearanceDate() {
		return this.clearanceDate;
	}

	public void setClearanceDate(Date clearanceDate) {
		this.clearanceDate = clearanceDate;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getMrNo() {
		return this.mrNo;
	}

	public void setMrNo(int mrNo) {
		this.mrNo = mrNo;
	}

	public int getPayNo() {
		return this.payNo;
	}

	public void setPayNo(int payNo) {
		this.payNo = payNo;
	}

	public Integer getPbankId() {
		return this.pbankId;
	}

	public void setPbankId(Integer pbankId) {
		this.pbankId = pbankId;
	}

	public Integer getPbranchId() {
		return this.pbranchId;
	}

	public void setPbranchId(Integer pbranchId) {
		this.pbranchId = pbranchId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VwChequeDetailsId))
			return false;
		VwChequeDetailsId castOther = (VwChequeDetailsId) other;

		return ((this.getChequeNo() == castOther.getChequeNo()) || (this
				.getChequeNo() != null && castOther.getChequeNo() != null && this
				.getChequeNo().equals(castOther.getChequeNo())))
				&& ((this.getChequeDate() == castOther.getChequeDate()) || (this
						.getChequeDate() != null
						&& castOther.getChequeDate() != null && this
						.getChequeDate().equals(castOther.getChequeDate())))
				&& ((this.getPartyAccountDetails() == castOther
						.getPartyAccountDetails()) || (this
						.getPartyAccountDetails() != null
						&& castOther.getPartyAccountDetails() != null && this
						.getPartyAccountDetails().equals(
								castOther.getPartyAccountDetails())))
				&& ((this.getVoucherNo() == castOther.getVoucherNo()) || (this
						.getVoucherNo() != null
						&& castOther.getVoucherNo() != null && this
						.getVoucherNo().equals(castOther.getVoucherNo())))
				&& ((this.getBankId() == castOther.getBankId()) || (this
						.getBankId() != null && castOther.getBankId() != null && this
						.getBankId().equals(castOther.getBankId())))
				&& ((this.getClearanceDate() == castOther.getClearanceDate()) || (this
						.getClearanceDate() != null
						&& castOther.getClearanceDate() != null && this
						.getClearanceDate()
						.equals(castOther.getClearanceDate())))
				&& ((this.getBankName() == castOther.getBankName()) || (this
						.getBankName() != null
						&& castOther.getBankName() != null && this
						.getBankName().equals(castOther.getBankName())))
				&& ((this.getBranchName() == castOther.getBranchName()) || (this
						.getBranchName() != null
						&& castOther.getBranchName() != null && this
						.getBranchName().equals(castOther.getBranchName())))
				&& (this.getMrNo() == castOther.getMrNo())
				&& (this.getPayNo() == castOther.getPayNo())
				&& ((this.getPbankId() == castOther.getPbankId()) || (this
						.getPbankId() != null && castOther.getPbankId() != null && this
						.getPbankId().equals(castOther.getPbankId())))
				&& ((this.getPbranchId() == castOther.getPbranchId()) || (this
						.getPbranchId() != null
						&& castOther.getPbranchId() != null && this
						.getPbranchId().equals(castOther.getPbranchId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getChequeNo() == null ? 0 : this.getChequeNo().hashCode());
		result = 37
				* result
				+ (getChequeDate() == null ? 0 : this.getChequeDate()
						.hashCode());
		result = 37
				* result
				+ (getPartyAccountDetails() == null ? 0 : this
						.getPartyAccountDetails().hashCode());
		result = 37 * result
				+ (getVoucherNo() == null ? 0 : this.getVoucherNo().hashCode());
		result = 37 * result
				+ (getBankId() == null ? 0 : this.getBankId().hashCode());
		result = 37
				* result
				+ (getClearanceDate() == null ? 0 : this.getClearanceDate()
						.hashCode());
		result = 37 * result
				+ (getBankName() == null ? 0 : this.getBankName().hashCode());
		result = 37
				* result
				+ (getBranchName() == null ? 0 : this.getBranchName()
						.hashCode());
		result = 37 * result + this.getMrNo();
		result = 37 * result + this.getPayNo();
		result = 37 * result
				+ (getPbankId() == null ? 0 : this.getPbankId().hashCode());
		result = 37 * result
				+ (getPbranchId() == null ? 0 : this.getPbranchId().hashCode());
		return result;
	}

}
