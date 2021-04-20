package tokenPackage;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleImmutableEntry;

import exceptionPackage.UnableToTokenizeException;

public class Tokenizer {

    private ArrayList<SimpleImmutableEntry<String, String>> classifiedTokens;
    private int tokenIndex = 0;

    public Tokenizer(String incomingCommand) throws UnableToTokenizeException {

        this.tokenizeIncomingCommand(incomingCommand);
    }

    private void tokenizeIncomingCommand(String command) throws UnableToTokenizeException {

        /* if incomming command is nothing throw expection */
        if (command.equals("")) {
            throw new UnableToTokenizeException();
        } else {
            /* classify the incoming command */
            StringSplitter sp = new StringSplitter(command);
            TokenClassifier tc = new TokenClassifier(sp.getSpiltArrayList());
            this.classifiedTokens = tc.getClassifiedTkns();
            /*check for unclassified tokens*/
            for (SimpleImmutableEntry<String, String> tkn : this.classifiedTokens) {
                if (tkn.getKey().equals("UNCLASSIFIED")) {
                    throw new UnableToTokenizeException();
                }
            }
        }
    }

    public boolean gotMoreTokens() {
        if (this.tokenIndex < this.classifiedTokens.size()) {
            return true;
        }
        return false;
    }


    public SimpleImmutableEntry<String, String> nextToken() {
        if (this.gotMoreTokens()) {
            SimpleImmutableEntry<String, String> token = this.classifiedTokens.get(this.tokenIndex);
            this.tokenIndex++;
            return token;
        }
        return null;
    }

    public SimpleImmutableEntry<String, String> peekNextToken() {
        if (this.gotMoreTokens()) {
            SimpleImmutableEntry<String, String> token = this.classifiedTokens.get(this.tokenIndex);
            return token;
        }
        return null;
    }

    public SimpleImmutableEntry<String, String> previousToken() {
        if (this.tokenIndex != 0) {
            SimpleImmutableEntry<String, String> prevTkn = this.classifiedTokens.get(this.tokenIndex - 2);
            return prevTkn;
        }
        return null;
    }

    public ArrayList<SimpleImmutableEntry<String, String>> getTokenArray() {
        return this.classifiedTokens;
    }

    public int getTokenIndex() {
        return this.tokenIndex;
    }

}
