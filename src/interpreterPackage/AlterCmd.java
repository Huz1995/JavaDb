package interpreterPackage;

import servicePackage.*;

import exceptionPackage.UnableToExecuteCmd;
import parserPackage.DbState;
import parserPackage.Parser;
import tokenPackage.Tokenizer;

public class AlterCmd {

    Tokenizer tokens;

    public AlterCmd(Tokenizer tokens) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.interpretAlterCmd();
    }

    private void interpretAlterCmd() throws UnableToExecuteCmd {
        /* check if database is in use */
        DbState.isDBInUse();

        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        /* check for table keyword */
        Parser.checkForKW(nextToken, "TABLE");
        String table = this.tokens.nextToken().getValue();
        /* check if valid name */
        Parser.checkIsAValidName(table);
        String altType = this.tokens.nextToken().getValue().toUpperCase();
        /* get attriubute and and check if valid */
        Parser.checkIsAValidAlt(altType);
        String attName = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(attName);
        /* check if next token is semicolon to ensure one name */
        nextToken = this.tokens.nextToken().getValue();
        Parser.checkIfSemiColon("FINAL", nextToken);
        /* interpret query */
        if (altType.equals("ADD")) {
            this.addAttribute(table, attName);
        } else if (altType.equals("DROP")) {
            this.dropAttribute(table, attName);
        } else {
            throw new UnableToExecuteCmd("ADD or DROP alteration command not found");
        }

    }

    private void addAttribute(String tableName, String attName) throws UnableToExecuteCmd {

        /* create new table struct load data from db into mem */
        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        int numRows = table.getStringTable().size();
        /* add the attribute name and empty string to table */
        for (int i = 0; i < numRows; i++) {
            if (i == 0) {
                table.alterAddCol(attName, i);
            } else {
                table.alterAddCol(" ", i);
            }
        }
        Service.storeTableInDB(table);
    }

    private void dropAttribute(String tableName, String attName) throws UnableToExecuteCmd {

        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        table.convertValToLiteral();
        int attPosition = table.getAttributePos(attName, "disallow_id");
        int numRows = table.getStringTable().size();
        for (int i = 0; i < numRows; i++) {
            table.removeColInRow(i, attPosition);
        }
        Service.storeTableInDB(table);
    }
}
