package exceptionPackage;

public class UnableToTokenizeException extends ErrorException {

    public UnableToTokenizeException() {

    };

    @Override
    public String toString() {
        return "[ERROR]: Unable to tokenize string, invalid command";
    }

}
