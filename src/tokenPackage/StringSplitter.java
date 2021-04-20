package tokenPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Pattern;

import exceptionPackage.UnableToTokenizeException;

public class StringSplitter {

    private ArrayList<String> rawTokens;
    /* regex expression that splits incoming query */

    public StringSplitter(String incomingClientString) throws UnableToTokenizeException {
        /* take incoming string and spilt with regex function */
        String[] rawTokens = incomingClientString.split(regexStringSplitter.PRIMARY.get());
        /* convert the operands with 2 characters to single special characters */
        String stringConvOperators = OperatorConverter.convertOperands(rawTokens);
        /* spilt the returned string with secondary regex exp to deal with operands */
        rawTokens = stringConvOperators.split(regexStringSplitter.SECONDARY.get());
        /* convert to arrayList and remove whitespace */

        this.rawTokens = this.listToArrList(rawTokens);
        this.removeSpaceTokenList();
        this.removeSpaceInToken();
        this.separateSemiColon();

    }

    /* if semi colon is attached to last word then separate */
    private void separateSemiColon() throws UnableToTokenizeException {
        int lastTokenIndex = this.rawTokens.size() - 1;
        String lastToken = this.rawTokens.get(lastTokenIndex);
        /* if last token is any number of ;;;;; then remove */
        Pattern regexPattern = Pattern.compile("^;;+$", Pattern.CASE_INSENSITIVE);
        boolean matcher = regexPattern.matcher(lastToken).find();
        if (matcher) {
            throw new UnableToTokenizeException();
        }
        /* if last token has ; attached then split */
        regexPattern = Pattern.compile(".;$", Pattern.CASE_INSENSITIVE);
        matcher = regexPattern.matcher(lastToken).find();
        if (matcher) {
            String tokenWithoutColon = lastToken.split(";")[0];
            this.rawTokens.set(lastTokenIndex, tokenWithoutColon);
            /* add colon separately */
            this.rawTokens.add(";");
        }
    }

    private void removeSpaceTokenList() {
        Iterator<String> itr = this.rawTokens.iterator();
        while (itr.hasNext()) {
            /* get the value at itr */
            String token = (String) itr.next();
            if (token.equals(" ")) {
                itr.remove();
            }
            if (token.equals("")) {
                itr.remove();
            }
        }
    }

    private void removeSpaceInToken() {
        ListIterator<String> itr = this.rawTokens.listIterator();
        while (itr.hasNext()) {
            int index = itr.nextIndex();
            String token = itr.next();
            if (Character.isWhitespace(token.charAt(0))) {
                token = token.replaceFirst(" ", "");
                this.rawTokens.set(index, token);
            }
        }
    }

    private ArrayList<String> listToArrList(String[] list) {

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 0; i < list.length; i++) {
            arrayList.add(list[i]);
        }
        return arrayList;
    }

    public ArrayList<String> getSpiltArrayList() {
        return this.rawTokens;
    }

}
