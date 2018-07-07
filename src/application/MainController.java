package application;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import db.DBHandler;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import scrapper.CsvFileHandeler;
import scrapper.FireFoxOperator;
import scrapper.Info;

public class MainController implements Initializable{
	
	@FXML
	private Button btnLaunch, btnLogin, btnSettings, btnBrowse, btnRun, btnPrintList; 
	@FXML
	private TextField tfSelectedFilePath, tfLinkedinId, tfLimits, tfMessageBox;
	@FXML
	private PasswordField pfPassword;
	
	@FXML
	private ImageView logoView;
	private DBHandler dBHandler;
	private Preferences prefs;
	
	CsvFileHandeler csvFileHandeler = null;
	LinkedList<Info> list = null;
	FireFoxOperator fireFoxOperator = null;
	
	// constructor is called before initialize() method
	public MainController() {
		System.out.println("Constructor");
		
		
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("Initialize");
		
		prefs = Preferences.userRoot().node("db_lplf");
		csvFileHandeler = new CsvFileHandeler();
		fireFoxOperator = new FireFoxOperator();
		btnLaunch.setDisable(true);
		btnLogin.setDisable(true);
		btnBrowse.setDisable(true);
		btnRun.setDisable(true); 
		
		tfLinkedinId.setText(prefs.get("linkedinUser", ""));
		pfPassword.setText(prefs.get("linkedinPassword", ""));
		
		File file = new File("image/yin-yang.jpg");
        Image image = new Image(file.toURI().toString());
        logoView.setImage(image);
        
        //stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        tfLimits.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
    	        String newValue) {
    	        if (newValue.matches("\\d*")) {
    	            int value = Integer.parseInt(newValue);
    	        } else {
    	        	tfLimits.setText(oldValue);
    	        }
    	    }
    	});
        
        loginDialoag();
	}
	
	
	@FXML
	private void launchBtnAction(ActionEvent event) {
		System.out.println("Launch Button");
		tfMessageBox.setText("Lunching Browser, Please wait......");
		String msg = fireFoxOperator.browserLauncher();
		tfMessageBox.setText(msg);
		if(!msg.contains("Error")) {
			btnLogin.setDisable(false);
			btnLaunch.setDisable(true);
		}
		
	}
	
	@FXML
	private void loginBtnAction(ActionEvent event) {
		System.out.println("Login Button");
		
		if(btnLogin.getText().contains("Login")) {
			btnLogin.setText("Sign Out");
			String user = tfLinkedinId.getText();
			String password = pfPassword.getText();
			if(!user.isEmpty() && !password.isEmpty()) {
				tfMessageBox.setText("Attempting to login");
				if(fireFoxOperator.linkedinLogin(user, password)) {
					btnBrowse.setDisable(false);
					btnLogin.setText("Sign Out");
					tfMessageBox.setText("Successfully logged in");
				}else
					tfMessageBox.setText("Unable to login, Please try manually");
			}else
				tfMessageBox.setText("Please Enter your Linkedin ID & Password");
		}
		
		else if(btnLogin.getText().contains("Sign Out")) {
			if(fireFoxOperator.signOut()) {
				btnLogin.setText("Login");
				tfMessageBox.setText("Sign Out Successfully");
			}
		}
	}
	
	@FXML
	private void settingsBtnAction(ActionEvent event) {
		System.out.println("Settings Button");
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/application/Settings.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(new Scene(parent));
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void browseBtnAction(ActionEvent event) {
		System.out.println("Browse Button");
		//stackoverflow.com/questions/25491732/how-do-i-open-the-javafx-filechooser-from-a-controller-class/25491787
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("CSV Files", "*.csv")
			);
		
        File file = fileChooser.showOpenDialog(new Stage());
        
        if(file != null) {
        	String filepath = file.getAbsolutePath();
    		if(filepath.endsWith(".csv")) {
    			list = csvFileHandeler.read(filepath);
    			if(list.size() == 0) {
    				btnRun.setDisable(true);
    				filepath = "";
    				tfMessageBox.setText("File is not in proper format");
    			}else if(list.size() > 0) {
    				btnRun.setDisable(false);
    				tfMessageBox.setText("List size : "+ list.size());
    			}
    		}
        	tfSelectedFilePath.setText(filepath);
        }
        
	}
	
	@FXML
	private void runBtnAction(ActionEvent event) {
		System.out.println("Run Button");
		
		
		// checking limits, how many links need to convert
		int limits = 0;
		if(!tfLimits.getText().isEmpty()) {
			limits = Integer.parseInt(tfLimits.getText());
			}
		System.out.println("limits : "+ limits);
		if(limits == 0) {
			tfMessageBox.setText("Please enter a number of links need to be converted");
			return;
		}
		
		// Checking if there is any sales link in the list 
		boolean noValidLink = true;
		if (list.size() > 0) {
			Iterator<Info> it = list.iterator();
			while (it.hasNext()) {
				String link = it.next().getLink();
				// for testing use linkedin sales nav links presence  
				if (link.contains("linkedin.com/sales")) {
					noValidLink = false;
					break;
				}
			}
			if (noValidLink) {
				tfMessageBox.setText("List doesn't contain any sales Nav link");
				return;
			}
		}

		// setting Button text
		if(btnRun.getText().equals("Run"))
			btnRun.setText("Pause");
		else if(btnRun.getText().equals("Pause"))
				btnRun.setText("Run");
		
		
		// need to test 
				/*
					
						int index = 0; // number of loop iteration / list serial number
						int count = 0; // counts number of converted links
						Info info = null;
						String link = "";
						String newlink = "";
						while (limits != 0) {

							info = list.get(index);
							link = info.getLink();
							if (link.contains("linkedin.com/sales")) {
								newlink = fireFoxOperator.getPublicLink(link);
								if(link!=newlink) {
									passMessage(count + " Links converted, process continues....");
									System.out.println(count + " Links converted");
									info.setLink(newlink);
									list.set(index, info);
									count++;
								}
								
							}

							index++;
							if (index + 1 == list.size() || index + 1 == count) {
								passMessage("Conversion Completed. Total : "+ count + " links converted.");
								return;
							}
								
						} 
					*/
				
				
	}
	
	private void passMessage(String msg) {
		new Runnable() {
			
			@Override
			public void run() {
				tfMessageBox.setText(msg);				
			}
		};
		
	}
	
	
	@FXML
	private void printListBtnAction(ActionEvent event) {
		System.out.println("Print Button");
		int size = (list != null) ? list.size() : 0 ;
		csvFileHandeler.write(list, size);
	}
	
	// source http://code.makery.ch/blog/javafx-dialogs-official/
	// just copy pest

	private void loginDialoag() {
		
		dBHandler = new DBHandler();

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login");
		dialog.setHeaderText("Please Enter your usernmae & password");

		// Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("image/login.png").toString()));
		

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		username.setText(prefs.get("user", ""));
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		password.setText(prefs.get("password", ""));

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			//System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
			//String msg = dBHandler.userAuth(usernamePassword.getKey(), usernamePassword.getValue());
			String msg = "Welcome Developer" ; // test only 
			if(msg.contains("Welcome"))
				btnLaunch.setDisable(false);
			tfMessageBox.setText(msg);

		});

	}
	
}
