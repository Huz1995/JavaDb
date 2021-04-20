package exceptionPackage;

public class UnableToExecuteCmd extends ErrorException {

    Boolean hasErrMsg = false;
    String errMsg;

    public UnableToExecuteCmd() {

    };

    public UnableToExecuteCmd(String errMsg) {
        this.hasErrMsg = true;
        this.errMsg = errMsg;
    };

    @Override
    public String toString() {
        if (hasErrMsg) {
            return "[ERROR]: " + errMsg;
        }
        return "[ERROR]: Query is not acceptable";
    }

}
