package confidenceCalculation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

	/**************************************************
	* Class  : SecondParser
	* Desc   : It contains to perform a second level
	*  parsing on the tweet.
	**************************************************/
public class SecondParser {
	Scanner s;
	ArrayList<String> Past;
	
	public SecondParser() {
		Past = new ArrayList<String>();
		try {
			s = new Scanner(new File("Files\\Past.txt"));
			
			while (s.hasNext()){
				Past.add(s.nextLine());
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**************************************************
	* Method : pastTenseParser()
	* Desc   : It checks if the tweet talks about a 
	* past incident.
	**************************************************/
	public Boolean pastTenseParser(String tweet)
	{		
		tweet = tweet.toLowerCase();
		for (String string : Past){
			if(tweet.contains(string))
			{
				return true;
			}
	     }
	
		return false;
	}
	
	/**************************************************
	* Method : checkForOR()
	* Desc   : It checks if the tweet talks about 
	* uncertainty of occurrence of food-poisoning.
	* (like "food poisoning or flu")
	**************************************************/
	public Boolean checkForOR(String tweet)
	{
		tweet = tweet.toLowerCase();
		
		if(tweet.contains("food poisoning or")||tweet.contains("or food poisoning"))
		{
			return true;
		}
	     
		return false;
	}
	
	/**************************************************
	* Method : checkForIf()
	* Desc   : It checks if the tweet contains "If I
	* have food poisoning". 
	**************************************************/
	public Boolean checkForIf(String tweet)
	{
		tweet = tweet.toLowerCase();
		
		if(tweet.contains("if i have food poisoning")||tweet.contains("if i got food poisoning"))
		{
			return true;
		}
		
		return false;
	}

}
