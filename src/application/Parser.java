package application;

import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import com.opencsv.CSVParser;

import confidenceCalculation.ConfidenceMarker;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/****************************************************
 * Class : Parser Desc : The class parses the tweets and identifies whether it
 * is a food-poisoning tweet or not.
 ***************************************************/
class Parser {

	/* main() method of the application. */
	public static void main(String[] args) {
		/* Using the NLP Stanford Parser */
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		if (args.length > 0) {
			parserModel = args[0];
		}
		LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
		tweetsToSentence();
		tweetParser(lp);
		ConfidenceMarker c = new ConfidenceMarker();
		c.storeValue();
		c.storeConfidence();
		c.getResult();
	}

	/****************************************************
	 * Method : tweetParser() Desc : It determines the structure of the tweet.
	 ***************************************************/
	public static void tweetParser(LexicalizedParser lp) {

		String csvFile = "Files\\TempFile\\TweetsSentence.csv"; // Temporary
																// intermediate
																// file
		CSVParser csvParser = new CSVParser(); // CSVParser Utility used to
												// parse csv.
		BufferedReader br = null;
		FileWriter writer = null;
		String line = " ";
		String subj = " ";
		String pred = " ";
		String obj = " ";
		String prep = " ";
		String tweetP = " ";
		String tweetNumP = " ";
		String tweetOriginalP = " ";

		try {
			writer = new FileWriter("Files\\TempFile\\TweetsOutput2.csv"); // Temporary
																			// intermediate
																			// file
																			// generated
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] tweets = csvParser.parseLine(line); // Csv parsed using
																// CSVParser
																// utility.
				String tweetNum = tweets[0];
				String tweetOriginal = tweets[1];
				String tweet = tweets[2];
				String tweetL = tweet.toLowerCase();
				if (tweetL.contains("poisoning and")) {
					tweet = tweetL.split("and")[0];
				} else if (tweetL.contains("poisoning &")) {
					tweet = tweetL.split("&")[0];
				}

				/* Instantiation of the parser */
				TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer
						.factory(new CoreLabelTokenFactory(), "");
				Tokenizer<CoreLabel> tok = tokenizerFactory
						.getTokenizer(new StringReader(tweet));
				List<CoreLabel> rawWords2 = tok.tokenize();
				Tree parse = lp.apply(rawWords2);

				TreebankLanguagePack tlp = lp.treebankLanguagePack();
				GrammaticalStructureFactory gsf = tlp
						.grammaticalStructureFactory();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
				List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

				/* Iterate on all the tweets to break into sentence structure */
				for (Iterator<TypedDependency> i = tdl.iterator(); i.hasNext();) {
					TypedDependency t = i.next();
					if (tweetP.equals(" ")) {
						tweetP = tweet;
						tweetNumP = tweetNum;
						tweetOriginalP = tweetOriginal;
					}
					if ((t.reln().toString()).equals("nsubj")) {	//Obtaining the 'Subject part'.
						if (subj.equals(" ") == false
								|| obj.equals(" ") == false) {
							// Writing the previous POS in csv
							writer.append(tweetNumP);
							writer.append(',');
							writer.append(tweetOriginalP);
							writer.append(',');
							writer.append(tweetP);
							writer.append(',');
							writer.append(subj);
							writer.append(',');
							writer.append(pred);
							writer.append(',');
							writer.append(obj);
							writer.append(',');
							writer.append(prep);
							writer.append('\n');

							// Initialize the new POS
							subj = " ";
							pred = " ";
							obj = " ";
							prep = " ";
							tweetP = tweet;
							tweetNumP = tweetNum;
							tweetOriginalP = tweetOriginal;

						}

						subj = t.dep().toString();
						subj = subj.split("\\/")[0];
						pred = t.gov().toString();					//Obtaining the 'Predicate' part
						pred = pred.split("\\/")[0];

					}

					if ((t.reln().toString()).equals("dobj")) {		//Obtaining the 'Object' part
						String g = t.gov().toString();
						g = g.split("\\/")[0];
						if (g.equals(pred)) {
							obj = t.dep().toString();
							obj = obj.split("\\/")[0];
						} else if (subj.equals(" ") == false
								|| obj.equals(" ") == false) {
							// Writing the previous POS in csv
							writer.append(tweetNumP);
							writer.append(',');
							writer.append(tweetOriginalP);
							writer.append(',');
							writer.append(tweetP);
							writer.append(',');
							writer.append(subj);
							writer.append(',');
							writer.append(pred);
							writer.append(',');
							writer.append(obj);
							writer.append(',');
							writer.append(prep);
							writer.append('\n');

							// Initialize the new POS
							subj = " ";
							pred = " ";
							obj = " ";
							prep = " ";
							tweetP = tweet;
							tweetNumP = tweetNum;
							tweetOriginalP = tweetOriginal;

						}
						
						// Populating the value of object
						pred = t.gov().toString();						//Obtaining the 'Predicate' part
						pred = pred.split("\\/")[0];
						obj = t.dep().toString();
						obj = obj.split("\\/")[0];

					}

					if ((t.reln().toString()).startsWith("prep")) {		//Obtaining the 'Preposition' part
						String gov = t.gov().toString();
						gov = gov.split("\\/")[0];
						String dep = t.dep().toString();
						dep = dep.split("\\/")[0];
						if ((gov.equals(subj) || gov.equals(obj) || gov
								.equals(pred)) && (dep.equals("poisoning"))) {
							prep = dep;
						}

					}

				}

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

	/******************************************************
	 * Method : tweetsToSentence() Desc : It breaks the tweet into multiple
	 * sentences.
	 *****************************************************/
	public static void tweetsToSentence() {
		// CHANGE THE PATH OF THE FILE
		String csvFile = "TweetC.csv";
		BufferedReader br = null;
		FileWriter writer = null;
		String line = "";

		try {
			writer = new FileWriter("Files\\TempFile\\TweetsSentence.csv");		//Temporary intermediate file
			br = new BufferedReader(new FileReader(csvFile));
			int tweetNum = 1;
			while ((line = br.readLine()) != null) {
				String tweetOriginal = line;
				String[] sentence = line.split("\\.");							//Splitting tweet into sentences.
				if (writer == null) {
					System.out.println("writer null");
				}

				for (int i = 0; i <= sentence.length - 1; i++) {
					String s = new String();
					s = sentence[i].replaceAll("[^\\p{ASCII}]", "");
					writer.append(String.valueOf(tweetNum));
					writer.append(",");
					writer.append(tweetOriginal);
					writer.append(",");
					writer.append(s);
					writer.append('\n');
				}
				tweetNum++;
			}
			writer.append("0");
			writer.append(",");
			writer.append("last line");
			writer.append(",");
			writer.append("last line");
			writer.append('\n');
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Parser() {
	} 

}
