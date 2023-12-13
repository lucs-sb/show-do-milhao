import { MatchAnswers } from "./matchAnswers";
import { MatchQuestion } from "./matchQuestion";

export interface Match{
    matchId: number;
    award: number;
    ended: boolean;
    lastQuestionAnswered: number;
    deletedAnswers: boolean;
    reasonForClosing: string;
    matchQuestions: MatchQuestion[];
    matchAnswers: MatchAnswers[];
}