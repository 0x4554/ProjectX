package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.ComplaintEntity;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

/**
 * This class is the controller for the complaint report boundary
 * ChainStoreManagerComplaintReportController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class ChainStoreManagerComplaintReportController implements Initializable {

	  @FXML
	    private Button clsBtn;
	    @FXML
	    private BarChart<String, String> chart;

	   private ObservableList<String> list;
	   
	    /**
	     * This method shows the table of orders
	     * @param orders
	     */
	    public void showComplaints(String quarter,ArrayList<ComplaintEntity> complaints) {
	    	  String[] firstQuarter = {"January","February","March"};
	    	  String[] secondQuarter = {"April","May","June"};
	    	  String[] thirdQuarter = {"July","August","September"};
	    	  String[] forthQuarter = {"October","November","December"};
	    	  String[] askedQuarter = null;

	    	int firstMonth=0,secondMonth=0,thirdMonth=0;
	        XYChart.Series dataSeries1 = new XYChart.Series();
	        dataSeries1.setName("Complaints");
	        
	        if(quarter.equals("1"))
	  		  askedQuarter=firstQuarter;
	  	  else if (quarter.equals("2"))
	  		  askedQuarter=secondQuarter;
	  	  else if (quarter.equals("3"))
	  		  askedQuarter=thirdQuarter;
	  	  else if (quarter.equals("4"))
	  		  askedQuarter=forthQuarter;
	      
	        
	        for(ComplaintEntity complaint : complaints)
	        {
	        	if(complaint.getFiledOn().getMonth()%3 +1 == 1)
	        		firstMonth++;
	        	else if(complaint.getFiledOn().getMonth()%3 +1 == 2)
	        		secondMonth++;
	        	else 
	        		thirdMonth++;
	        	
	        }
	        dataSeries1.getData().add(new XYChart.Data(askedQuarter[0], firstMonth));
	        dataSeries1.getData().add(new XYChart.Data(askedQuarter[1]  , secondMonth));
	        dataSeries1.getData().add(new XYChart.Data(askedQuarter[2] , thirdMonth));
	        
	        chart.getData().add(dataSeries1);
	        
		}
	    
	    /**
	     * This method closes the window
	     * @param event pressed close
	     */
	   public void close(ActionEvent event) {
		   ((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
	    }
	   
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub

		}


}
