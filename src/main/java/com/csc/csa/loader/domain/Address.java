package com.csc.csa.loader.domain;

public class Address extends OptDataObjectBase{
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String city;
	private int state=-1;
	private String country;
	private String zip;
	private String addID;
	private String searchAddressType;
	private int searchAddressTypeCode;
	private String hostID;
	private String clientID;
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getLine3() {
		return line3;
	}
	public void setLine3(String line3) {
		this.line3 = line3;
	}
	public String getLine4() {
		return line4;
	}
	public void setLine4(String line4) {
		this.line4 = line4;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddID() {
		return addID;
	}
	public void setAddID(String addID) {
		this.addID = addID;
	}
	public String getSearchAddressType() {
		return searchAddressType;
	}
	public void setSearchAddressType(String searchAddressType) {
		this.searchAddressType = searchAddressType;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	@Override
	public String toString(){
		StringBuffer retStr=new StringBuffer();
		retStr.append(Address.class.getName()).append("address ID ").append(addID);
		return retStr.toString();
	}
	public int getSearchAddressTypeCode() {
		return searchAddressTypeCode;
	}
	public void setSearchAddressTypeCode(int searchAddressTypeCode) {
		this.searchAddressTypeCode = searchAddressTypeCode;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
}
