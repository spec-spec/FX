package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
	private Label infoLabel;
	@FXML
	private TextField openFileName;
	@FXML
	private TextField saveFileName;
	private String fileName = "C:\\Temp.txt";
	private File f = new File(fileName);

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void openFile(ActionEvent event) {
		mainTextArea.clear();
		openFileFromDisk(fileName);
	}

	public void saveTextToFile(ActionEvent event) {
		log.info("save function");
		checkAndCreateFile(f);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append(mainTextArea.getText());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openArc(ActionEvent event) {
		mainTextArea.clear();
		byte[] buffer = new byte[2048];
		log.info("open Arc function");
		if (getFileNameToOpen() != null) {
			try (ZipInputStream zin = new ZipInputStream(new FileInputStream(
					getFileNameToOpen()))) {
				ZipEntry entry;
				while ((entry = zin.getNextEntry()) != null) {
					int len = 0;
					while ((len = zin.read(buffer)) > 0) {
						String decod = new String(buffer, "utf-8");// oracle example
						mainTextArea.appendText(decod);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			infoLabel.setText("error openning file");
	}

	public void saveArc(ActionEvent event) {
		log.info("save ARC function");
		if (getFileNameToSave() != null) {
			infoLabel.setText(" file name OK");
			File tmp = new File(getFileNameToSave());
			checkAndCreateFile(tmp);
			try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tmp))) {
				ZipEntry entry1 = new ZipEntry("first file to zip");
				zout.putNextEntry(entry1);
				byte[] buffer = mainTextArea.getText().getBytes();
				zout.write(buffer);
				zout.closeEntry();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			infoLabel.setText("wrong file name");
	}

	private String getFileNameToOpen() {
		if ((openFileName.getText().indexOf(".zip")) != -1) {
			return openFileName.getText();
		} else {
			log.info("wrong open file name");
			return null;
		}
	}

	private String getFileNameToSave() {
		if ((saveFileName.getText().indexOf(".zip")) != -1) {
			return saveFileName.getText();
		} else {
			log.info("wrong save file name");
			return null;
		}
	}

	private void openFileFromDisk(String name) {
		File fo = new File(name);
		try (BufferedReader br = new BufferedReader(new FileReader(fo))) {
			String s;
			while ((s = br.readLine()) != null) {
				mainTextArea.appendText(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkAndCreateFile(File fileToCheck) {
		if (!fileToCheck.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
