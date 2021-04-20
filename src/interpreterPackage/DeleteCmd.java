package interpreterPackage;

import servicePackage.Service;

import java.util.ArrayList;
import java.util.ListIterator;

import conditionPackage.ConditionTruthGenerator;
import exceptionPackage.UnableToExecuteCmd;
import parserPackage.DbState;
import parserPackage.Parser;
import servicePackage.TableStructure;
import tokenPackage.Tokenizer;

public class DeleteCmd {

    private Tokenizer tokens;

    public DeleteCmd(Tokenizer tokens) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.interpretDeleteQuery();
    }

    private void interpretDeleteQuery() throws UnableToExecuteCmd {
        DbState.isDBInUse();

        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        /* check for from keyword */
        Parser.checkForKW(nextToken, "FROM");
        String tableName = this.tokens.nextToken().getValue();
        /* check if table name is a valid name */
        Parser.checkIsAValidName(tableName);
        nextToken = this.tokens.nextToken().getValue().toUpperCase();
        /* check for where keyword */
        Parser.checkForKW(nextToken, "WHERE");

        TableStructure table = new TableStructure(tableName);
        Service.loadInTable(table);
        table.convertValToLiteral();

        /*apply the conditions to the table rows*/
        ArrayList<String> conditionStatementTkns = Parser.getConditionSegment(this.tokens);
        ListIterator<String> iter = conditionStatementTkns.listIterator();
        Parser.areBracketsBalanced(conditionStatementTkns);
        Parser.isValidConditionStatement(iter);

        ConditionTruthGenerator cgt = new ConditionTruthGenerator(table, conditionStatementTkns);
        table.executeRemoveRow("DELETE");
        Service.storeTableInDB(table);
    }

}
