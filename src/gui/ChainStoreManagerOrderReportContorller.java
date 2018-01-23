package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.OrderEntity;
import entities.ProductEntity;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * This class is the controller for the order report boundary
 * ChainStoreManagerOrderReportContorller.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class ChainStoreManagerOrderReportContorller implements Initializable {

	@FXML
    private TableView<String> typTblVw;

    @FXML
    private Button clsBtn;
    @FXML
    private BarChart<String, String> chart;

   private ObservableList<String> list;
   
    /**
     * This method shows the chart of orders oragnized by product type
     * @param orders
     */
    public void showOrders(ArrayList<OrderEntity> orders) {
    	
    	int flower=0,bridal=0,birthDay=0;
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Products ordered");
   
        for(OrderEntity order : orders)
        {
        	for(ProductEntity product : order.getProductsInOrder())
        	{
        		if(product.getProductType().equals("flower"))
        			flower++;
        		else if (product.getProductType().equals("bridal"))
        			bridal++;
        		else if (product.getProductType().equals("birthDay"))
        			birthDay++;
        	}
        	
        }
        dataSeries1.getData().add(new XYChart.Data("Flower", flower));
        dataSeries1.getData().add(new XYChart.Data("Bridal"  , bridal));
        dataSeries1.getData().add(new XYChart.Data("BirthDay"  , birthDay));
        
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
