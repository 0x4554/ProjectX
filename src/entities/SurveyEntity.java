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


	public SurveyEntity() {
		
	}

	//private Map<String,ArrayList<Integer>> questionsAndAnswers;		//*The String (key) is the question and the ArrayList holds all of the answers to that question*//

//	private String q1Txt;
//	private String q2Txt;
//	private String q3Txt;
//	private String q4Txt;
//	private String q5Txt;
//	private String q6Txt;
	
	private String[] questions=new String[6];
	private int[] singleRank = new int[6];
	
	private int[][] totalRanks = new int[6][10];
//	private int[] q2TotalAnswers = new int[10];
//	private int[] q3TotalAnswers = new int[10];
//	private int[] q4TotalAnswers = new int[10];
//	private int[] q5TotalAnswers = new int[10];
//	private int[] q6TotalAnswers = new int[10];
	
	
	
	/**
	 * the method sets the answers for the current survey
	 * 
	 * @param q1 - q1 answer
	 * @param q2 - q2 answer
	 * @param q3 - q3 answer
	 * @param q4 - q4 answer
	 * @param q5 - q5 answer
	 * @param q6 - q6 answer
	 */
	public void setAnswers(int q1,int q2,int q3,int q4,int q5,int q6) {
		singleRank[0]= q1;
		singleRank[1]=q2;
		singleRank[2]=q3;
		singleRank[3]=q4;
		singleRank[4]=q5;
		singleRank[5]=q6;
	}
	
	
	/**
	 * the method gives back the text for the wanted question
	 * 
	 * 
	 * @param questionNum - question number that was asked
	 * @return - returns String of the question
	 */
	public String getQuestionText(int questionNum) {
		
		return questions[questionNum-1];
	}
//		switch(questionNum) {
//			case 1:
//				return q1Txt;
//			
//			case 2:
//				return q2Txt;
//			
//			case 3:
//				return q3Txt;
//				
//			case 4:
//				return q4Txt;
//				
//			case 5:
//				return q5Txt;
//				
//			case 6:
//				return q6Txt;
//				
//			default:
//				return null;
//		}
//	}
	
	
	/**
	 * the method sets new question to be asked in the survey
	 * 
	 * 
	 * @param questionNum - question number to chang
	 * @param str - the new question to ask
	 */
	public void setQuestionText(int questionNum,String str) {
		if(questionNum<=6)
			questions[questionNum-1] = str;
	}
//		switch(questionNum) {
//		case 1:
//			q1Txt=str;
//		
//		case 2:
//			q2Txt=str;
//		
//		case 3:
//			q3Txt=str;
//			
//		case 4:
//			q4Txt=str;
//			
//		case 5:
//			q5Txt=str;
//			
//		case 6:
//			q6Txt=str;
//		
//		default:
//			return;
//		}
//	}
	
	
	/**
	 * this method return the rank for each question in the survey
	 * 
	 * @param questionNum number of question to get the rank for
	 * @return returns the rank if the question number exists or -1 if it doesn't
	 */
	public int getQuestionRank(int questionNum) {
		
		if(questionNum<=6)
			return singleRank[questionNum-1];
		return -1;
	}
//	switch(questionNum) {
//		case 1:
//			return q1Rnk;
//	
//		case 2:
//			return q2Rnk;
//	
//		case 3:
//			return q3Rnk;
//		
//		case 4:
//			return q4Rnk;
//		
//		case 5:
//			return q5Rnk;
//		
//		case 6:
//			return q6Rnk;
//		
//		default:
//			return -1;
//	}
//}
	
	public void setTotalRanks(int questionNum,int rankColoumn,int allRanks) {
		
		if(questionNum<=6 && rankColoumn<=10)
			totalRanks[questionNum-1][rankColoumn-1]=allRanks;
			
	}
	
	
	public int getTotalRanks(int questionNum,int rankColoumn) {
		
		if(questionNum<=6 && rankColoumn<=10)
			return totalRanks[questionNum-1][rankColoumn-1];
	
		return -1;
	}
	
	
}
