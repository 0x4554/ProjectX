package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class shows the complaint image to the screen
 * 
 * CustomerServiceWorkerShowComplaintImageBoundary.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerServiceWorkerShowComplaintImageController implements Initializable {

	@FXML
	private ImageView imgVW;

	@FXML
	private Button clsBtn;

	/**
	 * This method loads the image to the screen
	 * 
	 * @param image
	 */
	public void showImage(Image image) {
		this.imgVW.setImage(image);
	}

	/**
	 * This method closes the window
	 * 
	 * @param event
	 *            pressed close
	 */
	public void closeWindow(ActionEvent event) {
		
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
