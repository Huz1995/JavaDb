package interpreterPackage;

import java.util.ArrayList;

import conditionPackage.ConditionEvaluator;
import exceptionPackage.UnableToExecuteCmd;
import parserPackage.DbState;
import parserPackage.Parser;
import servicePackage.Service;
import servicePackage.TableStructure;
import tokenPackage.Tokenizer;

public class JoinCmd {

    Tokenizer tokens;
    DbState response;

    public JoinCmd(Tokenizer tokens, DbState response) throws UnableToExecuteCmd {
        this.tokens = tokens;
        this.response = response;
        this.interpretJoinCmd();
    }

    private void interpretJoinCmd() throws UnableToExecuteCmd {
        DbState.isDBInUse();
        /* get table names and joing attributes from query */
        String tableName1 = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(tableName1);
        String nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkForKW(nextToken, "AND");
        String tableName2 = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(tableName2);
        nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkForKW(nextToken, "ON");
        String attName1 = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(attName1);
        nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkForKW(nextToken, "AND");
        String attName2 = this.tokens.nextToken().getValue();
        Parser.checkIsAValidName(attName2);
        nextToken = this.tokens.nextToken().getValue().toUpperCase();
        Parser.checkIfSemiColon("FINAL", nextToken);

        this.createJoinTable(tableName1, tableName2, attName1, attName2);

    }

    private void createJoinTable(String tableName1, String tableName2, String attName1, String attName2)
            throws UnableToExecuteCmd {
        /* load in both tables */
        TableStructure table1 = new TableStructure(tableName1);
        Service.loadInTable(table1);
        table1.convertValToLiteral();

        TableStructure table2 = new TableStructure(tableName2);
        Service.loadInTable(table2);
        table2.convertValToLiteral();

        /* create new table for joined table */
        TableStructure joinedTable = new TableStructure("join");
        joinedTable.addLiteralRow(this.generateJoinAttTitle(table1, table2));
        this.generateJoinRows(table1, table2, joinedTable, attName1, attName2);
        joinedTable.convertLitToVal();
        response.setStringTable(joinedTable.getStringTable());
        response.buildResponse();

    }

    private ArrayList<Object> generateJoinAttTitle(TableStructure table1, TableStructure table2) {
        ArrayList<Object> titles = new ArrayList<Object>();
        titles.add("id");

        for (int i = 1; i < table1.getLitValList().get(0).size(); i++) {
            titles.add(table1.getTableName() + "." + table1.getLitValList().get(0).get(i));
        }

        for (int i = 1; i < table2.getLitValList().get(0).size(); i++) {
            titles.add(table2.getTableName() + "." + table2.getLitValList().get(0).get(i));
        }
        return titles;
    }

    private void generateJoinRows(TableStructure table1, TableStructure table2, TableStructure joinTable,
            String attName1, String attName2) throws UnableToExecuteCmd {
        /*get the index of specifed joing attributes*/
        int attIndx1 = table1.getAttributePos(attName1, "allow_id");
        int attIndx2 = table2.getAttributePos(attName2, "allow_id");
        /*get the literal tables from both table structures*/
        ArrayList<ArrayList<Object>> litValList1 = table1.getLitValList();
        ArrayList<ArrayList<Object>> litValList2 = table2.getLitValList();
        int id = 0;
        /*for each row,compare with other row in table at attribute values*/
        for (int i = 1; i < litValList1.size(); i++) {
            for (int j = 1; j < litValList2.size(); j++) {
                ArrayList<Object> row1 = litValList1.get(i);
                ArrayList<Object> row2 = litValList2.get(j);
                Object litVal1 = row1.get(attIndx1);
                String litVal2 = row2.get(attIndx2).toString();
                boolean areValsEql = new ConditionEvaluator(litVal1, "@", litVal2).isRowTrue();
                /*if eql create a new row and add to joining table struct*/
                if (areValsEql) {
                    ArrayList<Object> newRow = new ArrayList<Object>();
                    newRow.add(id++); newRow.addAll(row1);
                    newRow.addAll(row2); newRow.remove(0);
                    newRow.remove(row1.size());
                    joinTable.addLiteralRow(newRow);
                }
            }
        }
    }
}
