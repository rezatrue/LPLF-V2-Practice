package scrapper;

public class TestMain {

	public static void main(String[] args) {
		
	FireFoxOperator fireFoxOperator = new FireFoxOperator();
	
	fireFoxOperator.browserLauncher();
	fireFoxOperator.openUrl("https://www.linkedin.com/");
	
	// need to add value for tasting
	String url = "";
	String user = "";
	String pass = "";
	
	
	fireFoxOperator.linkedinLogin(user, pass);
	
	fireFoxOperator.getPublicLink(url);

	url = "https://www.linkedin.com/sales/profile/AAEAAAW_u9IByafoGXoIWgXm8MFMVR7VkGw85Dg,t91r,OUT_OF_NETWORK?moduleKey=peopleSearchResults";
	fireFoxOperator.getPublicLink(url);
	
	url = "https://www.linkedin.com/sales/profile/AAEAAACmIfABkmdQA3YUJ_Yv49ewNqIQalwrrVw,X99q,NAME_SEARCH?moduleKey=peopleSearchResults";
	fireFoxOperator.getPublicLink(url);
	
	url = "https://www.linkedin.com/sales/profile/AAEAAAAAEWkBrZq-RBr6ICmXyduM1X6cwLJ-dcs,mKA8,NAME_SEARCH?moduleKey=peopleSearchResults";
	fireFoxOperator.getPublicLink(url);
	
	url = "https://www.linkedin.com/sales/profile/AAEAAAAGdhkBgWM7US1GVi_irgkPHGy5b-iATa8,vbf3,NAME_SEARCH?moduleKey=peopleSearchResults";
	fireFoxOperator.getPublicLink(url);
	
	url = "https://www.linkedin.com/sales/profile/AAEAAA4tpTQBWIvYvv26yIhub9EDOACwOQGbIfg,JxDM,NAME_SEARCH?moduleKey=peopleSearchResults";
	fireFoxOperator.getPublicLink(url);
	
	
	url = "https://www.linkedin.com/sales/profile/AAEAAABizH8BaEEBMKZjEHharp4JiQ2Ch0sxaKo,Z2Xe,NAME_SEARCH?moduleKey=peopleSearchResults";
	fireFoxOperator.getPublicLink(url);
	
	
	}

}
