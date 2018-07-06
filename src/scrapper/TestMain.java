package scrapper;

public class TestMain {

	public static void main(String[] args) {
		
	FireFoxOperator fireFoxOperator = new FireFoxOperator();
	
	fireFoxOperator.browserLauncher();
	fireFoxOperator.openUrl("https://www.linkedin.com/");
	
	fireFoxOperator.linkedinLogin("", "");
	
	fireFoxOperator.getPublicLink("");


	}

}
