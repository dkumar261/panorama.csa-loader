package com.csc.csa.loader.domain;

import java.util.Date;

public class OptDataObjectBase {
	private String updatedBy;
	private Date updatedWhen;
	private String operationFlag;
	private String partyKey;
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedWhen() {
		return updatedWhen;
	}
	public void setUpdatedWhen(Date updatedWhen) {
		this.updatedWhen = updatedWhen;
	}
	public String getOperationFlag() {
		return operationFlag;
	}
	public void setOperationFlag(String operationFlag) {
		this.operationFlag = operationFlag;
	}
	public String getPartyKey() {
		return partyKey;
	}
	public void setPartyKey(String partyKey) {
		this.partyKey = partyKey;
	}	
}
