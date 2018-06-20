package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	@FXML
	private Button btnLaunch, btnLogin, btnSettings, btnBrowse, btnRun; 
	
	@FXML
	private ImageView logoView;

	// constructor is called before initialize() method
	public MainController() {
		System.out.println("Constructor");
		
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("Initialize");
		File file = new File("image/yin-yang.jpg");
        Image image = new Image(file.toURI().toString());
        logoView.setImage(image);
		
	}
	
	
	@FXML
	private void launchBtnAction(ActionEvent event) {
		System.out.println("Launch Button");
	}
	
	@FXML
	private void loginBtnAction(ActionEvent event) {
		System.out.println("Login Button");
	}
	
	@FXML
	private void settingsBtnAction(ActionEvent event) {
		System.out.println("Settings Button");
	}
	
	@FXML
	private void browseBtnAction(ActionEvent event) {
		System.out.println("Browse Button");
		
	}
	
	@FXML
	private void runBtnAction(ActionEvent event) {
		System.out.println("Run Button");
	}

	

	
	
}
