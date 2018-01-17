package entities;

public class ChainWorkerEntity extends UserEntity{

	private int workerID;

	//Default constructor
	public ChainWorkerEntity()
	{
		super("CW");//type of chain worker
	}
	//Constructor 
	public ChainWorkerEntity(int workerID){
		super("CW");//type of chain worker
		this.workerID=workerID;
		// TODO Auto-generated constructor stub
	}
	public int getWorkerID() {
		return workerID;
	}
	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
}
