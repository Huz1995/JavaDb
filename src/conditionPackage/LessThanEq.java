package conditionPackage;

import exceptionPackage.UnableToExecuteCmd;

public class LessThanEq {

    public static boolean compare(String strVal, Integer intVal, Double doubVal, Boolean boolVal, String comparisonVal)
            throws UnableToExecuteCmd {
        /*compares values to give boolean using correct data type*/
        if (strVal != null || boolVal != null) {
            throw new UnableToExecuteCmd("Cannot compute expression");
        } else if (intVal != null) {
            ConditionEvaluator.stopMisMatchComparison("NUMBER", comparisonVal);
            if (intVal <= Double.parseDouble(comparisonVal)) {
                return true;
            }
        } else {
            ConditionEvaluator.stopMisMatchComparison("NUMBER", comparisonVal);
            if (doubVal <= Double.parseDouble(comparisonVal)) {
                return true;
            }
        }
        return false;
    }

}
