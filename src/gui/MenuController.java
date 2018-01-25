package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
/**
 * This class is an abstract class for all the main menus of the users.
 * It is used for declaring a method  for loading the menu.
 * MenuController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public abstract class MenuController implements Initializable {

	/**
	 * An abstract method for loading each user's menu, to be Overridden.
	 * @throws IOException
	 */
	public abstract void showMenu()	throws IOException ;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
