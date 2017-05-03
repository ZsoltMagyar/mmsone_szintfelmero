package com.mmsone.szintfelmero;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class App {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
		List<Person> lista = new LinkedList<Person>();
		DataProcessor.parseJsonApi(lista, "https://api.myjson.com/bins/lgb9t");

		/*String fileName = "D:/Bemeno_adat.csv";

		String csvSeparator = ";";

		DataProcessor.parseCSV(lista, fileName, csvSeparator);*/
		

		BubbleListSort<Person> bls = new BubbleListSort<>(new BirthDateComparator());
		lista = bls.sort(lista);
		System.out.println("A személyek adatait tartalmazó csv fájl életkor szerint csökkenő sorrendbe rendezve, buborék rendezéssel:");
		for (Person item : lista) {
			System.out.println(item.toString());
		}
		
		InsertionListSort<Person> ils = new InsertionListSort<>(new NameComparator());
		lista = ils.sort(lista);
		System.out.println("A személyek adatait tartalmazó csv fájl név szerint rendezve, beszúró rendezéssel:");
		for (Person item : lista) {
			System.out.println(item.toString());
		}
	}
}
