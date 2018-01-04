package gui;
/**
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComplaintController implements Initializable{
	

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
	
	
	private CustomerMenuController cstmc;
	

	public ComplaintController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setConnectionData(CustomerMenuController cmc) {
		this.cstmc=cmc;
	}
	
	
	public void getComplaintData(ActionEvent event) throws IOException{					//////////////////////////
		String pctr;
		String details;
		if(!cmpDtsTxtArea.getText().isEmpty()) {					//if complaint inserted
				details=cmpDtsTxtArea.getText();					
		details=details.replaceAll(" ", "~");						//for handling the message later
		if(!picPathTxtFld.getText().isEmpty())						//if path to picture uploaded for sending it as avidence
				pctr=picPathTxtFld.getText();						
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cstmc.showCustomerMenu();								//back to main menu
		GeneralMessageController.showMessage("Dear customer, we got your complaint\nand we are doing everything we can\nto make it up to you");			//message to present when complaint succeeded
		return;
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
		this.cstmc.showCustomerMenu();
		return;
	}
	
	
	public void bckToMainMenu(ActionEvent event)throws IOException{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cstmc.showCustomerMenu();									//open previous menu
		return;
	}
	

	public void searchForPhoto() throws IOException{
/*		Stage secondaryStage=new Stage();
		FileChooser fileChooser=new FileChooser();
		  fileChooser.setTitle("View Pictures");
	        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))
	        );
	        
	        List<File> list = fileChooser.showOpenMultipleDialog(secondaryStage);
                if (list != null) {
                    for (File file : list) {
                    	 try {
                    		 desktop.open(file);
                         } catch (IOException ex) {
                             Logger.getLogger(
                                 FileChooserSample.class.getName()).log(
                                     Level.SEVERE, null, ex
                                 );
                         }
                    }
                        
                }
*/	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
}
