package entities;


import java.io.Serializable;

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

	
	private int surveyNum;
	private String[] questions=new String[6];
	private int[] singleRank = new int[6];
	
	private int[][] totalRanks = new int[6][10];
	
	
	
	/**
	 *Getter for the surveyNum
	 * @return the surveyNum
	 */
	public int getSurveyNum() {
		return surveyNum;
	}


	/**
	 *Setter for the surveyNum
	 * @param surveyNum the surveyNum to set
	 */
	public void setSurveyNum(int surveyNum) {
		this.surveyNum = surveyNum;
	}

	
	
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

	/**
	 * Setter for the question rank
	 * @param questionNum num of Q
	 * @param rankColoumn	rank of the column
	 * @param allRanks
	 */
	public void setTotalRanks(int questionNum,int rankColoumn,int allRanks) {
		
		if(questionNum<=6 && rankColoumn<=10)
			totalRanks[questionNum-1][rankColoumn-1]=allRanks;
			
	}
	
	/**
	 * Getter for a question's total column rank
	 * @param questionNum num of Q
	 * @param rankColoumn rank to Col
	 * @return
	 */
	public int getTotalRanks(int questionNum,int rankColoumn) {
		
		if(questionNum<=6 && rankColoumn<=10)
			return totalRanks[questionNum-1][rankColoumn-1];
	
		return -1;
	}
	
	
}
