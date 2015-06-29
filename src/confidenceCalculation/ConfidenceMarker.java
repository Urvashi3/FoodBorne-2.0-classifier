package confidenceCalculation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVParser;


/******************************************************
 * Class  : ConfidenceMarker 
 * Desc   : It marks the confidence based on the rules 
 * specified for identifying food-poisoning tweets.
 *****************************************************/
public class ConfidenceMarker {
	SecondParser sp = new SecondParser();
	/*Storing the reference files(for defining rules) in ArrayList*/
	ArrayList<String> SubR1 = new ArrayList<String>();
	ArrayList<String> SubR2 = new ArrayList<String>();
	ArrayList<String> SubIRR = new ArrayList<String>();
	ArrayList<String> PredR1 = new ArrayList<String>();
	ArrayList<String> PredR2 = new ArrayList<String>();
	ArrayList<String> PredR3 = new ArrayList<String>();
	ArrayList<String> ObjR1 = new ArrayList<String>();
	ArrayList<String> ObjR2 = new ArrayList<String>();
	ArrayList<String> PrepR1 = new ArrayList<String>();

	
	/**************************************************
	 * Method : storeValue()
	 * Desc   : It stores the reference files values 
	 * in an ArrayList.
	 **************************************************/
	public void storeValue() {
		Scanner s;
		try {
			s = new Scanner(new File("Files\\SubR1.txt"));		//Read the reference file.

			while (s.hasNext()) {
				SubR1.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\SubR2.txt"));		//Read the reference file.

			while (s.hasNext()) {
				SubR2.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\SubIRR.txt"));		//Read the reference file.

			while (s.hasNext()) {
				SubIRR.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\PredR1.txt"));		//Read the reference file.

			while (s.hasNext()) {
				PredR1.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\PredR2.txt"));		//Read the reference file.

			while (s.hasNext()) {
				PredR2.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\PredR3.txt"));		//Read the reference file.

			while (s.hasNext()) {
				PredR3.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\ObjR1.txt"));		//Read the reference file.

			while (s.hasNext()) {
				ObjR1.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\ObjR2.txt"));		//Read the reference file.

			while (s.hasNext()) {
				ObjR2.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s = new Scanner(new File("Files\\PrepR1.txt"));		//Read the reference file.

			while (s.hasNext()) {
				PrepR1.add(s.nextLine());						//Store the reference file in Arraylist.
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**************************************************
	 * Method : containsCaseInsensitive()
	 * Desc   : It checks if the element is present 
	 * or not in the Arraylist (case-sensitive).
	 **************************************************/
	public boolean containsCaseInsensitive(String s, List<String> l) {
		for (String string : l) {
			if (string.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	/**************************************************
	 * Method : confidenceValue()
	 * Desc   : It computes the confidence value based 
	 * on the defined rules.
	 **************************************************/
	public double confidenceValue(String tweet, String subj, String pred,
			String obj, String prep) {

		double confidence = 0;
		
		/*Assigning Confidence based on rules*/
		if (containsCaseInsensitive(subj, SubR1)
				&& (pred.equalsIgnoreCase("think") || pred
						.equalsIgnoreCase("thinks"))) {
			confidence = 100;
		} else if (containsCaseInsensitive(subj, SubR1)
				&& containsCaseInsensitive(pred, PredR1)
				&& containsCaseInsensitive(obj, ObjR1) && prep.equals(" ")) {
			confidence = 0.9;
		} else if (containsCaseInsensitive(subj, SubR1)
				&& containsCaseInsensitive(pred, PredR1)
				&& containsCaseInsensitive(prep, PrepR1)) {
			confidence = 0.8;
		} else if (containsCaseInsensitive(subj, SubR1)
				&& containsCaseInsensitive(prep, PrepR1)) {
			confidence = 0.8;
		} else if (containsCaseInsensitive(subj, SubR1)
				&& containsCaseInsensitive(pred, PredR3) && obj.equals(" ")
				&& prep.equals(" ")) {
			confidence = 0.8;
		} else if (containsCaseInsensitive(subj, SubIRR)
				&& containsCaseInsensitive(pred, PredR1)
				&& containsCaseInsensitive(obj, ObjR1) && prep.equals(" ")) {
			confidence = 0.2;
		} else if (!containsCaseInsensitive(subj, SubIRR)
				&& containsCaseInsensitive(pred, PredR1)
				&& containsCaseInsensitive(prep, PrepR1)) {
			confidence = 0.7;
		} else if (subj.equals(" ") && containsCaseInsensitive(pred, PredR1)
				&& containsCaseInsensitive(obj, ObjR1)) {
			confidence = 0.6;
		} else if (containsCaseInsensitive(subj, SubR1)
				&& containsCaseInsensitive(pred, PredR1) && obj.equals(" ")
				&& prep.equals(" ")) {
			confidence = 0.2;
		} else if (containsCaseInsensitive(subj, SubR2)
				&& containsCaseInsensitive(pred, PredR2)
				&& containsCaseInsensitive(obj, ObjR2)) {
			confidence = 0.9;
		} else if (containsCaseInsensitive(subj, SubR2)
				&& containsCaseInsensitive(pred, PredR2)) {
			confidence = 0.8;
		} else if (containsCaseInsensitive(subj, SubR2) && pred.equals(" ")
				&& obj.equals(" ")) {
			confidence = 0.6;
		} else if (subj.equals("food") && containsCaseInsensitive(pred, PredR3)) {
			confidence = 0.6;
		} else if (!containsCaseInsensitive(subj, SubIRR)
				&& pred.equalsIgnoreCase("gave")
				&& containsCaseInsensitive(obj, ObjR1)) {
			confidence = 0.6;
		}
		return confidence;
	}

	/**************************************************
	 * Method : storeConfidence()
	 * Desc   : It assigns confidence after performing 
	 * a second parsing.
	 **************************************************/
	public void storeConfidence() {

		String csvFile = "Files\\TempFile\\TweetsOutput2.csv";
		BufferedReader br = null;
		FileWriter writer = null;
		String line = "";
		String tweet = "";
		String subj = "";
		String pred = "";
		String obj = "";
		String prep = "";
		double confidence;
		String c = "";
		String tweetNum = "";
		String tweetOriginal = "";

		try {
			writer = new FileWriter("Files\\TempFile\\TweetConf.csv");
			br = new BufferedReader(new FileReader(csvFile));
			CSVParser csvParser = new CSVParser();
			while ((line = br.readLine()) != null) {
				String[] lineArray = csvParser.parseLine(line);
				tweetNum = lineArray[0];
				tweetOriginal = lineArray[1];
				tweet = lineArray[2];
				subj = lineArray[3];
				pred = lineArray[4];
				obj = lineArray[5];
				prep = lineArray[6];

				confidence = confidenceValue(tweet, subj, pred, obj, prep);
				/*Assigning confidence after second parsing, as per some more rules*/
				if (confidence >= 0.6) {
					if (sp.pastTenseParser(tweet)) {
						confidence = 0.1;
					} else if (sp.checkForOR(tweet)) {
						confidence = 0.15;
					} else if (sp.checkForIf(tweet)) {
						confidence = 0.18;
					}
				}
				c = Double.toString(confidence);

				// Writing the previous POS in csv
				writer.append(tweetNum);
				writer.append(',');
				writer.append(tweetOriginal);
				writer.append(',');
				writer.append(tweet);
				writer.append(',');
				writer.append(subj);
				writer.append(',');
				writer.append(pred);
				writer.append(',');
				writer.append(obj);
				writer.append(',');
				writer.append(prep);
				writer.append(',');
				writer.append(c);
				writer.append('\n');
			}
			writer.close();

		} catch (FileNotFoundException e) {

			File myFile = new File(csvFile);
			try {
				System.out.println("Attempting to read from file in: "
						+ myFile.getCanonicalPath());
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**************************************************
	 * Method : getResult()
	 * Desc   : It produces the results in a csv "TweetResult",
	 * which is stored on the workspace project folder.
	 **************************************************/
	public void getResult() {
		String csvFile = "Files\\TempFile\\TweetConf.csv";		//Temporary intermediate file
		BufferedReader br = null;
		FileWriter writer = null;
		String line = "";
		String tweetNum ="";
		String tweet = "";
		String confidence = "";
		ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
		ArrayList<String> innerList1 = new ArrayList<String>();
		ArrayList<String> innerList2 = new ArrayList<String>();
		ArrayList<String> innerList3 = new ArrayList<String>();
		
		try{
			writer = new FileWriter("TweetResult.csv");			//Final Result csv(Stired on the workspace folder).
			br = new BufferedReader(new FileReader(csvFile));
			CSVParser csvParser = new CSVParser();
			boolean firstRowFlag = true;
			int i =0;
			while ((line = br.readLine()) != null) {
				String[] lineArray = csvParser.parseLine(line);
				tweetNum = lineArray[0];
				tweet = lineArray[1];
				confidence = lineArray[7];
				if(firstRowFlag==false){
					if(finalResult.get(0).contains(tweetNum)){
						if(Double.parseDouble(finalResult.get(2).get(i))<Double.parseDouble(confidence)){
							finalResult.get(2).set(i, confidence);
						}
					}
					else{
						i++;
						finalResult.get(0).add(tweetNum);
						finalResult.get(1).add(tweet);
						finalResult.get(2).add(confidence);
						
					}
					
				}
				if(firstRowFlag==true){
					firstRowFlag=false;
					innerList1.add(tweetNum);
					innerList2.add(tweet);
					innerList3.add(confidence);
					finalResult.add(innerList1);
					finalResult.add(innerList2);
					finalResult.add(innerList3);
				}
				
			}
			
			int innerListSize = finalResult.get(0).size();
			for(int j=0; j<innerListSize; j++){
				writer.append(finalResult.get(0).get(j));
				writer.append(',');
				writer.append(finalResult.get(1).get(j));
				writer.append(',');
				writer.append(finalResult.get(2).get(j));
				writer.append(',');
				if(Double.parseDouble(finalResult.get(2).get(j))<0.6){
					writer.append("Non food poisoning tweet");
				}
				else if(Double.parseDouble(finalResult.get(2).get(j))>=0.6 && Double.parseDouble(finalResult.get(2).get(j))<=1){
					writer.append("Food poisoning tweet");
				}
				else if(Double.parseDouble(finalResult.get(2).get(j))==100){
					writer.append("Non food poisoning tweet");
				}
				else{
					writer.append("Non food poisoning tweet");
				}
				writer.append('\n');
			}
			
			writer.close();
		}catch (FileNotFoundException e) {

			File myFile = new File(csvFile);
			try {
				System.out.println("Attempting to read from file in: "
						+ myFile.getCanonicalPath());
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
