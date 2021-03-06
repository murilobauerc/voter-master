package br.edu.ulbra.election.voter.service;

import br.edu.ulbra.election.voter.client.ElectionClientService;
import br.edu.ulbra.election.voter.exception.CannotCreateVoterWithExistentEmail;
import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.exception.InvalidEmailException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.model.Voter;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.repository.VoterRepository;
import br.edu.ulbra.election.voter.util.ValidateVoterInput;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static br.edu.ulbra.election.voter.util.ValidateVoterInput.*;

@Service
public class VoterService {

    private final VoterRepository voterRepository;
    private final ElectionClientService electionClientService;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTER_NOT_FOUND = "Voter not found";

    @Autowired
    public VoterService(VoterRepository voterRepository, ElectionClientService electionClientService, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.voterRepository = voterRepository;
        this.electionClientService = electionClientService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<VoterOutput> getAll(){
        Type voterOutputListType = new TypeToken<List<VoterOutput>>(){}.getType();
        return modelMapper.map(voterRepository.findAll(), voterOutputListType);
    }

    public VoterOutput create(VoterInput voterInput) throws CannotCreateVoterWithExistentEmail, InvalidEmailException {
        validateInput(voterInput, false);
        validateLastVotersName(voterInput);
        if(!ValidateVoterInput.verifyValidEmail(voterInput.getEmail())){
            throw new InvalidEmailException("The email contains invalid characters.");
        }


        validateSameVotersEmail(voterInput, null, false);
        Voter voter = modelMapper.map(voterInput, Voter.class);
        voter.setPassword(passwordEncoder.encode(voter.getPassword()));
        voter = voterRepository.save(voter);
        return modelMapper.map(voter, VoterOutput.class);
    }

    public VoterOutput getById(Long voterId){
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        return modelMapper.map(voter, VoterOutput.class);
    }

    public VoterOutput update(Long voterId, VoterInput voterInput) throws CannotCreateVoterWithExistentEmail, InvalidEmailException {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(voterInput, true);
        if(!ValidateVoterInput.verifyValidEmail(voterInput.getEmail())){
            throw new InvalidEmailException("The email contains invalid characters.");
        }
        validateSameVotersEmail(voterInput, voterId, true);
        validateLastVotersName(voterInput);

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        voter.setEmail(voterInput.getEmail());
        voter.setName(voterInput.getName());
        if (!StringUtils.isBlank(voterInput.getPassword())) {
            voter.setPassword(passwordEncoder.encode(voterInput.getPassword()));
        }
        voter = voterRepository.save(voter);
        return modelMapper.map(voter, VoterOutput.class);
    }

    public GenericOutput delete(Long voterId) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        validateVotersAlreadyVote(voterId);

        voterRepository.delete(voter);

        return new GenericOutput("Voter deleted");
    }
}
