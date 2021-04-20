package conditionPackage;

import exceptionPackage.UnableToExecuteCmd;
import tokenPackage.FormalGrammar.RegExp;

public class ConditionEvaluator {

    String operand;
    Integer intVal;
    Double doubleVal;
    String strVal;
    Boolean boolVal;
    String cmpreVal;

    /*take in operand, value rom client and value in row*/
    public ConditionEvaluator(Object valInTble, String operand, String cmpreVal) {
        this.operand = operand;
        this.cmpreVal = cmpreVal;
        this.getLiteralValue(valInTble);
    }

    /*get datatype of value in databse*/
    private void getLiteralValue(Object valInTble) {
        if (valInTble instanceof Integer) {
            intVal = Integer.parseInt(valInTble.toString());
        } else if (valInTble instanceof Double) {
            doubleVal = Double.parseDouble(valInTble.toString());
        } else if (valInTble instanceof Boolean) {
            boolVal = Boolean.valueOf(valInTble.toString());
        } else {
            strVal = valInTble.toString();
        }
    }

    /*send to compare classes*/
    public boolean isRowTrue() throws UnableToExecuteCmd {
        if (this.operand.equals("@")) {
            return Equiv.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        } else if (this.operand.equals(">")) {
            return GreaterThan.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        } else if (this.operand.equals("}")) {
            return GreaterThanEq.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        } else if (this.operand.equals("<")) {
            return LessThan.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        } else if (this.operand.equals("{")) {
            return LessThanEq.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        } else if (this.operand.equals("!")) {
            return NotEqual.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        } else if (this.operand.toUpperCase().equals("LIKE")) {
            return Like.compare(strVal, intVal, doubleVal, boolVal, cmpreVal);
        }
        throw new UnableToExecuteCmd("Entered an invalid operator");
    }

    /*this function makes sure cross comparing between strings, numbers and boolean doesnt happen*/
    public static void stopMisMatchComparison(String litType, String cmpreVal) throws UnableToExecuteCmd {
        if (litType.equals("STRING")) {
            if (RegExp.BOOLLIT.doesTokenMatch(cmpreVal) || RegExp.INTLIT.doesTokenMatch(cmpreVal)
                    || RegExp.FLOATLIT.doesTokenMatch(cmpreVal)) {
                throw new UnableToExecuteCmd("Expression is invalid");
            }
        } else if (litType.equals("BOOLEAN")) {
            if (RegExp.STRGLIT.doesTokenMatch(cmpreVal) || RegExp.INTLIT.doesTokenMatch(cmpreVal)
                    || RegExp.FLOATLIT.doesTokenMatch(cmpreVal)) {
                throw new UnableToExecuteCmd("Expression is invalid");
            }
        } else if (litType.equals("NUMBER")) {
            if (RegExp.BOOLLIT.doesTokenMatch(cmpreVal) || RegExp.STRGLIT.doesTokenMatch(cmpreVal)) {
                throw new UnableToExecuteCmd("Expression is invalid");
            }
        }

    }
}
