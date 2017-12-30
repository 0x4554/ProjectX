package logic;

import GUI.LoginController;
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
 * @author Lana Kricheli
 * @author Katya Yaakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class MainLaunch extends Application {

	private Stage primaryStage;
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
		Parent root = loader.load(getClass().getResource("/GUI/LoginGUI.fxml").openStream());	//load the login window
//		LoginBoundary login = loader.getController();

		Scene scene = new Scene(root);

		this.primaryStage.setTitle("Login");	/**set the title**/
		this.primaryStage.setScene(scene);
		this.primaryStage.show();

	}
}