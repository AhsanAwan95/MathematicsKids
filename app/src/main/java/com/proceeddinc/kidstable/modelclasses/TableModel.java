package com.proceeddinc.kidstable.modelclasses;

public class TableModel {

    public String question;
    public int op_1, op_2, op_3, answer, missing_pos;

    public TableModel(int op_1, int op_2, int op_3, int answer, String question, int missing_pos) {
        this.op_1 = op_1;
        this.op_2 = op_2;
        this.op_3 = op_3;
        this.answer = answer;
        this.question = question;
        this.missing_pos = missing_pos;
    }
}
