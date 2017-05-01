package com.mmsone.szintfelmero;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class DataProcessor {

	public static void parseCSV(List<Person> lista, String fileName, String csvSeparator)
			throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
		String line;
		int i = 0;

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

		while ((line = br.readLine()) != null) {

			String[] personData = line.split(csvSeparator);

			if (validate(personData)) {
				String name = personData[0];

				Date birthDate = new SimpleDateFormat("yyyy.mm.dd").parse(personData[1]);

				String birthPlace = personData[2];

				String gender = personData[3];

				String taxNumber = personData[4];

				lista.add(new Person(name, gender, birthDate, birthPlace, taxNumber));
			}
			i++;
		}
		System.out.println("A megfelelő rekordok(személyek) száma: " + lista.size());
		System.out.println("Hibás sorok száma: " + (i - lista.size()) + "\n");
		br.close();
	}

	private static boolean validate(String[] personData) {

		if (personData[0].matches("[a-zA-Z ]*") && dataValidator(personData[1])
				&& (personData[3].equals("Férfi") || personData[3].equals("Nő"))) {
			return true;
		} else
			return false;
	}

	private static boolean dataValidator(String dateToValidate) {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd");
		sdf.setLenient(false);

		try {
			Date date = sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

}
