package entities;

/**
 * Entity class for the chain worker
 * ChainWorkerEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class ChainWorkerEntity extends UserEntity{

	private int workerID;

	/**
	 * 
	 * Constructor for the ChainWorkerEntity.java class
	 */
	public ChainWorkerEntity()
	{
		super("CW");//type of chain worker
	}
	
	/**
	 * 
	 * Constructor for the ChainWorkerEntity.java class
	 * @param workerID
	 */
	public ChainWorkerEntity(int workerID){
		super("CW");//type of chain worker
		this.workerID=workerID;
		// TODO Auto-generated constructor stub
	}
	/**
	 * getter for the worker id
	 * @return
	 */
	public int getWorkerID() {
		return workerID;
	}
	
	/**
	 * Setter for the worker id
	 * @param workerID
	 */
	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
}
