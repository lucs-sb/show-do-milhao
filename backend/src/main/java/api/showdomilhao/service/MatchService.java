package api.showdomilhao.service;

import api.showdomilhao.dto.MatchDTO;
import api.showdomilhao.entity.*;
import api.showdomilhao.exceptionHandler.exceptions.MessageNotFoundException;
import api.showdomilhao.repository.MatchRepository;
import api.showdomilhao.repository.QuestionRepository;
import api.showdomilhao.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MatchService {
    @Autowired
    private MatchRepository repository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Optional<Match> findById(Long id){
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Match> findAllByUserId(Long userId){
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Match> findByUserIdAndEnded(Long userId, boolean ended){
        return repository.findByUserIdAndEnded(userId, ended);
    }

    @Transactional
    public Long create(Match newMatch){
        Optional<UserAccount> userAccount = Optional.ofNullable(userAccountRepository.findById(newMatch.getUser().getUserId()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Set<MatchQuestion> matchQuestions = new HashSet<>();
        Set<MatchAnswer> matchAnswers = new HashSet<>();
        List<Question> questions = questionRepository.findByAcceptedForMatch();

        questions.forEach(question -> {
            matchQuestions.add(new MatchQuestion(question, questions.indexOf(question)));
            question.getAnswers().forEach(answer -> {
                matchAnswers.add(new MatchAnswer(answer, false));
            });
        });

        Match match = new Match();
        match.setUser(userAccount.get());
        match.setEnded(false);
        match.setDeletedAnswers(false);
        match.setMatchQuestions(matchQuestions);
        match.setMatchAnswers(matchAnswers);

        repository.save(match);

        return match.getMatchId();
    }

    @Transactional
    public void update(MatchDTO newMatch){
        Optional<Match> match = Optional.ofNullable(repository.findById(newMatch.matchId()).orElseThrow(() -> {
            throw new MessageNotFoundException("Partida não encontrada na base");
        }));

        match.get().setAward(newMatch.award());
        match.get().setEnded(newMatch.ended());
        match.get().setDeletedAnswers(newMatch.deletedAnswers());
        match.get().setLastQuestionAnswered(newMatch.lastQuestionAnswered());
        match.get().setReasonForClosing(newMatch.reasonForClosing());

        if (newMatch.answers() != null && !newMatch.answers().isEmpty()){
            newMatch.answers().forEach(answerId -> {
                match.get().getMatchAnswers().forEach(matchAnswer -> {
                    if (Objects.equals(answerId, matchAnswer.getAnswer().getAnswerId()))
                        matchAnswer.setDeleted(true);
                });
            });
        }

        repository.save(match.get());
    }
}
