package database.hibernate;

// Generated Mar 1, 2011 4:41:30 PM by Hibernate Tools 3.3.0.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * TbCompanyInfo generated by hbm2java
 */
@Entity
@Table(name = "tbCompanyInfo", schema = "dbo", catalog = "broker", uniqueConstraints = @UniqueConstraint(columnNames = "companyName"))
public class TbCompanyInfo implements java.io.Serializable {

	private int companyId;
	private String companyName;
	private String dseMemNo;
	private String cseMemberNo;
	private String cdblParticularId;
	private String phoneNo;
	private String fax;
	private String email;
	private String address;
	private Integer userId;
	private String userIp;
	private Date entryTime;
	private String imageLoc;

	public TbCompanyInfo() {
	}

	public TbCompanyInfo(int companyId) {
		this.companyId = companyId;
	}

	public TbCompanyInfo(int companyId, String companyName, String dseMemNo,
			String cseMemberNo, String cdblParticularId, String phoneNo,
			String fax, String email, String address, Integer userId,
			String userIp, Date entryTime, String imageLoc) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.dseMemNo = dseMemNo;
		this.cseMemberNo = cseMemberNo;
		this.cdblParticularId = cdblParticularId;
		this.phoneNo = phoneNo;
		this.fax = fax;
		this.email = email;
		this.address = address;
		this.userId = userId;
		this.userIp = userIp;
		this.entryTime = entryTime;
		this.imageLoc = imageLoc;
	}

	@Id
	@Column(name = "companyId", unique = true, nullable = false)
	public int getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	@Column(name = "companyName", unique = true)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "dseMemNo")
	public String getDseMemNo() {
		return this.dseMemNo;
	}

	public void setDseMemNo(String dseMemNo) {
		this.dseMemNo = dseMemNo;
	}

	@Column(name = "cseMemberNo")
	public String getCseMemberNo() {
		return this.cseMemberNo;
	}

	public void setCseMemberNo(String cseMemberNo) {
		this.cseMemberNo = cseMemberNo;
	}

	@Column(name = "cdblParticularId")
	public String getCdblParticularId() {
		return this.cdblParticularId;
	}

	public void setCdblParticularId(String cdblParticularId) {
		this.cdblParticularId = cdblParticularId;
	}

	@Column(name = "phoneNo")
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Column(name = "fax")
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "userId")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "userIp", length = 50)
	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entryTime", length = 23)
	public Date getEntryTime() {
		return this.entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	
	@Column(name = "imageLoc")
	public String getImageLoc() {
		return this.imageLoc;
	}

	public void setImageLoc(String imageLoc) {
		this.imageLoc = imageLoc;
	}

}