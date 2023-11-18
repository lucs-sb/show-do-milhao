package api.showdomilhao.dto;

import java.util.List;

public record MatchDTO(long matchId, long userAccountId, boolean ended, int lastQuestionAnswered, boolean deletedAnswers, int award, String reasonForClosing, long questionId, List<Long> answers) {
}
