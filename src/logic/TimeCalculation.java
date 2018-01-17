package logic;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**This class is used for calculating time differences
 * 
 * TimeCalculation.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class TimeCalculation {
	
	/**
	   * This method calculates time difference between two Timestamp objects
	   * @param t1	Later Timestamp
	   * @param t2	sooner Timestamp
	   * @return the time difference in miliseconds
	   */
	  public static Long calculateTimeDifference(Timestamp t1, Timestamp t2)
	  {
			Long t3 = t1.getTime()-t2.getTime();
			System.out.println(TimeUnit.MILLISECONDS.toHours(t3));
			return t3;
	  }
}
