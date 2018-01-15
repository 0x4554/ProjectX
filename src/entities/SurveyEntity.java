package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the survey entity
 * SurveyEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class SurveyEntity implements Serializable{

	//private Map<String,ArrayList<Integer>> questionsAndAnswers;		//*The String (key) is the question and the ArrayList holds all of the answers to that question*//

	private String q1Txt;
	private String q2Txt;
	private String q3Txt;
	private String q4Txt;
	private String q5Txt;
	private String q6Txt;
	
	private int q1Rnk;
	private int q2Rnk;
	private int q3Rnk;
	private int q4Rnk;
	private int q5Rnk;
	private int q6Rnk;
	
	private String usernameAnswers;				//holds the user that took the survey
	
	
	/**
	 * the method sets the ansers for the current survey
	 * 
	 * @param q1 - q1 answer
	 * @param q2 - q2 answer
	 * @param q3 - q3 answer
	 * @param q4 - q4 answer
	 * @param q5 - q5 answer
	 * @param q6 - q6 answer
	 */
	public void setAnswers(int q1,int q2,int q3,int q4,int q5,int q6) {
		q1Rnk=q1;
		q2Rnk=q2;
		q3Rnk=q3;
		q4Rnk=q4;
		q5Rnk=q5;
		q6Rnk=q6;
	}
	
	
	/**
	 * the method gives back the text for the wanted question
	 * 
	 * 
	 * @param questionNum - question number that was asked
	 * @return - returns String of the question
	 */
	public String getQuestion(int questionNum) {
		switch(questionNum) {
			case 1:
				return q1Txt;
			
			case 2:
				return q2Txt;
			
			case 3:
				return q3Txt;
				
			case 4:
				return q4Txt;
				
			case 5:
				return q5Txt;
				
			case 6:
				return q6Txt;
				
			default:
				return null;
		}
	}
	
	
	/**
	 * the method sets new question to be asked in the survey
	 * 
	 * 
	 * @param questionNum - question number to chang
	 * @param str - the new question to ask
	 */
	public void setQuestionText(int questionNum,String str) {
		switch(questionNum) {
		case 1:
			q1Txt=str;
		
		case 2:
			q2Txt=str;
		
		case 3:
			q3Txt=str;
			
		case 4:
			q4Txt=str;
			
		case 5:
			q5Txt=str;
			
		case 6:
			q6Txt=str;
		
		default:
			return;
		}
	}
	
}
