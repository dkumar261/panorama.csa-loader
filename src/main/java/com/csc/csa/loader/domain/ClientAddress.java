package com.csc.csa.loader.domain;

public class ClientAddress extends OptDataObjectBase{
	private String clientID;
	private String addID;
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getAddID() {
		return addID;
	}
	public void setAddID(String addID) {
		this.addID = addID;
	}
	@Override
	public String toString(){
		StringBuffer retStr=new StringBuffer();
		retStr.append(ClientAddress.class.getName()).append("ClientID ").
		append(clientID).append("address ID ").append(addID);
		return retStr.toString();
	}
}
