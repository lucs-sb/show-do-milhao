import { Answer } from "./answer";

export interface Question{
    questionId: number;
    userAccountId: number;
    statement: string;
    accepted: boolean;
    amountApprovals: number;
    mountFailures: number;
    amountComplaints: number;
    deletionDate: Date;
    answers: Answer[];
}