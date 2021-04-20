package tokenPackage;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleImmutableEntry;

import tokenPackage.FormalGrammar.*;

public class TokenClassifier {

    private ArrayList<SimpleImmutableEntry<String, String>> classifiedTkns;

    public TokenClassifier(ArrayList<String> unClassifiedTkns) {
        this.classifiedTkns = new ArrayList<SimpleImmutableEntry<String, String>>();
        this.classifyIncomingString(unClassifiedTkns);
        System.out.println(classifiedTkns);
    }

    private void classifyIncomingString(ArrayList<String> unClassifiedTkns) {

        /*
         * for every unclassifed token when need to match to a classifier class as sign
         * it with signiture. If not classifed sign with "UNCLASSIFIED
         */
        for (String unClassifiedTkn : unClassifiedTkns) {
            if (this.tokenClassifcation(unClassifiedTkn, new CommandTypes())) {
                continue;
            } else if (this.tokenClassifcation(unClassifiedTkn, new Keywords())) {
                continue;
            } else if (this.tokenClassifcation(unClassifiedTkn, new Literals())) {
                continue;
            } else if (this.tokenClassifcation(unClassifiedTkn, new Operators())) {
                continue;
            } else if (this.tokenClassifcation(unClassifiedTkn, new Punctuation())) {
                continue;
            } else if (this.tokenClassifcation(unClassifiedTkn, new Names())) {
                continue;
            } else {
                SimpleImmutableEntry<String, String> tokenPair = new SimpleImmutableEntry<String, String>(
                        "UNCLASSIFIED", unClassifiedTkn);
                classifiedTkns.add(tokenPair);
            }
        }
    }

    /*if token is apart of group then sign with its singiture*/
    private boolean tokenClassifcation(String token, FormalGrammar classifier) {
        boolean isClassified = classifier.isTokenInGroup(token);
        if (isClassified) {
            SimpleImmutableEntry<String, String> tokenPair = 
                new SimpleImmutableEntry<String, String>(classifier.signature(), token);
            classifiedTkns.add(tokenPair);
            return isClassified;
        }
        return false;
    }

    public ArrayList<SimpleImmutableEntry<String, String>> getClassifiedTkns() {
        return this.classifiedTkns;
    }

}
