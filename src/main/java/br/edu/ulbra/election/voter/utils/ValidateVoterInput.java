package br.edu.ulbra.election.voter.utils;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import org.apache.commons.lang.StringUtils;

public class ValidateVoterInput {
    private ValidateVoterInput(){}

    /**
     * Throw an generic exception if email, name or password is blank.
     * Throw an generic exception if the actual password doesn't match with password confirm.
     * @param voterInput can be a email, name or password.
     * @param isUpdate boolean value that verifies whether it is updated in the database or not
     * @throws GenericOutputException if the fields state code, description or year goes wrong.
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
}
