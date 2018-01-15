package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class SurveyController {
	
	@FXML
	private RadioButton q1r1;
	@FXML
	private RadioButton q1r2;
	@FXML
	private RadioButton q1r3;
	@FXML
	private RadioButton q1r4;
	@FXML
	private RadioButton q1r5;
	@FXML
	private RadioButton q2r1;
	@FXML
	private RadioButton q2r2;
	@FXML
	private RadioButton q2r3;
	@FXML
	private RadioButton q2r4;
	@FXML
	private RadioButton q2r5;
	@FXML
	private RadioButton q3r1;
	@FXML
	private RadioButton q3r2;
	@FXML
	private RadioButton q3r3;
	@FXML
	private RadioButton q3r4;
	@FXML
	private RadioButton q3r5;
	@FXML
	private RadioButton q4r1;
	@FXML
	private RadioButton q4r2;
	@FXML
	private RadioButton q4r3;
	@FXML
	private RadioButton q4r4;
	@FXML
	private RadioButton q4r5;
	@FXML
	private RadioButton q5r1;
	@FXML
	private RadioButton q5r2;
	@FXML
	private RadioButton q5r3;
	@FXML
	private RadioButton q5r4;
	@FXML
	private RadioButton q5r5;
	@FXML
	private RadioButton q6r1;
	@FXML
	private RadioButton q6r2;
	@FXML
	private RadioButton q6r3;
	@FXML
	private RadioButton q6r4;
	@FXML
	private RadioButton q6r5;
	
	@FXML
	private ToggleGroup q1;
	@FXML
	private ToggleGroup q2;
	@FXML
	private ToggleGroup q3;
	@FXML
	private ToggleGroup q4;
	@FXML
	private ToggleGroup q5;
	@FXML
	private ToggleGroup q6;
	
	@FXML
	private Button sbmtBtn;
	@FXML
	private Button bckBtn;
	
	
	
	public boolean validateAnswers() {
		if(!q1.getSelectedToggle().isSelected() || !q2.getSelectedToggle().isSelected() || !q3.getSelectedToggle().isSelected() || !q4.getSelectedToggle().isSelected() || !q5.getSelectedToggle().isSelected() || !q6.getSelectedToggle().isSelected())
			return false;
		
		return true;
	}


	public void getSurveyAnswers(ActionEvent event) throws IOException {
		if(!validateAnswers())
			GeneralMessageController.showMessage("Please fill in missing fields");
		
		
	}
	
	
	
	
}