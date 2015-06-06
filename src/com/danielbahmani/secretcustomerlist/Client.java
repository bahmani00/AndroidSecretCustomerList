package com.danielbahmani.secretcustomerlist;

public class Client {
	int id;
	String fullname;
	long birthdate;
	boolean gender;
	String picture;
	String address;
	String postalcode;
	String notes;
	
	public Client(int id, String fullname) {
		this.id = id;
		this.fullname = fullname;
	}
	
	public String getName() {
		return fullname;
	}
	public void setName(String fullname) {
		this.fullname = fullname;
	}

	public long getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(int birthdate) {
		this.birthdate = birthdate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}