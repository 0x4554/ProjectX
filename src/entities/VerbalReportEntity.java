package entities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

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
	
	
	/**
	 * Necessary constructor
	 * 
	 *constructor for the VerbalReportEntity.java class
	 */
	public VerbalReportEntity() {
		
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
