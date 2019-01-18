package br.edu.ulbra.election.voter.exception;

public class InvalidEmailException extends Exception{
    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String message) {
        super(message);
    }
}
