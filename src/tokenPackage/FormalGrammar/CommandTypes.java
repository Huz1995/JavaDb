package tokenPackage.FormalGrammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

public class CommandTypes implements FormalGrammar {

    ArrayList<String> cmdTypeRegexArray;
    private String signature = "CT";

    public CommandTypes() {
        this.cmdTypeRegexArray = new ArrayList<String>();
        this.cmdTypeRegexArray.add("^USE$");
        this.cmdTypeRegexArray.add("^CREATE$");
        this.cmdTypeRegexArray.add("^DROP$");
        this.cmdTypeRegexArray.add("^ALTER$");
        this.cmdTypeRegexArray.add("^INSERT$");
        this.cmdTypeRegexArray.add("^SELECT$");
        this.cmdTypeRegexArray.add("^UPDATE$");
        this.cmdTypeRegexArray.add("^DELETE$");
        this.cmdTypeRegexArray.add("^JOIN$");

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
