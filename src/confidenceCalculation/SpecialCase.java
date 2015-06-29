package confidenceCalculation;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/**************************************************
 * Class  : SpecialCase
 * Desc   : It considers some more special cases 
 * for confidence computation.
 **************************************************/
public class SpecialCase {
	
	String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
	TokenizerFactory<CoreLabel> tokenizerFactory =
     				PTBTokenizer.factory(new CoreLabelTokenFactory(), "");

	/**************************************************
 	* Method : checkThink()
 	* Desc   : It computes confidence for some special 
 	* cases.
 	**************************************************/
	public Boolean checkThink(String tweet, String pred){
		Boolean flag = false;
		Tokenizer<CoreLabel> tok =
		        tokenizerFactory.getTokenizer(new StringReader(tweet));
		List<CoreLabel> rawWords2 = tok.tokenize();
		Tree parse = lp.apply(rawWords2);

		TreebankLanguagePack tlp = lp.treebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		String s = null;	
		String p = null;
		for(Iterator<TypedDependency> i=tdl.iterator();i.hasNext();)
		{
			TypedDependency t = i.next();
			 if((t.reln().toString()).equals("ccomp")){
				 s = t.dep().toString();
				 s = s.split("\\/")[0];
				 p = t.gov().toString();
				 p = p.split("\\/")[0];
				 if((p.equalsIgnoreCase("think")||p.equalsIgnoreCase("thinks"))&&s.equalsIgnoreCase(pred))
				 {
					 return true;
				 }
			 }
		}
		return flag;
	}
}
