package interpreterPackage;

import exceptionPackage.UnableToExecuteCmd;
import parserPackage.Parser;
import servicePackage.Service;
import tokenPackage.Tokenizer;

public class UseCmd {

    private Tokenizer tokenStream;

    public UseCmd(Tokenizer tokenStream) throws UnableToExecuteCmd {
        this.tokenStream = tokenStream;
        this.interpretUseCmd();
    }

    private void interpretUseCmd() throws UnableToExecuteCmd {

        String dbName = this.tokenStream.nextToken().getValue();
        /* if next token is a valid name, check if directory exists */
        Parser.checkIsAValidName(dbName);
        String nextToken = this.tokenStream.nextToken().getValue();
        Parser.checkIfSemiColon("FINAL", nextToken);
        /* assert that next token has to be a semi colon */
        Service.setDBInUse(dbName);

    }

}