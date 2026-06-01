package backend.exceptions;

public class TransactionTypeNotFoundException extends Exception{

    public TransactionTypeNotFoundException(String message) {
        super(message);
    }
}