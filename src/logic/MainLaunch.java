package logic;


import java.io.IOException;

import gui.LaunchScreenController;
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

	public Stage primaryStage;
	public LoginController login;
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


		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());	//load the login window(the login fxml file is in a different package /gui/)

		this.login = loader.getController();
		
		LaunchScreenController lsc = loader.getController();
		lsc.setLogos();
		FadeTransition ft = new FadeTransition(Duration.millis(3500), root);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();				
		Scene scene = new Scene(root);
		this.primaryStage.setScene(scene);
		this.primaryStage.show();	
		PauseTransition pause = new PauseTransition(Duration.seconds(4));
		pause.setOnFinished(event -> {
			Parent root2 = null;
			try {
				FXMLLoader loader2 = new FXMLLoader();
				root2 = loader2.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	//load the login window(the login fxml file is in a different package /gui/)
			 Scene secondScene = new Scene(root2);
			 secondScene.getStylesheets().add("/gui/LoginStyle.css");
		     this.primaryStage.setTitle("Login");
		     this.primaryStage.setScene(secondScene);
		     });
		pause.play();
	}

	}
	

	@Override
	public void stop() throws Exception
	{
		LoginController.signalAppClose();
	}
}