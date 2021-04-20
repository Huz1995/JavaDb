package interpreterPackage;


import exceptionPackage.UnableToExecuteCmd;
import parserPackage.DbState;
import parserPackage.Parser;
import servicePackage.Service;
import tokenPackage.Tokenizer;

public class DropCmd {

    private Tokenizer tokens;

    public DropCmd(Tokenizer tokens) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.interpretDropQuery();
    }

    private void interpretDropQuery() throws UnableToExecuteCmd {
        String structure = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkIsAValidStructure(structure);
        String structName = this.tokens.nextToken().getValue();
        /* check if is a valid name */
        Parser.checkIsAValidName(structName);
        /* check next token is a semi-colon */
        String nextToken = this.tokens.nextToken().getValue();
        Parser.checkIfSemiColon("FINAL", nextToken);
        /* if keyword is not database or table then throw error */
        if (structure.equals("DATABASE")) {
            Service.dropDatabase(structName);
        } else if (structure.equals("TABLE")) {
            DbState.isDBInUse();
            Service.dropTable(structName);
        } else {
            throw new UnableToExecuteCmd("[ERROR]: Can only drop a Database or Table");
        }

    }

}
