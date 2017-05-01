package com.mmsone.szintfelmero;

import java.util.Comparator;

public class BirthDateComparator implements Comparator<Person>{

	public int compare(Person p1, Person p2) {
		return p1.getBirthDate().compareTo(p2.getBirthDate());
	}

}
