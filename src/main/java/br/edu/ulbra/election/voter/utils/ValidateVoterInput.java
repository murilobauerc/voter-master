package br.edu.ulbra.election.voter.utils;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.model.Voter;
import org.apache.commons.lang.StringUtils;

public class ValidateVoterInput {
    private ValidateVoterInput(){}

    /**
     * Throw an generic exception if email, name is blank.
     * Throw an generic exception if the actual password doesn't match with password confirm.
     * @param voterInput object that can be an email, name or password.
     * @param isUpdate boolean value that verifies whether it is updated in the database or not
     * @throws GenericOutputException if the fields email,name or password goes wrong.
     */
    public static void validateInput(VoterInput voterInput, boolean isUpdate){
        if (StringUtils.isBlank(voterInput.getEmail())){
            throw new GenericOutputException("Invalid email");
        }
        if (StringUtils.isBlank(voterInput.getName())){
            throw new GenericOutputException("Invalid name");
        }
        if (!StringUtils.isBlank(voterInput.getPassword())){
            if (!voterInput.getPassword().equals(voterInput.getPasswordConfirm())){
                throw new GenericOutputException("Passwords doesn't match");
            }
        }else{
            if (!isUpdate) {
                throw new GenericOutputException("Password doesn't match");
            }
        }
    }


    /**
     * Throw an generic exception if the voter's name does not contain at least 5 letters and a last name.
     * @param voterInput  object which case it is the name to be searched.
     * @throws GenericOutputException if name goes wrong.
     */
    public static void validateLastVotersName(VoterInput voterInput){
        String[] word = voterInput.getName().trim().split(" ");
        if(word.length < 2){
           throw new GenericOutputException("The name must have at least a last name.");
        }
        if(lengthVotersName(voterInput) < 5) {
            throw new GenericOutputException("The name must have at least 5 letters.");
        }
    }

    /**
     * Get the length of the voter's name (already trimmed)
     * @param voterInput object which case it is the name to be searched.
     * @return an integer number that is the length of the voter's name
     */
    public static int lengthVotersName(VoterInput voterInput){
        return voterInput.getName().trim().length();
    }

}
