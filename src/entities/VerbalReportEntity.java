package entities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;

import logic.FilesConverter;

/**
 * class for Verbal Report entity
 * 
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 */
public class VerbalReportEntity implements Serializable{

	private byte[] file;
	private Timestamp date;
	
	
	
	/**
	 * Necessary constructor
	 * 
	 *constructor for the VerbalReportEntity.java class
	 */
	public VerbalReportEntity() {
		
	}

	
	
	
	/**
	 *Getter for the date
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}




	/**
	 *Setter for the date
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}




	/**
	 * getter for the wanted file
	 * 
	 * @return
	 */
	public byte[] getFile() {
		return file;
	}
	
	/**
	 * setter for the the wanted file
	 * 
	 * @param file
	 * @throws IOException 
	 */
	public void setFile(String filePath) throws IOException {
		File f = new File (filePath);
		this.file = FilesConverter.convertFileToByteArray(f);
	}
		
}
