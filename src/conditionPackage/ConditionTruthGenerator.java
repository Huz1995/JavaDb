package conditionPackage;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import exceptionPackage.UnableToExecuteCmd;
import tokenPackage.FormalGrammar.RegExp;
import servicePackage.TableStructure;

public class ConditionTruthGenerator {

    private ArrayList<String> commandList;
    private TableStructure table;

    public ConditionTruthGenerator(TableStructure table, ArrayList<String> commandList) throws UnableToExecuteCmd {
        this.commandList = commandList;
        this.table = table;
        this.markTableRowsTruthy();
    }

    /*for each row check if it is true or false as per conditions*/
    private void markTableRowsTruthy() throws UnableToExecuteCmd {
        ArrayList<ArrayList<Object>> litTable = this.table.getLitValList();
        if(litTable.size()==1) {
            throw new UnableToExecuteCmd("Table has no attributes");
        }else{
            for (int i = 1; i < litTable.size(); i++) {
                boolean isRowTruthy = this.getTruthyExpFromRow(litTable.get(i));
                this.table.addTruthyToRow(isRowTruthy);
            }
        }
    }

    private boolean getTruthyExpFromRow(ArrayList<Object> literalRow) throws UnableToExecuteCmd {
        ListIterator<String> iter = this.commandList.listIterator();
        StringBuilder builder = new StringBuilder();
        while (iter.hasNext()) {
            String value = iter.next();

            /*if expression is a name send next tokens to evalute boolean*/
            if (RegExp.OBJECTNAME.doesTokenMatch(value)) {
                /*get index pos of attribute to get value in row*/
                int attributeIndex = table.getAttributePos(value, "allow_id");
                Object literal = literalRow.get(attributeIndex);
                boolean isTruthy = new ConditionEvaluator(literal, iter.next(), iter.next()).isRowTrue();
                builder.append(String.valueOf(isTruthy).toLowerCase());
                builder.append(" ");
            } else if (value.equals(";")) {
                continue;
            } else {
                builder.append(value);
                builder.append(" ");
            }
        }
        return this.spiltBooleanCmdString(builder.toString());
    }

    /*function to mould boolean statement for evaluation*/
    private boolean spiltBooleanCmdString(String boolExp) {

        ArrayList<String> booleanCommand = new ArrayList<String>();
        boolExp = boolExp.replace("( false )", "false");
        boolExp = boolExp.replace("( true )", "true");
        String[] tempHolder = boolExp.split(" ");
        booleanCommand.add("(");
        for (String symbl : tempHolder) {
            booleanCommand.add(symbl);
        }
        booleanCommand.add(")");
        return this.condenseBoolCmdArr(booleanCommand);

    }

    /*walk through array appending to stack to get the final value for row*/
    private boolean condenseBoolCmdArr(ArrayList<String> booleanCommand) {
        Stack<String> parenStack = new Stack<String>();
        Stack<String> opStack = new Stack<String>();
        Stack<Boolean> resStack = new Stack<Boolean>();
        /*if len is 3 then no and/or so take single boolean value*/
        if (booleanCommand.size() == 3) {
            resStack.push(Boolean.valueOf(booleanCommand.get(1)));
        } else {
            for (String symbl : booleanCommand) {
                if (symbl.equals("(")) {
                    parenStack.push(symbl);
                } else if (symbl.toLowerCase().equals("false") || symbl.toLowerCase().equals("true")) {
                    resStack.push(Boolean.valueOf(symbl));
                } else if (symbl.toUpperCase().equals("AND") || symbl.toUpperCase().equals("OR")) {
                    opStack.push(symbl);
                } else {
                    /*if the smbl is ) then calc boolean in that bracket*/
                    parenStack.pop();
                    String op = opStack.pop();
                    Boolean bool1 = resStack.pop();
                    Boolean bool2 = resStack.pop();
                    Boolean results = calcBoolean(bool1, bool2, op);
                    resStack.push(results);
                }
            }
        }
        return resStack.pop();
    }

    private boolean calcBoolean(Boolean bool1, Boolean bool2, String op) {
        if (op.toUpperCase().equals("OR")) {
            return bool1 || bool2;
        }
        return bool1 && bool2;
    }
}
