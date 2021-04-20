package tokenPackage.FormalGrammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Literals implements FormalGrammar {

    ArrayList<String> cmdTypeRegexArray;
    private String signature = "LT";

    public Literals() {
        this.cmdTypeRegexArray = new ArrayList<String>();
        this.cmdTypeRegexArray.add("^[-+]?[1-9][0-9]*$|^0$");
        this.cmdTypeRegexArray.add("^[-+]?[1-9][0-9]*[.][0-9]+$" + "|^[-+]?0[.][0-9]+$");
        this.cmdTypeRegexArray.add("^'([^'\t]|'')*'$");
        this.cmdTypeRegexArray.add("^TRUE$|^FALSE$");

    }

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