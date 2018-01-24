package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.SurveyEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * This class is used for the satisfaction report
 * 
 * ChainStoreManagerSatisfactionReportController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class ChainStoreManagerSatisfactionReportController implements Initializable {

	 @FXML
	    private Button clseBtn;
	    @FXML
	    private BarChart<String, String> chart;
	    @FXML
	    private ListView<String> QLstVw;

	   private ObservableList<String> list;
	   
	    /**
	     * This method shows the chart of the survey result
	     * @param orders
	     */
	    public void showOrders(SurveyEntity survey) {
	    	this.chart.setTitle("Survey results");
	    	this.list = FXCollections.observableArrayList();
	    	ArrayList<XYChart.Series<String, String>> dataSeries = new ArrayList<XYChart.Series<String, String>>(10);
	    	for(int i=0 ; i<10;i++)
	    	{
	    		dataSeries.add(new XYChart.Series()) ;			//new char series
	    	}
	    	
	    	
	        for(int i=0;i<10;i++)
	        	dataSeries.get(i).setName("Ans. "+Integer.toString(i+1));			//name of each bar

	        for(int i=0;i<10;i++)
	        {
	        dataSeries.get(i).getData().add(new XYChart.Data("Q1", survey.getTotalRanks(1, i+1)));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q2"  , survey.getTotalRanks(2, i+1)));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q3"  , survey.getTotalRanks(3, i+1)));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q4"  , survey.getTotalRanks(4, i+1)));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q5"  , survey.getTotalRanks(5, i+1)));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q6"  , survey.getTotalRanks(6, i+1)));
	        chart.getData().add(dataSeries.get(i));
	        }
	        
	        ArrayList<String> questions = new ArrayList<String>();
	        for(int i=1;i<=6;i++)
	        	questions.add("Question No. "+i+" : "+survey.getQuestionText(i));
	        list.setAll(questions);
	        this.QLstVw.setItems(list);
	        
		}
	    
	    /**
	     * This method closes the window
	     * @param event pressed close
	     */
	   public void closeReport(ActionEvent event) {
		   ((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
	    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
