package application;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
		csvFileHandeler = new CsvFileHandeler();
		fireFoxOperator = new FireFoxOperator();
		
		btnLogin.setDisable(true);
		btnBrowse.setDisable(true);
		btnRun.setDisable(true);
		
		// test settings
		tfLinkedinId.setText("rezatrue@yahoo.com");
		pfPassword.setText("1Canada");
		
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
		
	}
	
	
	@FXML
	private void launchBtnAction(ActionEvent event) {
		System.out.println("Launch Button");
		if(fireFoxOperator.browserLauncher()) {
			btnLogin.setDisable(false);
			
		}
		
	}
	
	@FXML
	private void loginBtnAction(ActionEvent event) {
		System.out.println("Login Button");
		String user = tfLinkedinId.getText();
		String password = pfPassword.getText();
		if(!user.isEmpty() && !password.isEmpty())
			if(fireFoxOperator.linkedinLogin(user, password))
				btnBrowse.setDisable(false);
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
        	tfSelectedFilePath.setText(file.getAbsolutePath());
        	String filepath = tfSelectedFilePath.getText().toString();
    		if(filepath.endsWith(".csv")) {
    			list = csvFileHandeler.read(filepath);
    			if(list.size() > 0) btnRun.setDisable(false);
    		}
        }
        
	}
	
	@FXML
	private void runBtnAction(ActionEvent event) {
		System.out.println("Run Button");
		// need to test 
		int limits = Integer.parseInt(tfLimits.getText());
		int index = 0; // number of loop iteration / list serial number
		int count = 0; // counts number of converted links
		while (limits != 0) {
			
			Info info = list.get(index);
			String link = info.getLink();
			if(link.contains("linkedin.com/sales/")) {
				link = fireFoxOperator.getPublicLink(link);
				info.setLink(link);
				list.set(index, info);
				count++;
			}
			
			index++;
			System.out.println(count + " Links converted");
			if(index + 1 == list.size()) break;
			limits -= count;
		}
		
	}

	@FXML
	private void printListBtnAction(ActionEvent event) {
		System.out.println("Print Button");
		csvFileHandeler.write(list, list.size()+"");
	}
	
	
}
