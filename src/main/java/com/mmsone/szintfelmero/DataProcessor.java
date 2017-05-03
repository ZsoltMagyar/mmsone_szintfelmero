package com.mmsone.szintfelmero;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class DataProcessor {

	public static void parseJsonApi(List<Person> lista, String urlParameter)
			throws IOException, JSONException, ParseException {
		URL url = new URL(urlParameter);
		URLConnection connection = url.openConnection();
		JSONTokener tokener = new JSONTokener(new InputStreamReader(connection.getInputStream()));
		JSONArray jsonArray = new JSONArray(tokener);
		Iterator<?> it = jsonArray.iterator();

		while (it.hasNext()) {
			JSONObject obj = new JSONObject(it.next().toString());

			String name = obj.get("name").toString();

			String bDate = obj.get("birthDate").toString();

			String birthPlace = obj.get("birthPlace").toString();

			String gender = obj.get("gender").toString();

			String taxNumber = obj.get("taxnumber").toString();

			if (validate(name, bDate, gender)) {
				Date birthDate = new SimpleDateFormat("yyyy.mm.dd").parse(obj.get("birthDate").toString());
				lista.add(new Person(name, gender, birthDate, birthPlace, taxNumber));
			}
		}
	}

	public static void parseCSV(List<Person> lista, String fileName, String csvSeparator)
			throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
		String line;
		int i = 0;

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

		while ((line = br.readLine()) != null) {

			String[] personData = line.split(csvSeparator);

			if (validate(personData[0], personData[1], personData[3])) {
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

	private static boolean validate(String name, String birthDate, String gender) throws ParseException {

		if (name.matches("[a-zA-Z ]*") && dataValidator(birthDate) && (gender.equals("Férfi") || gender.equals("Nő"))) {
			return true;
		} else
			return false;
	}

	private static boolean dataValidator(String dateToValidate) throws ParseException {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd");
		sdf.setLenient(false);
		
		try {
			Date date = sdf.parse(dateToValidate);
			if(date.after(new Date())) return false;
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

}
