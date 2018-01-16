package logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * this method helps to convert files into different types
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 */
public class FilesConverter {
	
  	/**
  	 * this method converts InputStream object into array of bytes(byte[])
  	 * 
  	 * 
  	 * @param inStrm - InputStream to convert
  	 * @return  - array of bytes
  	 * @throws IOException 
  	 */
  	public static byte[] convertInputStreamToByteArray(InputStream inStrm) throws IOException {
  		
  		byte [] retByteArray=null;
  		byte[] buff = new byte[4096];
  		int bytesRead = 0;

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        
        while((bytesRead = inStrm.read(buff)) != -1) {							//read the entire stream
            bao.write(buff, 0, bytesRead);
         }

         retByteArray = bao.toByteArray();
  
  		return retByteArray;
  	}
  	
  	
  	
 	/**
  	 * converts array of bytes (byte[]) into a InputStream in order to enter it to the database as blob
  	 * 
  	 * 
  	 * @param byteArray - the byte[] to convert
  	 */
  	public static InputStream convertByteArrayToInputStream(byte [] byteArray) {
  		
  		InputStream retInputStream = new ByteArrayInputStream(byteArray);
  		
  		return retInputStream;
  	}
  	
  	
}
  	
  	

