package com.mmsone.szintfelmero;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {

	private String name;

	private String gender;

	private Date birthDate;

	private String birthPlace;

	private String taxNumber;

	public Person(String name, String gender, Date birthDate, String birthPlace, String taxNumber) {
		super();
		this.name = name;
		this.gender = gender;
		this.birthDate = birthDate;
		this.birthPlace = birthPlace;
		this.taxNumber = taxNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	@Override
	public String toString() {
		return "Név: " + this.getName() + "\nNem: " + this.getGender() + "\nSzületési idő: "
				+ new SimpleDateFormat("yyyy.mm.dd").format(this.getBirthDate()) + "\nSzületési hely: "
				+ this.getBirthPlace() + "\nAdószám: " + this.getTaxNumber() + "\n";
	}

}
