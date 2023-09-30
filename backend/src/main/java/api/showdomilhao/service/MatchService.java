package api.showdomilhao.service;

import api.showdomilhao.entity.Match;
import api.showdomilhao.entity.MatchQuestion;
import api.showdomilhao.entity.Question;
import api.showdomilhao.entity.UserAccount;
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
    public void create(Match newMatch){
        Optional<UserAccount> userAccount = Optional.ofNullable(userAccountRepository.findById(newMatch.getUserAccountId()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Set<MatchQuestion> matchQuestions = new HashSet<>();
        List<Question> questions = questionRepository.findByAcceptedForMatch();

        questions.forEach(x -> {
            matchQuestions.add(new MatchQuestion(x.getQuestionId(), questions.indexOf(x)));
        });

        Match match = new Match();
        match.setUserAccountId(userAccount.get().getUserAccountId());
        match.setEnded(false);
        match.setDeletedAnswers(false);
        match.setQuestions(matchQuestions);

        repository.save(match);
    }

    @Transactional
    public void update(Match newMatch){
        Optional<Match> match = Optional.ofNullable(repository.findById(newMatch.getMatchId()).orElseThrow(() -> {
            throw new MessageNotFoundException("Partida não encontrada na base");
        }));

        match.get().setAward(newMatch.getAward());
        match.get().setEnded(newMatch.isEnded());
        match.get().setDeletedAnswers(newMatch.isDeletedAnswers());
        match.get().setLastQuestionAnswered(newMatch.getLastQuestionAnswered());
        match.get().setReasonForClosing(newMatch.getReasonForClosing());

        repository.save(match.get());
    }
}
