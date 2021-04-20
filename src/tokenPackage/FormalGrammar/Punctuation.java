package tokenPackage.FormalGrammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Punctuation implements FormalGrammar {

    ArrayList<String> cmdTypeRegexArray;
    private String signature = "PN";

    public Punctuation() {
        this.cmdTypeRegexArray = new ArrayList<String>();
        this.cmdTypeRegexArray.add("^[(]$");
        this.cmdTypeRegexArray.add("^[)]$");
        this.cmdTypeRegexArray.add("^,$");
        this.cmdTypeRegexArray.add("^;$");

    }

    /*funciton that checks to see if token is in the group*/
    public boolean isTokenInGroup(String token) {

        boolean doesTokenMatch = false;

        for (String regexExp : this.cmdTypeRegexArray) {
            Pattern pattern = Pattern.compile(regexExp, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(token);
            doesTokenMatch = matcher.find();
            if (doesTokenMatch) {
                break;
            }
        }
        return doesTokenMatch;

    }

    public String signature() {
        return this.signature;
    }
}