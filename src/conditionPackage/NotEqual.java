package conditionPackage;

import exceptionPackage.UnableToExecuteCmd;

public class NotEqual {

    public static boolean compare(String strVal, Integer intVal, Double doubVal, Boolean boolVal, String comparisonVal)
            throws UnableToExecuteCmd {
        /*compares values to give boolean using correct data type*/
        if (strVal != null) {
            ConditionEvaluator.stopMisMatchComparison("STRING", comparisonVal);
            if (strVal.equals(comparisonVal)) {
                return false;
            }
        } else if (intVal != null) {
            ConditionEvaluator.stopMisMatchComparison("NUMBER", comparisonVal);
            if (intVal == Double.parseDouble(comparisonVal)) {
                return false;
            }
        } else if (boolVal != null) {
            ConditionEvaluator.stopMisMatchComparison("BOOLEAN", comparisonVal);
            if (boolVal == Boolean.valueOf(comparisonVal)) {
                return false;
            }
        } else {
            ConditionEvaluator.stopMisMatchComparison("NUMBER", comparisonVal);
            if (doubVal == Double.parseDouble(comparisonVal)) {
                return false;
            }
        }
        return true;
    }

}
