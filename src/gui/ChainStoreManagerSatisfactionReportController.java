package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.OrderEntity;
import entities.ProductEntity;
import entities.SurveyEntity;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ChainStoreManagerSatisfactionReportController implements Initializable {

	 @FXML
	    private Button clseBtn;
	    @FXML
	    private BarChart<String, String> chart;
	    @FXML
	    private ListView<String> QLstVw;

	   private ObservableList<String> list;
	    /**
	     * This method shows the table of orders
	     * @param orders
	     */
	    public void showOrders(SurveyEntity survey) {
	    	
	    	int a1=0,a2=0,a3=0,a4,a5,a6,a7,a8,a9,a10;
	    	this.chart.setTitle("Survey results");
	    	ArrayList<XYChart.Series<String, String>> dataSeries = new ArrayList<XYChart.Series<String, String>>(10);
	       // XYChart.Series<String,String>[] dataSeries = new XYChart<String, String>();
//	        for(XYChart.Series d : dataSeries)
//	        	d= new XYChart.Series();
	    	for(int i=0 ; i<10;i++)
	    	{
	    		dataSeries.add(new XYChart.Series()) ;
	    	}
	    	
	    	
	        for(int i=0;i<10;i++)
	        	dataSeries.get(i).setName(Integer.toString(i+1));

	      
	        
//	        for(OrderEntity order : orders)
//	        {
//	        	for(ProductEntity product : order.getProductsInOrder())
//	        	{
//	        		if(product.getProductType().equals("flower"))
//	        			flower++;
//	        		else if (product.getProductType().equals("bridal"))
//	        			bridal++;
//	        		else if (product.getProductType().equals("birthDay"))
//	        			birthDay++;
//	        	}
//	        	
//	        }
	        for(int i=0;i<10;i++)
	        {
	        dataSeries.get(i).getData().add(new XYChart.Data("Q1", 4));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q2"  , 3));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q3"  , 1));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q4"  , 0));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q5"  , 8));
	        dataSeries.get(i).getData().add(new XYChart.Data("Q6"  , 15));
	        chart.getData().add(dataSeries.get(i));
	        }
//	        chart.getData().add(dataSeries[i]);
	        
	        ArrayList<String> questions = new ArrayList<String>();
	        ///get the questions/////////////////////////////
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
