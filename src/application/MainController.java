package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
	
	@FXML
	private Button btnLaunch, btnLogin, btnSettings, btnBrowse, btnRun; 
	

	// constructor is called before initialize() method
	public MainController() {
		System.out.println("Constructor");

	}
	
	@FXML
	private void initialize() {
		System.out.println("Initialize");
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
