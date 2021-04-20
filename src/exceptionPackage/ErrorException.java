package exceptionPackage;

public class ErrorException extends Exception {

    public ErrorException() {
    }

    @Override
    public String toString() {
        return "[ERROR]:";
    }

}
