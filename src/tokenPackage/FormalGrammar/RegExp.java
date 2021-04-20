package tokenPackage.FormalGrammar;

import java.util.regex.Pattern;

public enum RegExp {

    INTLIT(Pattern.compile("^[-+]?[1-9][0-9]*$|^0$")),
    FLOATLIT(Pattern.compile("^[-+]?[1-9][0-9]*[.][0-9]+$" +
                                "|^[-+]?0[.][0-9]+$")),
    STRGLIT(Pattern.compile("^'([^'\t]|'')*'$")),     
    BOOLLIT(Pattern.compile("^TRUE$|^FALSE$",Pattern.CASE_INSENSITIVE)),      

    OBJECTNAME(Pattern.compile("^[a-zA-Z0-9]+$")),
    EQUAL(Pattern.compile("^=$")),  
    EQUIV(Pattern.compile("^@$")),
    NOTEQUAL(Pattern.compile("^!$")),  
    GTEQ(Pattern.compile("^[}]$")),  
    LTEQ(Pattern.compile("^[{]$"));

    private Pattern keyWord;

    private RegExp(Pattern keyWord) {
        this.keyWord = keyWord;
    }

    /*function that takes a token and matches give true if matches*/
    public boolean doesTokenMatch(String token) {
        if(token.toUpperCase().equals("AND") || token.toUpperCase().equals("OR")) {
            return false;
        }
        boolean doesMatch = keyWord.matcher(token).find();
        return doesMatch;
    }    
}
