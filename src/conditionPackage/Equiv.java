package conditionPackage;

import exceptionPackage.UnableToExecuteCmd;

public class Equiv {
    /*compares values to give boolean using correct data type*/
    public static boolean compare(String strVal, Integer intVal, Double doubVal, Boolean boolVal, String comparisonVal)
            throws UnableToExecuteCmd {
        if (strVal != null) {
            ConditionEvaluator.stopMisMatchComparison("STRING", comparisonVal);
            if (strVal.equals(comparisonVal)) {
                return true;
            }
        } else if (intVal != null) {
            ConditionEvaluator.stopMisMatchComparison("NUMBER", comparisonVal);
            if (intVal == Double.parseDouble(comparisonVal)) {
                return true;
            }
        } else if (boolVal != null) {
            ConditionEvaluator.stopMisMatchComparison("BOOLEAN", comparisonVal);
            if (boolVal == Boolean.valueOf(comparisonVal)) {
                return true;
            }
        } else {
            ConditionEvaluator.stopMisMatchComparison("NUMBER", comparisonVal);
            if (doubVal == Double.parseDouble(comparisonVal)) {
                return true;
            }
        }
        return false;
    }

}
