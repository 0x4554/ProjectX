package entities;

import java.util.ArrayList;

/**
 * This class represents the catalog entity
 * CatalogEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CatalogEntity {
	private ArrayList<ProductEntity> catalogProducts;
	/**
	 * This method adds a new product to the catalog
	 * @param newProduct	the new product to add
	 */
	public void addProductToCatalog(ProductEntity newProduct)
	{
		this.catalogProducts.add(newProduct);
	}
	/**
	 * This methods removes a product from the catalog
	 * @param product	the prodcut to delete
	 */
	public void deleteProductFromCatalog(ProductEntity product)
	{
		this.catalogProducts.remove(product);
	}
	/**
	 * Getter for the catalogProducts
	 * @return the catalogProducts
	 */
	public ArrayList<ProductEntity> getCatalogProducts() {
		return catalogProducts;
	}

	/**
	 * Setter for the catalogProducts
	 * @param catalogProducts the catalogProducts to set
	 */
	public void setCatalogProducts(ArrayList<ProductEntity> catalogProducts) {
		this.catalogProducts = catalogProducts;
	}
	

}
