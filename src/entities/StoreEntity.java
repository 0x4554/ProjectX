package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Store entity
 * StoreEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class StoreEntity extends Object implements Serializable{
	private int branchID;
	private String BranchName;
	private int storeManagerWorkerID;
	private ArrayList<Integer> storeWorkers;	//all the workers of the store by worker ID
	private Map<Integer,Double> storeDiscoutsSales; 	//holds all the discounts of the store sale
	
	/**
	 * 
	 * Constructor for the StoreEntity.java class
	 * @param branchID	the branch id
	 * @param storeManager	the store manager
	 * @param workers	the list of store workers
	 */
	public StoreEntity(int branchID,String branchName,int storeManager)
	{
		this.branchID = branchID;
		this.BranchName = branchName;
		this.storeManagerWorkerID=storeManager;
		this.storeWorkers= new ArrayList<Integer>();
		this.storeDiscoutsSales = new HashMap<Integer,Double>();
	}
	/**
	 * This method adds a worker to the store workers list
	 * @param workerID	the new workers id
	 */
	public void addStoreWorker(Integer workerID)
	{
		this.storeWorkers.add(workerID);
	}
	
	/**
	 * This method deletes a worker from the workers list
	 * @param workerID
	 */
	public void deleteWorker(Integer workerID)
	{
		this.storeWorkers.remove(workerID);
	}

	/**
	 * Getter for the branchName
	 * @return the branchName
	 */
	public String getBranchName() {
		return BranchName;
	}
	/**
	 * Setter for the branchName
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		BranchName = branchName;
	}
	/**
	 * Getter for the branchID
	 * @return the branchID
	 */
	public int getBranchID() {
		return branchID;
	}

	/**
	 * Setter for the branchID
	 * @param branchID the branchID to set
	 */
	public void setBranchID(int branchID) {
		this.branchID = branchID;
	}

	/**
	 * Getter for the storeManagerWorkerID
	 * @return the storeManagerWorkerID
	 */
	public int getStoreManagerWorkerID() {
		return storeManagerWorkerID;
	}

	/**
	 * Setter for the storeManagerWorkerID
	 * @param storeManagerWorkerID the storeManagerWorkerID to set
	 */
	public void setStoreManagerWorkerID(int storeManagerWorkerID) {
		this.storeManagerWorkerID = storeManagerWorkerID;
	}

	/**
	 * Getter for the storeWorkers
	 * @return the storeWorkers
	 */
	public ArrayList<Integer> getStoreWorkers() {
		return storeWorkers;
	}

	/**
	 * Setter for the storeWorkers
	 * @param storeWorkers the storeWorkers to set
	 */
	public void setStoreWorkers(ArrayList<Integer> storeWorkers) {
		this.storeWorkers = storeWorkers;
	}

	/**
	 * Getter for the storeDiscoutsSales
	 * @return the storeDiscoutsSales
	 */
	public Map<Integer, Double> getStoreDiscoutsSales() {
		return storeDiscoutsSales;
	}

	/**
	 * Setter for the storeDiscoutsSales
	 * @param storeDiscoutsSales the storeDiscoutsSales to set
	 */
	public void setStoreDiscoutsSales(Map<Integer, Double> storeDiscoutsSales) {
		this.storeDiscoutsSales = storeDiscoutsSales;
	}

}
