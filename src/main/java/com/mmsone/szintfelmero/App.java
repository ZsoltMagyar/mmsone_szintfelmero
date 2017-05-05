package com.mmsone.szintfelmero;

import java.io.IOException;
import java.text.ParseException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
	public static void main(String[] args) throws IOException, ParseException{
		Application.launch(args);	
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/com/mmsone/szintfelmero/gui/gui.fxml"));
		stage.setTitle("MMSOne szintfelmérő");
		stage.setScene(new Scene(root, 860, 640));
		stage.show();
	}
}
