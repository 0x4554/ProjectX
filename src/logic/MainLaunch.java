package logic;

import client.Client;
import gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * MainLaunch.java 
 * 
 * The MainLaunch class extends Application, the main method calls the launch to start the application of our project
 * The start method will load the login page which is the first window in the client's application
 * 
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yaakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class MainLaunch extends Application {

	private Stage primaryStage;
	private LoginController login;
	/**
	 * The main method for the application.
	 * Calls the launch() to start the application
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * This method loads the first window in the application, the login window
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		this.primaryStage = primaryStage;	//get the primary Stage
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginGUI.fxml").openStream());	//load the login window(the login fxml file is in a different package /gui/)
		this.login = loader.getController();
		
		Scene scene = new Scene(root);

		this.primaryStage.setTitle("Login");	/**set the title**/
		this.primaryStage.setScene(scene);
		this.primaryStage.show();

	}
	
	/**
	 * This method uses to call another method (in the loginController) to close connection if user is logged in to the server
	 */
	@Override
	public void stop() throws Exception
	{
		this.login.signalAppClose();	
	}
}