package tokenPackage.FormalGrammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Names implements FormalGrammar {

    ArrayList<String> cmdTypeRegexArray;
    private String signature = "ID";

    public Names() {
        this.cmdTypeRegexArray = new ArrayList<String>();
        this.cmdTypeRegexArray.add("^[a-zA-Z0-9]+$");
    }

    public boolean isTokenInGroup(String token) {

        boolean doesTokenMatch = false;

        if (token.toUpperCase().equals("FROM") || 
                token.toUpperCase().equals("WHERE")) {
            return doesTokenMatch;
        }

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