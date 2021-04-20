package interpreterPackage;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.AbstractMap.SimpleImmutableEntry;

import conditionPackage.ConditionTruthGenerator;
import exceptionPackage.UnableToExecuteCmd;
import parserPackage.DbState;
import parserPackage.Parser;
import servicePackage.Service;
import servicePackage.TableStructure;
import tokenPackage.Tokenizer;
import tokenPackage.FormalGrammar.RegExp;
import tokenPackage.FormalGrammar.Literals;
import tokenPackage.FormalGrammar.Names;

public class UpdateCmd {

    private Tokenizer tokens;

    public UpdateCmd(Tokenizer tokens) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.interpretUpdateQuery();
    }

    private void interpretUpdateQuery() throws UnableToExecuteCmd {
        DbState.isDBInUse();

        String tableName = this.tokens.nextToken().getValue();
        /* check for if valid name */
        Parser.checkIsAValidName(tableName);
        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        /* check if valid SET ketword */
        Parser.checkForKW(nextToken, "SET");
        String peakTkn = this.tokens.peekNextToken().getValue();
        if (peakTkn.equals(",")) {
            throw new UnableToExecuteCmd("Comma in wrong place");
        }

        /* load table abd get create literal table */
        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        table.convertValToLiteral();
        this.parseNamValPairList(table);
        /* get the condition statments */
        ArrayList<String> conditionStatementTkns = Parser.getConditionSegment(this.tokens);
        ListIterator<String> iter = conditionStatementTkns.listIterator();
        Parser.areBracketsBalanced(conditionStatementTkns);
        Parser.isValidConditionStatement(iter);
        /* apply conditions to table */
        ConditionTruthGenerator cgt = 
                    new ConditionTruthGenerator(table, conditionStatementTkns);
        table.executeUpdate();
        Service.storeTableInDB(table);
    }

    private void parseNamValPairList(TableStructure table) throws UnableToExecuteCmd {

        /* get the next token in the token stream */
        String nextToken = this.tokens.nextToken().getValue();
        if (nextToken.toUpperCase().equals("WHERE") && 
                !this.tokens.previousToken().getValue().equals(",")) {
            return;
        } else if (nextToken.equals(",")) {
            this.parseNamValPairList(table);
        } else if (RegExp.OBJECTNAME.doesTokenMatch(nextToken)) {
            this.obtainIndexValPairs(nextToken, table);
        } else {
            throw new UnableToExecuteCmd("Key pair list is invalid");
        }
    }

    private void obtainIndexValPairs(String nextToken, TableStructure table) throws UnableToExecuteCmd {
        /* disallow id as not allowed to change the id */
        int attIndexInTable = table.getAttributePos(nextToken, "disallow_id");
        nextToken = this.tokens.nextToken().getValue();
        /* next token needs to be = otherwise throw error */
        if (nextToken.equals("=")) {
            nextToken = this.tokens.nextToken().getValue();
            /* next token needs to be literal otherwise throw error */
            if (new Literals().isTokenInGroup(nextToken)) {
                String peekAheadTkn = this.tokens.peekNextToken().getValue();
                /* check for missing comma */
                if (new Names().isTokenInGroup(peekAheadTkn)) {
                    throw new UnableToExecuteCmd("Incorrect attributes list");
                }
                table.addKeyValPair(new SimpleImmutableEntry<Integer, String>(attIndexInTable, nextToken));
                this.parseNamValPairList(table);
            } else {
                throw new UnableToExecuteCmd("value is not of correct type");
            }
        } else {
            throw new UnableToExecuteCmd("Missing equals sign");
        }

    }

}
