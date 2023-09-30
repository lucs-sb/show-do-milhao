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
    public Optional<Question> findQuestionById(Long questionId){
        return repository.findById(questionId);
    }

    @Transactional(readOnly = true)
    public List<Question> findQuestionsToApprovals(Long userId) {
        return repository.findQuestionsToApprovals(userId);
    }

    @Transactional
    public void addQuestion(QuestionDTO newQuestion) {
        if (newQuestion.getAnswers().size() != 4)
            throw new MessageBadRequestException("A pergunta deve conter 4 respostas");

        Optional<UserAccount> userAccount = Optional.ofNullable(userAccountRepository.findById(newQuestion.getUserAccountId()).orElseThrow(() -> {
            throw new MessageNotFoundException("Usuário não encontrado");
        }));

        Set<QuestionAnswer> answers = new HashSet<>();
        newQuestion.getAnswers().forEach(x -> {
            Answer answer = new Answer();
            answer.setDescription(x.getDescription());
            answer.setCorrect(x.isCorrect());
            answerRepository.save(answer);
            answers.add(new QuestionAnswer(answer.getAnswerId()));
        });

        Question question = new Question();
        question.setUserAccountId(userAccount.get().getUserAccountId());
        question.setStatement(newQuestion.getStatement());
        question.setAmountApprovals(0);
        question.setAmountComplaints(0);
        question.setAmountFailures(0);
        question.setAccepted(false);
        question.setAnswers(answers);
        repository.save(question);

        usersToValidateQuestion(question.getQuestionId(), question.getUserAccountId());
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

        usersToValidateQuestion(question.get().getQuestionId(), question.get().getUserAccountId());
    }

    @Transactional
    public void deleteQuestion(Long questionId){
        Optional<Question> question = Optional.ofNullable(repository.findById(questionId)
                .orElseThrow(() -> {
                    throw new MessageNotFoundException("Pergunta não encontrada na base");
                }));
        question.get().setDeletionDate(LocalDateTime.now());
        repository.save(question.get());
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

            usersToValidateQuestion(question.get().getQuestionId(), question.get().getUserAccountId());
        }

        repository.save(question.get());
    }

    @Transactional
    public void approveQuestion(Long questionId, Long userId, boolean approve){
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

        user.get().getValidatedQuestions().add(new ValidatedQuestionsUser(question.get().getQuestionId()));

        for (ValidationQuestionUser q : user.get().getValidationQuestions()) {
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
        users.forEach(x -> {
            x.getValidationQuestions().stream().filter(q -> Objects.equals(q.getQuestionId(), questionId)).forEach(q -> {
                x.getValidationQuestions().remove(q);
            });
            userAccountRepository.save(x);
        });
        users = userAccountRepository.findUsersToValidateQuestion(userId);
        users.forEach(x -> {
            x.getValidationQuestions().add(new ValidationQuestionUser(questionId));
            userAccountRepository.save(x);
        });
    }
}
