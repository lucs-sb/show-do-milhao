package api.showdomilhao.service;

import api.showdomilhao.dto.MatchDTO;
import api.showdomilhao.entity.*;
import api.showdomilhao.exceptionHandler.exceptions.MessageNotFoundException;
import api.showdomilhao.repository.*;
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
    @Autowired
    private MatchQuestionRepository matchQuestionRepository;
    @Autowired
    private MatchAnswerRepository matchAnswerRepository;

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
    public Long create(Long userId){
        Optional<UserAccount> userAccount = Optional.ofNullable(userAccountRepository.findById(userId).orElseThrow(() -> new MessageNotFoundException("Usuário não encontrado")));

        Match match = new Match();
        match.setUser(userAccount.get());
        match.setEnded(false);
        match.setDeletedAnswers(false);
        repository.save(match);

        Set<MatchQuestion> matchQuestions = new HashSet<>();
        Set<MatchAnswer> matchAnswers = new HashSet<>();
        List<Question> questions = questionRepository.findByAcceptedForMatch();

        questions.forEach(question -> {
            MatchQuestion matchQuestion = new MatchQuestion();
            matchQuestion.setMatch(match);
            matchQuestion.setQuestionId(question.getQuestionId());
            matchQuestion.setPosition(questions.indexOf(question));
            matchQuestionRepository.save(matchQuestion);

            matchQuestions.add(matchQuestion);

            question.getAnswers().forEach(answer -> {
                MatchAnswer matchAnswer = new MatchAnswer();
                matchAnswer.setMatch(match);
                matchAnswer.setAnswerId(answer.getAnswerId());
                matchAnswer.setDeleted(false);
                matchAnswerRepository.save(matchAnswer);

                matchAnswers.add(matchAnswer);
            });
        });

        match.setMatchQuestions(matchQuestions);
        match.setMatchAnswers(matchAnswers);
        repository.save(match);

        return match.getMatchId();
    }

    @Transactional
    public void update(MatchDTO newMatch){
        Optional<Match> match = Optional.ofNullable(repository.findById(newMatch.matchId()).orElseThrow(() -> new MessageNotFoundException("Partida não encontrada na base")));

        match.get().setAward(newMatch.award());
        match.get().setEnded(newMatch.ended());
        match.get().setDeletedAnswers(newMatch.deletedAnswers());
        match.get().setLastQuestionAnswered(newMatch.lastQuestionAnswered());
        match.get().setReasonForClosing(newMatch.reasonForClosing());

        if (newMatch.answers() != null && !newMatch.answers().isEmpty()){
            newMatch.answers().forEach(answerId -> {
                match.get().getMatchAnswers().forEach(matchAnswer -> {
                    if (Objects.equals(answerId, matchAnswer.getAnswerId())) {
                        matchAnswer.setDeleted(true);
                        matchAnswerRepository.save(matchAnswer);
                    }
                });
            });
        }

        repository.save(match.get());

        if (match.get().isEnded()){
            Optional<UserAccount> user = userAccountRepository.findById(match.get().getUser().getUserId());
            Long totalAward = user.get().getTotalAward() + match.get().getAward();
            user.get().setTotalAward(totalAward);
            userAccountRepository.save(user.get());
        }

    }
}
