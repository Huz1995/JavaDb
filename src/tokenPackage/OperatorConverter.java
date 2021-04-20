package tokenPackage;

import tokenPackage.FormalGrammar.RegExp;

public class OperatorConverter {

    private OperatorConverter() {
    }

    /*takes incoming raw tokens and if >=,!=,== etc convert*/
    public static String convertOperands(String[] incomingTokens) {
        for (int i = 0; i < incomingTokens.length; i++) {
            boolean isStringLit = RegExp.STRGLIT.doesTokenMatch(incomingTokens[i]);
            if (!isStringLit) {
                String convertedToken = incomingTokens[i].replace(">=", "}");
                convertedToken = convertedToken.replace("<=", "{");
                convertedToken = convertedToken.replace("!=", "!");
                convertedToken = convertedToken.replace("==", "@");
                incomingTokens[i] = convertedToken;
            }
        }
        /* convert back to string for splitting */
        String alteredString = String.join(" ", incomingTokens);
        return alteredString;
    }

}
