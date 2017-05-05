package com.mmsone.szintfelmero.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mmsone.szintfelmero.BirthDateComparator;
import com.mmsone.szintfelmero.BubbleListSort;
import com.mmsone.szintfelmero.DataProcessor;
import com.mmsone.szintfelmero.InsertionListSort;
import com.mmsone.szintfelmero.NameComparator;
import com.mmsone.szintfelmero.Person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller implements Initializable {

	private List<Person> lista;
	private ObservableList<Person> oList;
	private FileChooser fileDialog;
	private ObservableList<XYChart.Series<String, Number>> chartData;

	public Label infoLabel;
	public CategoryAxis xAxis;
	public NumberAxis yAxis;
	public TableColumn<Person, String> nameColumn;
	public TableColumn<Person, String> genderColumn;
	public TableColumn<Person, Date> birthDateColumn;
	public TableColumn<Person, String> birthPlaceColumn;
	public TableColumn<Person, String> taxNumberColumn;
	public Button openCSVButton;
	public TableView<Person> table;
	public BarChart<String, Number> barChart;
	public ComboBox<String> comboBox;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resource) {
		lista = new LinkedList<>();

		comboBox.getItems().addAll("BubbleSort by name", "BubbleSort by birth date", "InsertionSort by name",
				"InsertionSort by birth date", "Java built-in sort by name", "Java built-in sort by birth date");

		nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setSortable(false);

		genderColumn = new TableColumn<>("Gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		genderColumn.setSortable(false);

		birthDateColumn = new TableColumn<>("Birth Date");
		birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		birthDateColumn.setSortable(false);

		birthPlaceColumn = new TableColumn<>("Birth Place");
		birthPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("birthPlace"));
		birthPlaceColumn.setSortable(false);

		taxNumberColumn = new TableColumn<>("Tax Number");
		taxNumberColumn.setCellValueFactory(new PropertyValueFactory<>("taxNumber"));
		taxNumberColumn.setSortable(false);

		table.getColumns().addAll(nameColumn, genderColumn, birthDateColumn, birthPlaceColumn, taxNumberColumn);

		table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			try {
				setInfoLabelText(newValue);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private void setInfoLabelText(Person p) throws IOException, ParseException {
		String txt = "";
		txt += p.getName() + "\n";

		URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q={" + p.getBirthPlace()
				+ "}&appid=592ff1f3e758d14e209c1ee0483ae763");
		URLConnection connection = url.openConnection();
		JSONTokener tokener = new JSONTokener(new InputStreamReader(connection.getInputStream()));
		JSONObject jobj = new JSONObject(tokener);
		JSONArray jsonArray = new JSONArray(jobj.get("weather").toString());

		Iterator<?> it = jsonArray.iterator();
		String description = "";
		while (it.hasNext()) {
			JSONObject obj = new JSONObject(it.next().toString());
			description += obj.get("description").toString();
		}

		txt += "Birth Place: " + p.getBirthPlace() + "\nWeather in " + p.getBirthPlace() + ": " + description + "\nCheckSum is (nem működik :( ): "
				+ validateTaxNumber(p.getTaxNumber(), p);
		infoLabel.setText(txt);
	}

	public void openCSV() throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
		clearTable();
		fileDialog = new FileChooser();
		fileDialog.setTitle("Open a CSV file");
		fileDialog.setInitialDirectory(new File("C:\\"));

		Stage stage = (Stage) openCSVButton.getScene().getWindow();
		File file = fileDialog.showOpenDialog(stage);
		String csvSeparator = ";";

		DataProcessor.parseCSV(lista, file.getAbsolutePath(), csvSeparator);

		int numberOfWomen = countWomen();
		chartData = getGenderSeries(numberOfWomen);
		barChart.setData(chartData);

		oList = FXCollections.observableList(lista);
		table.setItems(oList);
		table.refresh();
	}

	public void loadAPI() throws JSONException, IOException, ParseException {
		clearTable();
		DataProcessor.parseJsonApi(lista, "http://api.myjson.com/bins/lgb9t");

		int numberOfWomen = countWomen();
		chartData = getGenderSeries(numberOfWomen);
		barChart.setData(chartData);

		oList = FXCollections.observableList(lista);
		table.setItems(oList);
		table.refresh();
	}

	public void saveToCSV() {
		FileChooser saveToFile = new FileChooser();
		saveToFile.setTitle("Save to CSV");
		saveToFile.setInitialFileName("untitled.csv");
		saveToFile.setInitialDirectory(new File("C:\\"));
		saveToFile.getExtensionFilters().add(new ExtensionFilter("CSV File", "*.csv"));
		File file = saveToFile.showSaveDialog((Stage) openCSVButton.getScene().getWindow());
		if (file == null) {
			return;
		}
		try {
			String output = "";
			for (Person p : oList) {
				output = output + p.toString();
			}
			Files.write(file.toPath(), output.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int countWomen() {
		int numberOfWomen = 0;
		for (Person p : lista) {
			if (p.getGender().equals("Nő"))
				numberOfWomen++;
		}
		return numberOfWomen;
	}

	public void sortTable() {
		switch (comboBox.getValue()) {
		case "BubbleSort by name":
			BubbleListSort<Person> blsByName = new BubbleListSort<>(new NameComparator());
			blsByName.sort(lista);
			oList = FXCollections.observableList(lista);
			break;
		case "BubbleSort by birth date":
			BubbleListSort<Person> blsByBDate = new BubbleListSort<>(new BirthDateComparator());
			blsByBDate.sort(lista);
			oList = FXCollections.observableList(lista);
			break;
		case "InsertionSort by name":
			InsertionListSort<Person> ilsByName = new InsertionListSort<>(new NameComparator());
			ilsByName.sort(lista);
			oList = FXCollections.observableList(lista);
			break;
		case "InsertionSort by birth date":
			InsertionListSort<Person> ilsByBDate = new InsertionListSort<>(new BirthDateComparator());
			ilsByBDate.sort(lista);
			oList = FXCollections.observableList(lista);
			break;
		case "Java built-in sort by name":
			Collections.sort(lista, new NameComparator());
			oList = FXCollections.observableList(lista);
			break;
		case "Java built-in sort by birth date":
			Collections.sort(lista, new BirthDateComparator());
			oList = FXCollections.observableList(lista);
			break;
		}

		table.refresh();
	}

	public void primBirthDate() {
		LinkedList<Person> primeList = new LinkedList<>();
		Calendar cal = Calendar.getInstance();
		for (Person p : lista) {
			cal.setTime(p.getBirthDate());
			int year = cal.get(Calendar.YEAR);
			if (isPrime(year)) {
				primeList.add(p);
			}
		}
		oList = FXCollections.observableList(primeList);
		table.setItems(oList);
		table.refresh();
	}

	private boolean validateTaxNumber(String taxNumber, Person p) throws ParseException {
		StringBuilder sb = new StringBuilder(taxNumber);
		if ((sb.charAt(0)) != '8')
			return false;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd");

		Date date = p.getBirthDate();
		cal1.setTime(date);
		date = sdf.parse("1967.02.13");
		cal2.setTime(date);

		int days = daysBetween(cal1.getTime(), cal2.getTime());
		System.out.println();
		if (!sb.subSequence(2, 6).equals(days)) {
			System.out.println("2-6 nem egyenlő");
			return false;
		}
		if (Integer.parseInt(sb.subSequence(7, 9).toString()) != 0)
			return false;
		int checkSum = 0;
		checkSum += Integer.valueOf(sb.charAt(0)) + Integer.valueOf(sb.charAt(1)) * 2
				+ Integer.valueOf(sb.charAt(2)) * 3 + Integer.valueOf(sb.charAt(3)) * 4
				+ Integer.valueOf(sb.charAt(4)) * 5 + Integer.valueOf(sb.charAt(5)) * 6
				+ Integer.valueOf(sb.charAt(6)) * 7 + Integer.valueOf(sb.charAt(7)) * 8
				+ Integer.valueOf(sb.charAt(8)) * 9;
		checkSum %= 11;
		if (Integer.valueOf(sb.charAt(9)) == checkSum)
			return true;
		return false;
	}

	public int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	private boolean isPrime(int n) {
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	private void clearTable() {
		if (oList != null && !(oList.isEmpty())) {
			oList.removeAll(oList);
			oList = null;
			table.refresh();
		}
	}

	public void clearAll() {
		clearTable();
		lista.removeAll(lista);
		barChart.setData(null);
	}

	@SuppressWarnings("unchecked")
	public ObservableList<XYChart.Series<String, Number>> getGenderSeries(int numberOfWomen) {
		XYChart.Series<String, Number> nemek = new XYChart.Series<>();
		nemek.getData().addAll(new XYChart.Data<>("Férfiak", (lista.size() - numberOfWomen)),
				new XYChart.Data<>("Nők", numberOfWomen));
		ObservableList<XYChart.Series<String, Number>> data = FXCollections.<XYChart.Series<String, Number>>observableArrayList();
		data.addAll(nemek);
		return data;
	}
}
