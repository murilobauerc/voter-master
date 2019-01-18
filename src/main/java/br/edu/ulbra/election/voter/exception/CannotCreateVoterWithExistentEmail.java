package br.edu.ulbra.election.voter.exception;

public class CannotCreateVoterWithExistentEmail extends Exception{
    public CannotCreateVoterWithExistentEmail() {
        super();
    }

    public CannotCreateVoterWithExistentEmail(String message) {
        super(message);
    }
}
