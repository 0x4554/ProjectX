package entities;

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
public class SurveyEntity {

	private Map<String,ArrayList<Integer>> questionsAndAnswers;		//*The String (key) is the question and the ArrayList holds all of the answers to that question*//

	/**
	 * Constructor for the SurveyEntity.java class
	 * @param questions the questions of the survey
	 */
	public SurveyEntity(ArrayList<String> questions) {
		this.questionsAndAnswers = new HashMap<String,ArrayList<Integer>>();
		for (String str : questions)
		{
			this.questionsAndAnswers.put(str, new ArrayList<Integer>());	//build a hash map with the questions
		}
	}

	/**
	 * Getter for the questionsAndAnswers
	 * @return the questionsAndAnswers
	 */
	public Map<String, ArrayList<Integer>> getQuestionsAndAnswers() {
		return questionsAndAnswers;
	}
	
	/**
	 * This methods adds a answer to a question
	 * @param question
	 * @param answer
	 */
	public void addAnswerToQuestion(String question,Integer answer)
	{
		this.questionsAndAnswers.get(question).add(answer);		//add the answer to the question's answers arrayList
	}
	
	
	

}
