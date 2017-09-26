package com.csc.csa.loader.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Party extends OptDataObjectBase{
	private String clientID;
	private String fullName;
	private String govID;
	private String govIdStatus;
	private int govIdType;
	private String searchFirstName; 
	private String searchLastName;	
	private String searchCompanyName;	
	private String searchAddressType;
	private Date searchDob;
	private int clientStatus;
	private int clientTypeID;
	private String partyType;
	private int genderId;
	private int preffredCommType;
	private String agentNumber;
	private String agentNumberLong;
	private int agentStatus;
	private String hostID;
	/**
	 * PANFRM-2424
	 * validate with uniA db for agentNumberLong and contractId, since both are composite key for uniqueness.
	 */
	private String contractId;
	
	private List<ClientAddress> clientAddrList=new ArrayList<>();
	private List<Address> AddrList=new ArrayList<>();
	private Portfolio portfolio=new Portfolio();
	private JobLogData logData;
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getGovID() {
		return govID;
	}
	public void setGovID(String govID) {
		this.govID = govID;
	}
	public String getGovIdStatus() {
		return govIdStatus;
	}
	public void setGovIdStatus(String govIdStatus) {
		this.govIdStatus = govIdStatus;
	}
	public int getGovIdType() {
		return govIdType;
	}
	public void setGovIdType(int govIdType) {
		this.govIdType = govIdType;
	}
	public String getSearchFirstName() {
		return searchFirstName;
	}
	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}
	public String getSearchLastName() {
		return searchLastName;
	}
	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}
	public String getSearchCompanyName() {
		return searchCompanyName;
	}
	public void setSearchCompanyName(String searchCompanyName) {
		this.searchCompanyName = searchCompanyName;
	}
	public String getSearchAddressType() {
		return searchAddressType;
	}
	public void setSearchAddressType(String searchAddressType) {
		this.searchAddressType = searchAddressType;
	}
	public Date getSearchDob() {
		return searchDob;
	}
	public void setSearchDob(Date searchDob) {
		this.searchDob = searchDob;
	}
	public int getClientStatus() {
		return clientStatus;
	}
	public void setClientStatus(int clientStatus) {
		this.clientStatus = clientStatus;
	}
	public int getClientTypeID() {
		return clientTypeID;
	}
	public void setClientTypeID(int clientTypeID) {
		this.clientTypeID = clientTypeID;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public int getPreffredCommType() {
		return preffredCommType;
	}
	public void setPreffredCommType(int preffredCommType) {
		this.preffredCommType = preffredCommType;
	}
	public Portfolio getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	public List<ClientAddress> getClientAddrList() {
		return clientAddrList;
	}
	public List<Address> getAddrList() {
		return AddrList;
	}
	public void updateClientIDInHeirarchy(){
		getPortfolio().setClientID(clientID);
		for(ClientAddress addr:clientAddrList){
			addr.setClientID(clientID);
		}
	}
	@Override
	public String toString(){
		StringBuffer retStr=new StringBuffer();
		retStr.append(Party.class.getName()).append("ClientID ").
		append(clientID).append("\r\n GOV ID ").append(govID)
		.append("\r\n LastName ").append(searchLastName)
		.append("\r\n partyKey ").append(getPartyKey())
		.append("\r\n OperationFlag ").append(getOperationFlag())
		.append("\r\n Policy Number ").append(portfolio.getContractID());
		retStr.append("\r\n").append(this.getPortfolio().toString());		
		return retStr.toString();
	}
	public void setClientAddrList(List<ClientAddress> clientAddrList) {
		this.clientAddrList = clientAddrList;
	}
	public void setAddrList(List<Address> addrList) {
		AddrList = addrList;
	}
	public String getPartyType() {
		return partyType;
	}
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}
	public String getAgentNumber() {
		return agentNumber;
	}
	public void setAgentNumber(String agentNumber) {
		this.agentNumber = agentNumber;
	}
	public String getAgentNumberLong() {
		return agentNumberLong;
	}
	public void setAgentNumberLong(String agentNumberLong) {
		this.agentNumberLong = agentNumberLong;
	}
	public int getAgentStatus() {
		return agentStatus;
	}
	public void setAgentStatus(int agentStatus) {
		this.agentStatus = agentStatus;
	}
	public JobLogData getLogData() {
		if(logData==null)
			logData=new JobLogData();
		return logData;
	}
	public void setLogData(JobLogData logData) {
		this.logData = logData;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}	
	
}
