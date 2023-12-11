package api.showdomilhao.service;

import api.showdomilhao.dto.QuestionDTO;
import api.showdomilhao.entity.*;
import api.showdomilhao.exceptionHandler.exceptions.MessageBadRequestException;
import api.showdomilhao.exceptionHandler.exceptions.MessageNotFoundException;
import api.showdomilhao.repository.AnswerRepository;
import api.showdomilhao.repository.QuestionRepository;
import api.showdomilhao.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository repository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<Question> findQuestionsByUserIdAndAccepted(Long userId, boolean accepted){
        return repository.findQuestionsByUserIdAndAccepted(userId, accepted);
    }

    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findQuestionByIdAndMatchId(Long matchId, Long questionId){
        Optional<Question> question = repository.findById(questionId);
        Set<Answer> answers = answerRepository.findAnswersByMatchIdAndQuestionId(matchId, question.get().getQuestionId());
        return Optional.of(new QuestionDTO(question.get(), answers));
    }

    @Transactional(readOnly = true)
    public List<Question> findQuestionsToApprovals(Long userId) {
        return repository.findQuestionsToApprovals(userId);
    }

    @Transactional
    public void addQuestion(QuestionDTO newQuestion) {
        if (newQuestion.getAnswers().size() != 4)
            throw new MessageBadRequestException("A pergunta deve conter 4 respostas");

        int count = 0;

        for (Answer answer : newQuestion.getAnswers()){
            if (answer.isCorrect())
                count++;
        };

        if (count != 1)
            throw new MessageBadRequestException("A pergunta deve conter 1 resposta correta");

        Optional<UserAccount> userAccount = Optional.ofNullable(userAccountRepository.findById(newQuestion.getUserAccountId()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Set<Answer> answers = new HashSet<>();
        newQuestion.getAnswers().forEach(x -> {
            Answer answer = new Answer();
            answer.setDescription(x.getDescription());
            answer.setCorrect(x.isCorrect());
            answerRepository.save(answer);
            answers.add(answer);
        });

        Question question = new Question();
        question.setUser(userAccount.get());
        question.setStatement(newQuestion.getStatement());
        question.setAmountApprovals(0);
        question.setAmountComplaints(0);
        question.setAmountFailures(0);
        question.setAccepted(false);
        question.setAnswers(answers);
        repository.save(question);

        usersToValidateQuestion(question.getQuestionId(), question.getUser().getUserId());
    }

    @Transactional
    public void updateQuestion(QuestionDTO newQuestion) {
        newQuestion.getAnswers().forEach(x -> {
            Optional<Answer> answer = Optional.ofNullable(answerRepository.findById(x.getAnswerId())
                    .orElseThrow(() -> {
                        throw new MessageNotFoundException("Resposta da pergunta não encontrada na base");
                    }));
            answer.get().setDescription(x.getDescription());
            answer.get().setCorrect(x.isCorrect());
            answerRepository.save(answer.get());
        });

        Optional<Question> question = Optional.ofNullable(repository.findById(newQuestion.getQuestionId())
                .orElseThrow(() -> {
                    throw new MessageNotFoundException("Pergunta não encontrada na base");
                }));
        question.get().setStatement(newQuestion.getStatement());
        question.get().setAmountApprovals(0);
        question.get().setAmountFailures(0);
        question.get().setAmountComplaints(0);
        question.get().setAccepted(false);
        repository.save(question.get());

        usersToValidateQuestion(question.get().getQuestionId(), question.get().getUser().getUserId());
    }

    @Transactional
    public void deleteQuestion(Long questionId){
        Optional<Question> question = Optional.ofNullable(repository.findById(questionId)
                .orElseThrow(() -> new MessageNotFoundException("Pergunta não encontrada na base")));

        Set<Answer> answers = answerRepository.findAnswersByQuestionId(questionId);

        repository.deleteQuestionInValidation(questionId);
        repository.deleteQuestionValidated(questionId);
        repository.deleteQuestionInTheMatch(questionId);
        repository.delete(question.get());

        answers.forEach(answer -> answerRepository.delete(answer));
    }

    @Transactional
    public void reportQuestion(Long questionId){
        Optional<Question> question = Optional.ofNullable(repository.findById(questionId)
                .orElseThrow(() -> {
                    throw new MessageNotFoundException("Pergunta não encontrada na base");
                }));

        int reports = question.get().getAmountComplaints() + 1;

        if (reports < 2)
            question.get().setAmountComplaints(reports);
        else{
            question.get().setAmountApprovals(0);
            question.get().setAmountFailures(0);
            question.get().setAmountComplaints(0);
            question.get().setAccepted(false);

            usersToValidateQuestion(question.get().getQuestionId(), question.get().getUser().getUserId());
        }

        repository.save(question.get());
    }

    @Transactional
    public void validateQuestion(Long questionId, Long userId, boolean approve){
        Optional<Question> question = Optional.ofNullable(repository.findByIdAndUserId(questionId, userId)
                .orElseThrow(() -> {
                    throw new MessageNotFoundException("Pergunta não esta na lista de perguntas para aprovações desse usuário");
                }));

        int approvals = 0;
        int failures = 0;

        if (approve)
            approvals = question.get().getAmountApprovals() + 1;
        else
            failures = question.get().getAmountFailures() + 1;

        int amountAnswers = Math.abs(question.get().getAmountApprovals() - question.get().getAmountFailures());

        if (amountAnswers < 5){
            if (approve)
                question.get().setAmountApprovals(approvals);
            else
                question.get().setAmountFailures(failures);
        }

        if (question.get().getAmountApprovals() == 5) {
            question.get().setAmountApprovals(approvals);
            question.get().setAccepted(true);
        }

        Optional<UserAccount> user = Optional.ofNullable(userAccountRepository.findById(userId).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        user.get().getValidatedQuestions().add(question.get());

        for (Question q : user.get().getValidationQuestions()) {
            if (q.getQuestionId().equals(question.get().getQuestionId())) {
                user.get().getValidationQuestions().remove(q);
                break;
            }
        }

        userAccountRepository.save(user.get());
        repository.save(question.get());
    }

    @Transactional
    private void usersToValidateQuestion(Long questionId, Long userId){
        List<UserAccount> users = userAccountRepository.findUsersByQuestionId(questionId);
        Optional<Question> question = repository.findById(questionId);
        users.forEach(x -> {
            x.getValidationQuestions().stream().filter(q -> Objects.equals(q.getQuestionId(), questionId)).forEach(q -> {
                x.getValidationQuestions().remove(q);
            });
            userAccountRepository.save(x);
        });
        users = userAccountRepository.findUsersToValidateQuestion(userId);
        users.forEach(x -> {
            x.getValidationQuestions().add(question.get());
            userAccountRepository.save(x);
        });
    }
}
