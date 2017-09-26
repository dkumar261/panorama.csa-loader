package com.csc.csa.loader.domain;

public class Portfolio extends OptDataObjectBase{
	private String clientID;
	private String clientContractID;
	private String hostID;
	private String contractID;	
	private String hostContractID;
	private int participationID;
	private String policyStatus;
	private double faceAmt;
	private String productType;
	private String plan;
	private String groupBilling;
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientContractID() {
		return clientContractID;
	}
	public void setClientContractID(String clientContractID) {
		this.clientContractID = clientContractID;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	public String getContractID() {
		return contractID;
	}
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}
	public String getHostContractID() {
		return hostContractID;
	}
	public void setHostContractID(String hostContractID) {
		this.hostContractID = hostContractID;
	}
	public int getParticipationID() {
		return participationID;
	}
	public void setParticipationID(int participationID) {
		this.participationID = participationID;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	public double getFaceAmt() {
		return faceAmt;
	}
	public void setFaceAmt(double faceAmt) {
		this.faceAmt = faceAmt;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	@Override
	public String toString(){
		StringBuffer retStr=new StringBuffer();
		retStr.append(Portfolio.class.getName()).append("\r\n ClientID ").
		append(clientID).append("\r\n clientContractID ").append(clientContractID)
		.append("\r\n ContractID ").append(contractID);
		return retStr.toString();
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	//Charger: Begin	
	public String getGroupBilling() {
		return groupBilling;
	}
	public void setGroupBilling(String groupBilling) {
		this.groupBilling = groupBilling;
	}	
	//Charger: End	
}
