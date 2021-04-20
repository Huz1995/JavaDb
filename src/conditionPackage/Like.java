package conditionPackage;

import exceptionPackage.UnableToExecuteCmd;

public class Like {

    public static boolean compare(String strVal, Integer intVal, Double doubVal, Boolean boolVal, String comparisonVal)
            throws UnableToExecuteCmd {
        /*compares values to give boolean using correct data type*/
        if (strVal != null) {
            ConditionEvaluator.stopMisMatchComparison("STRING", comparisonVal);
            comparisonVal = comparisonVal.replace("'", "");
            if (strVal.contains(comparisonVal)) {
                return true;
            }
        } else if (intVal != null || boolVal != null || doubVal != null) {
            throw new UnableToExecuteCmd("Cannot use like operator without string value");
        }
        return false;
    }

}
