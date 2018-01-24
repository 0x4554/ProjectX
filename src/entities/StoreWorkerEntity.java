package entities;
/**
 * This class represents a user of a Store worker type
 * StoreWorkerEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class StoreWorkerEntity extends UserEntity {
	private int branchID;
	private int workerID;
	/**
	 * Constructor for the StoreWorkerEntity.java class
	 * invokes the UserEntity constructor for the store worker
	 * @param branchID the branch ID
	 * @param workerID the worker ID
	 */
	public StoreWorkerEntity(int workerID,int branchID) {
		super("storeWorker");
		this.branchID=branchID;
		this.workerID=workerID;
	}
	/**
	 * 
	 * Constructor for the StoreManagerEntity class which extends StoreWorkerEntity class
	 * @param type	storeManager
	 */
	public StoreWorkerEntity(String type)
	{
		super(type);
	}
	/**
	 * Getter for the branch
	 * @return the branch
	 */
	public int getBranchID() {
		return branchID;
	}
	/**
	 * Setter for the branch ID
	 * @param branchID	the new branch id
	 */
	public void setBranch(int branchID) {
		this.branchID = branchID;
	}
	/**
	 * Getter for the worker's id
	 * @return	the worker's id
	 */
	public int getWorkerid() {
		return workerID;
	}
	/**
	 * Setter for the worker's id
	 * @param workerid	the new worker's id
	 */
	public void setWorkerid(int workerid) {
		this.workerID = workerid;
	}
	

}
