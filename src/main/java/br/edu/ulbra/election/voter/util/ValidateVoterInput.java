package br.edu.ulbra.election.voter.util;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if(trimInsideOut(voterInput.getName()).split(" ").length < 2){
           throw new GenericOutputException("The name must have at least a last name.");
        }
        if(trimInsideOut(voterInput.getName()).length() < 5) {
            throw new GenericOutputException("The name must have at least 5 letters.");
        }
    }

    /**
     * Removes blank spaces in the beginning and the end
     * Replaces multiple blank spaces,if exists, to a single one
     *
     * @param word any string
     * @return trimmed string and treated at all
     */
    public static String trimInsideOut(String word){
        word = word.trim();
        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(word);
        word = matcher.replaceAll(" ");

        return word;
    }
}
