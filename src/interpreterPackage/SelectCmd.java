package interpreterPackage;

import java.util.ArrayList;
import java.util.ListIterator;

import conditionPackage.ConditionTruthGenerator;
import exceptionPackage.UnableToExecuteCmd;
import tokenPackage.FormalGrammar.Names;
import tokenPackage.FormalGrammar.RegExp;
import parserPackage.DbState;
import parserPackage.Parser;
import servicePackage.Service;
import servicePackage.TableStructure;
import tokenPackage.Tokenizer;

public class SelectCmd {

    Tokenizer tokens;
    DbState response;

    public SelectCmd(Tokenizer tokens, DbState response) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.response = response;
        this.interpretSelectQuery();
    }

    /* next token needs to be * or name */
    private void interpretSelectQuery() throws UnableToExecuteCmd {
        String nextToken = this.tokens.nextToken().getValue();
        DbState.isDBInUse();
        if (nextToken.equals("*")) {
            this.selectWild();
        } else if (RegExp.OBJECTNAME.doesTokenMatch(nextToken)) {
            String peekToken = this.tokens.peekNextToken().getValue();
            if (peekToken.equals(",")||peekToken.toUpperCase().equals("FROM")) {
                this.selectAttributes(nextToken);
            } else {
                throw new UnableToExecuteCmd("Incorrect attribute list statement"); 
            }
        } else {
            throw new UnableToExecuteCmd("Incorrect select statement");
        }

    }

    /* get all values from the table for client */
    private void selectWild() throws UnableToExecuteCmd {

        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkForKW(nextToken, "FROM");
        String tableName = this.tokens.nextToken().getValue();
        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        table.convertValToLiteral();
        /* if ; then build literal else get the conditions */
        nextToken = this.tokens.nextToken().getValue().toUpperCase();
        if (Parser.checkIfSemiColon("CARRYON", nextToken)) {
            this.buildResponse(table);
            return;
        }
        Parser.checkForKW(nextToken, "WHERE");
        this.selectConditionedRows(table);
        this.buildResponse(table);

    }

    public void selectAttributes(String firstAttribute) throws UnableToExecuteCmd {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add(firstAttribute);
        /* get the attributes needed and store in array */
        this.getAttList(attributes);
        String tableName = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(tableName);
        /* create new table */
        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        table.convertValToLiteral();
        /* store selected index in table to display attributes to user */
        for (int i = 0; i < attributes.size(); i++) {
            int index = table.getAttributePos(attributes.get(i), "allow_id");
            table.setSelectIndexs(index);
        }
        /* if next token is ;, remove attributes and send to user */
        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        if (Parser.checkIfSemiColon("CARRYON", nextToken)) {
            table.removeUnselectedColumns();
            this.buildResponse(table);
            return;
        }
        /* if where then get the conditions */
        Parser.checkForKW(nextToken, "WHERE");
        this.selectConditionedRows(table);
        table.removeUnselectedColumns();
        this.buildResponse(table);

    }

    /* takes in a table and applies conditions to it to get selected rows */
    private void selectConditionedRows(TableStructure table) throws UnableToExecuteCmd {
        ArrayList<String> conditionStatementTkns = Parser.getConditionSegment(this.tokens);
        ListIterator<String> iter = conditionStatementTkns.listIterator();
        Parser.areBracketsBalanced(conditionStatementTkns);
        Parser.isValidConditionStatement(iter);
        ConditionTruthGenerator cgt = new ConditionTruthGenerator(table, conditionStatementTkns);
        table.executeRemoveRow("SELECT");
    }

    private void getAttList(ArrayList<String> attributes) throws UnableToExecuteCmd {

        /* get the next token in the token stream */
        String nextToken = this.tokens.nextToken().getValue();

        if (nextToken.toUpperCase().equals("FROM") && !this.tokens.previousToken().getValue().equals(",")) {
            return;
        } else if (RegExp.OBJECTNAME.doesTokenMatch(nextToken)) {
            String peekAheadTkn = this.tokens.peekNextToken().getValue();
            /* check for missing comma */
            if (new Names().isTokenInGroup(peekAheadTkn)) {
                throw new UnableToExecuteCmd("Incorrect attributes list");
            }
            attributes.add(nextToken);
            getAttList(attributes);
        } else if (nextToken.equals(",")) {
            getAttList(attributes);
        } else {
            throw new UnableToExecuteCmd("The attribute list is not correct");
        }
    }

    private void buildResponse(TableStructure table) {
        this.response.setStringTable(table.getStringTable());
        this.response.buildResponse();
    }

}
