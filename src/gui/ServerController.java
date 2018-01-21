package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import server.ServerMain;

public class ServerController implements Initializable {

	@FXML
    private TextArea cnslTxtArea;

    @FXML
    private Button tnOffBtn;
    public ServerMain server;

   /**
    * This method turns off the server and closes the window
    * @param event	pressed turn off server
    */
   public void closeServer(ActionEvent event) {
	   try
	{
//		server.stop();
		 Stage stage = (Stage) this.tnOffBtn.getScene().getWindow();
		    // do what you have to do
		    stage.close();
	} catch (Exception e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
   
   /**
    * This method prints messages to the server UI
    * @param msg	message to add
    */
   public void showMessageToUI(String msg)
   {
	   this.cnslTxtArea.insertText(cnslTxtArea.getText().length(), msg +"\n");
   }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
