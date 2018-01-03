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
	private int workerid;
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
		return workerid;
	}
	/**
	 * Setter for the worker's id
	 * @param workerid	the new worker's id
	 */
	public void setWorkerid(int workerid) {
		this.workerid = workerid;
	}
	

}
