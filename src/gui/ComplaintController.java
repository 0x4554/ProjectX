package gui;
/**
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 * the class handles the creation of new complaints in the system
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.Client;
import entities.ComplaintEntity;
import entities.ComplaintEntity.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.MessageToSend;

public class ComplaintController implements Initializable{
	
	@FXML
	private TextField ordNumTxtFld;
	
    @FXML
    private TextArea cmpDtsTxtArea;

    @FXML
    private Button upldBtn;

    @FXML
    private Button sndBtn;

    @FXML
    private Button cnclBtn;

    @FXML
    private TextField picPathTxtFld;
	
	
//	private CustomerMenuController cstmc;
	

	public ComplaintController() {
		// TODO Auto-generated constructor stub
	}
	
//	public void setConnectionData(CustomerMenuController cmc) {
//		this.cstmc=cmc;
//	}
	
	/**
	 * This method collects the complaint data and sends it to the server
	 * @param event	pressed send
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void getComplaintData(ActionEvent event) throws IOException, InterruptedException{					//////////////////////////
		String complaintDetails="";
		String pctr;
		String details;
		
		
		if(!ordNumTxtFld.getText().isEmpty())
			if(!cmpDtsTxtArea.getText().isEmpty()) {					//if complaint inserted			
				
					ComplaintEntity cmplnt=new ComplaintEntity(Integer.parseInt(ordNumTxtFld.getText()), cmpDtsTxtArea.getText(), Status.processing);
					if(!picPathTxtFld.getText().isEmpty()) {
						cmplnt.setFile(picPathTxtFld.getText());
					}
					
//					Client c=this.cstmc.getClient();
					MessageToSend toServer = new MessageToSend(cmplnt,"complaint");
					Client.getClientConnection().setConfirmationFromServer();
					Client.getClientConnection().setDataFromUI(toServer);									//, "complaint!");
					Client.getClientConnection().accept();
					
					while(!Client.getClientConnection().getConfirmationFromServer())
						Thread.sleep(100);
					Client.getClientConnection().setConfirmationFromServer();
					MessageToSend fromServer=(MessageToSend)Client.getClientConnection().getMessageFromServer();
					String reply=(String)fromServer.getMessage();
					if(reply.equals("failed"))				
						GeneralMessageController.showMessage("Order does not exist");
			else
						if(reply.equals("Success"))
						{
							cancelComplaint(event);
							GeneralMessageController.showMessage("Your complaint has been received received.\nWe will contact you soon.");
						}
						else if (reply.equals("Complaint was already filed"))
						{
							cancelComplaint(event);
							GeneralMessageController.showMessage("Complaint was already filed for this order.");
							
						}
					
			}
		else {
			GeneralMessageController.showMessage("Please fill in your complaint");		//if nothing was inserted show general message
		}
	}
	
	/**
	 * this method handles the complaint cancellation, and sends to the previous screen
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void cancelComplaint(ActionEvent event) throws IOException {
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/CustomerOrderDetailsBoundary.fxml").openStream());
		CustomerOrderDetailsController cocdc = loader.getController();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(pRoot);
		primaryStage.setTitle("Your Orders");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	/**
	 * this method uploads a picture of complaint
	 * 
	 * @param path - client side file location in file-system
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void uploadPhoto(String path) throws IOException, InterruptedException {
		
//		Client c = this.cstmc.getClient();
		MessageToSend m = new MessageToSend(path, "downloadFile");
		Client.getClientConnection().setDataFromUI(m);
		Client.getClientConnection().accept();
		
	//	c.uploadFileToServer(5556, path);
	//	c.setConfirmationFromServer();
	}
	
	
	/**
	 * This method searches for a file to add
	 * @param event	pressed add file
	 * @throws IOException
	 */
	public void searchForPhoto(ActionEvent event) throws IOException{
		
		Stage secondaryStage=new Stage();
		Node node = (Node) event.getSource();
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Choose File");
	    try {
	    File f=chooser.showOpenDialog(secondaryStage);
	    String filepath = f.getAbsolutePath();
	    picPathTxtFld.setText(filepath);
	    }
	    catch(Exception e) {
	    return;
	    }
	}
	
	/**
	 * This method sets the orderID
	 * @param OrderID
	 */
	public void setOrderID(Integer OrderID)
	{
		this.ordNumTxtFld.setText(OrderID.toString());
		this.ordNumTxtFld.setEditable(false);
	}
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
}
