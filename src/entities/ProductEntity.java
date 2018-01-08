package entities;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductEntity implements Serializable{
	private String productID;
	private String productName;
	private String productType;
	private Double productPrice;
	private String productDescription;
	private String productDominantColor;
	private Image productImage;
	//private ImageView productImageView;
	
	public ProductEntity()
	{
		
	}
	/**
	 * Getter for the productImage
	 * @return the productImage
	 */
	public Image getProductImage() {
		return productImage;
	}

	/**
	 * Setter for the productImage
	 * @param productImage the productImage to set
	 */
	public void setProductImage(Image productImage) {
		this.productImage = productImage;
	}

	/**
	 * Constructor for the ProductEntity.java class
	 * @param productID	the product's id
	 * @param productName	the name
	 * @param productType	the type
	 * @param productPrice	the base price
	 * @param productDescription	description
	 * @param productDominantColor	dominant color
	 */
	public ProductEntity(String productID, String productName, String productType, Double productPrice,
			String productDescription, String productDominantColor,Image productImage) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.productType = productType;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.productDominantColor = productDominantColor;
		this.productImage = productImage;
	}
	
	/**
	 * Getter for the productID
	 * @return the productID
	 */
	public String getProductID() {
		return productID;
	}
	/**
	 * Setter for the productID
	 * @param productID the productID to set
	 */
	public void setProductID(String productID) {
		this.productID = productID;
	}
	/**
	 * Getter for the productName
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * Setter for the productName
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	///////////////////////////////////////
//	public ImageView getProductImage() {
//		return productImage;
//	}
//
//	public void setProductImage(ImageView productImage) {
//		this.productImage = productImage;
//	}
///////////////////////////////////////////////
	/**
	 * Getter for the productType
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * Setter for the productType
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * Getter for the productPrice
	 * @return the productPrice
	 */
	public Double getProductPrice() {
		return productPrice;
	}
	/**
	 * Setter for the productPrice
	 * @param productPrice the productPrice to set
	 */
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	/**
	 * Getter for the productDescription
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * Setter for the productDescription
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	/**
	 * Getter for the productDominantColor
	 * @return the productDominantColor
	 */
	public String getProductDominantColor() {
		return productDominantColor;
	}
	/**
	 * Setter for the productDominantColor
	 * @param productDominantColor the productDominantColor to set
	 */
	public void setProductDominantColor(String productDominantColor) {
		this.productDominantColor = productDominantColor;
	}

}
