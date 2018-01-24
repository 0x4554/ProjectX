package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LaunchScreenController implements Initializable{

	  @FXML
	  private ImageView zerliLogo;

	  @FXML
	  private ImageView projectXLogo;
	
	public void setLogos() {
		Image zlimg = new Image("/logos/logozl.png");
		Image pximg = new Image("/logos/projectxlogo.png");
		zerliLogo.setImage(zlimg);
		projectXLogo.setImage(pximg);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Image zlimg = new Image("/logos/logozl.png");
		Image pximg = new Image("/logos/projectxlogo.png");
		zerliLogo.setImage(zlimg);
		projectXLogo.setImage(pximg);
	}

}
