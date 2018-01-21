package server;

import gui.GeneralMessageController;
import gui.LoginController;
import gui.StoreManagerMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.MessageToSend;
import server_gui.ServerController;

public class ServerMain extends Application {

	  final public static int DEFAULT_PORT = 5555;
	  private Stage primaryStage;
	  public static ServerController serverController;
	  private static int port =0;
	  public static ProjectServer sv;
	 /**
	   * This method is responsible for the creation of 
	   * the server instance (there is no UI in this phase).
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
		 public static void main(String[] args) 
	  {
			 try
			    {
				 port = Integer.parseInt(args[0]); //Get port from command line
			    }
			    catch(Throwable t)
			    {
			      port = DEFAULT_PORT; //Set port to 5555
			    }
				
			 launch(args);
	  
	  }
	    
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		  int port = 0; //Port to listen on
		    
		   
//		     this.sv = new ProjectServer(port);
		    
		    try {
		    	this.primaryStage = primaryStage;	//get the primary Stage
				FXMLLoader loader = new FXMLLoader();


				Parent root = loader.load(getClass().getResource("/server_gui/ServerBoundary.fxml").openStream());	//load the login window(the login fxml file is in a different package /gui/)

				this.serverController = loader.getController();
				this.serverController.server=this;
				
				Scene scene = new Scene(root);
	//			scene.getStylesheets().add("/gui/LoginStyle.css");

				this.primaryStage.setTitle("Zer-Li Server");	/**set the title**/
				this.primaryStage.setScene(scene);
				this.primaryStage.show();
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    this.sv = new ProjectServer(port);
		    
		    try 
		    {
		      sv.listen(); //Start listening for connections
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println("ERROR - Could not listen for clients!");
		    }
	}
	
	@Override
	public void stop() throws Exception
	{
//		MessageToSend msg = new MessageToSend("server closed", "");			//when server closed , send messages to all clients connected to the system
//		this.sv.sendToAllClients(msg);
		sv.close();
		
	
	}

}
