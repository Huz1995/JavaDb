package interpreterPackage;

import java.io.FileNotFoundException;
import java.io.IOException;

import exceptionPackage.UnableToExecuteCmd;
import tokenPackage.FormalGrammar.Literals;
import tokenPackage.FormalGrammar.RegExp;
import parserPackage.DbState;
import parserPackage.Parser;
import servicePackage.Service;
import servicePackage.TableStructure;
import tokenPackage.Tokenizer;

public class InsertCmd {

    Tokenizer tokens;

    public InsertCmd(Tokenizer tokens) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.interpretInsertCmd();
    }

    private void interpretInsertCmd() throws UnableToExecuteCmd {
        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkForKW(nextToken, "INTO");
        String tableName = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(tableName);
        nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkForKW(nextToken, "VALUES");
        nextToken = this.tokens.nextToken().getValue();
        Parser.checkIfOpeningBrckt(nextToken,this.tokens.peekNextToken().getValue());

        DbState.isDBInUse();
        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        table.removeDefaultIdFromInsertRow();
        int id = this.createIdForRow(table);
        /* the actual number of coloums in database */
        int colWidth = table.getStringTable().get(0).size();
        /* when inserting new col set first element id number */
        table.setRowValue(String.valueOf(id));
        this.getValueAttributes(table);
        /*check if dimension match*/
        if (table.getInsertRow().size() == colWidth && colWidth != 1) {
            table.addInsertRowToTable();
            Service.storeTableInDB(table);
        } else {
            throw new UnableToExecuteCmd("Either no attributes or new data as wrong dimension");
        }

    }

    /*created an id for the row*/
    private int createIdForRow(TableStructure table) {
        int id;
        int lastRowIndx = table.getStringTable().size() - 1;
        String idString = table.getStringTable().get(lastRowIndx).get(0);
        if (idString.equals("id")) {
            id = 1;
        } else {
            id = Integer.parseInt(idString) + 1;
        }
        return id;
    }

    private void getValueAttributes(TableStructure newTable) throws UnableToExecuteCmd {

        /* get the next token in the token stream */
        String nextToken = this.tokens.nextToken().getValue();
        /*
         * if a literal then store in table, if a comma the continue, if closing bracket
         * then end
         */
        if (nextToken.equals(")") && !this.tokens.previousToken().getValue().equals(",")) {
            return;
        } else if (RegExp.FLOATLIT.doesTokenMatch(nextToken) || RegExp.STRGLIT.doesTokenMatch(nextToken)
                || RegExp.INTLIT.doesTokenMatch(nextToken) || RegExp.BOOLLIT.doesTokenMatch(nextToken)) {
            String peekAheadTkn = this.tokens.peekNextToken().getValue();
            /*check for missing comma*/
            if(new Literals().isTokenInGroup(peekAheadTkn)){
                throw new UnableToExecuteCmd("Incorrect value list");
            }
            newTable.setRowValue(nextToken);
            getValueAttributes(newTable);
        } else if (nextToken.equals(",")) {
            this.getValueAttributes(newTable);
        } else {
            throw new UnableToExecuteCmd("ncorrect value list");
        }
    }

}
