package interpreterPackage;


import parserPackage.DbState;
import servicePackage.Service;
import servicePackage.TableStructure;
import exceptionPackage.UnableToExecuteCmd;
import parserPackage.Parser;
import tokenPackage.Tokenizer;
import tokenPackage.FormalGrammar.*;

public class CreateCmd {

    private Tokenizer tokens;

    public CreateCmd(Tokenizer tokens) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.interpretCreateQuery();
    }

    private void interpretCreateQuery() throws UnableToExecuteCmd {
        String structureTkn = this.tokens.nextToken().getValue().toUpperCase();

        /* if keyword is not database or table then throw error */
        if (structureTkn.equals("DATABASE")) {
            this.createDatabase();
        } else if (structureTkn.equals("TABLE")) {
            this.createTable();
        } else {
            throw new UnableToExecuteCmd("Can only create a Database or Table");
        }
    }

    private void createDatabase() throws UnableToExecuteCmd {

        String dbNameTkn = this.tokens.nextToken().getValue();
        /* check database name in the Names classification */
        Parser.checkIsAValidName(dbNameTkn);
        /* check next token is a semi-colon */
        String nextToken = this.tokens.nextToken().getValue();
        Parser.checkIfSemiColon("FINAL", nextToken);
        /* create file path and make a new directory */
        Service.createDatabase(dbNameTkn);

    }

    private void createTable() throws UnableToExecuteCmd {
        DbState.isDBInUse();
        /* check if table name is a valid name */
        String tableName = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(tableName);
        TableStructure newTable = new TableStructure(tableName);
        String nextToken = this.tokens.nextToken().getValue();
        /* if the next token is a semi colon then create empty table */
        if (Parser.checkIfSemiColon("CARRYON", nextToken)) {
            Service.createTable(newTable);
            return;
        }
        /* if the next token is an opening bracket store the attributes */
        String peekToken = this.tokens.peekNextToken().getValue();
        Parser.checkIfOpeningBrckt(nextToken,peekToken);
        this.getTableAttributes(newTable);
        nextToken = this.tokens.nextToken().getValue();
        Parser.checkIfSemiColon("FINAL", nextToken);
        Service.createTable(newTable);

    }

    private void getTableAttributes(TableStructure newTable) throws UnableToExecuteCmd {

        /* get the next token in the token stream */
        String nextToken = this.tokens.nextToken().getValue();
        /*
         * if a name then store in table, if a comma the continue, if closing bracket
         * then end
         */
        if (nextToken.equals(")") && !this.tokens.previousToken().getValue().equals(",")) {
            return;
        } else if (RegExp.OBJECTNAME.doesTokenMatch(nextToken)) {
            String peekAheadTkn = this.tokens.peekNextToken().getValue();
            /*check for missing comma*/
            if(new Names().isTokenInGroup(peekAheadTkn)){
                throw new UnableToExecuteCmd("Incorrect attributes list");
            }
            newTable.setRowValue(nextToken);
            getTableAttributes(newTable);
        } else if (nextToken.equals(",")) {
            getTableAttributes(newTable);
        } else {
            throw new UnableToExecuteCmd("The attribute list is not correct");
        }
    }

}
