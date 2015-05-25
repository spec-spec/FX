package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ZipController implements Initializable {
	Logger log = Logger.getLogger(getClass().getName());
	@FXML
	private TextArea mainTextArea;
	@FXML
	private Label stringCount;
	@FXML
	private TextField searchTextField;
	File f = new File("C:/Text.txt");
	private int fromIndex = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showRowsNum();
	}

	public void openFile(ActionEvent event) {
		log.info("open function");
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String s;
			while ((s = br.readLine()) != null) {
				mainTextArea.appendText(s);
			}
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveTextToFile(ActionEvent event) {
		log.info("save function");

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append(mainTextArea.getText());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void searchText(ActionEvent event) {
		String textToFind = searchTextField.getText();
		String whereToFind = mainTextArea.getText();
		int currentStringPlace = whereToFind.indexOf(textToFind, fromIndex);
		if (currentStringPlace != -1) {
			mainTextArea.selectRange(textToFind.length() + currentStringPlace,
					currentStringPlace);
			fromIndex = textToFind.length() + currentStringPlace;
			log.info("find in place :" + currentStringPlace);
		} else {
			searchTextField.clear();
			fromIndex = 0;
		}
	}

	public void showRowsNum() {
		mainTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				stringCount.setText(String.valueOf(rowCount(newValue)));
			}
		});

	}

	private int rowCount(String s) {
		int rowCount = 1;
		int fromPosition = 0;
		int nextPosition = 0;
		while (s.indexOf('\n', fromPosition) != -1) {
			rowCount++;
			log.info("string num :" + rowCount);
			nextPosition = s.indexOf('\n', fromPosition);
			fromPosition = nextPosition + 1;
		}
		return rowCount;
	}

}
