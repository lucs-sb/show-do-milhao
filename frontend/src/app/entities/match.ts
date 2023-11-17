import { MatchQuestion } from "./matchQuestion";

export interface Match{
    matchId: number;
    userAccountId: number;
    award: number;
    ended: boolean;
    lastQuestionAnswered: number;
    deletedAnswers: boolean;
    reasonForClosing: string;
    questions: MatchQuestion[];
}