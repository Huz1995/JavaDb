package tokenPackage.FormalGrammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Keywords implements FormalGrammar {

    ArrayList<String> cmdTypeRegexArray;
    private String signature = "KW";

    public Keywords() {
        this.cmdTypeRegexArray = new ArrayList<String>();
        this.cmdTypeRegexArray.add("^DATABASE$|^TABLE$");
        this.cmdTypeRegexArray.add("^ADD$|^DROP$");
        this.cmdTypeRegexArray.add("^[*]$");
        this.cmdTypeRegexArray.add("^FROM$");
        this.cmdTypeRegexArray.add("^WHERE$");
        this.cmdTypeRegexArray.add("^AND$");
        this.cmdTypeRegexArray.add("^OR$");
        this.cmdTypeRegexArray.add("^ON$");
        this.cmdTypeRegexArray.add("^INTO$");
        this.cmdTypeRegexArray.add("^VALUES$");
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
