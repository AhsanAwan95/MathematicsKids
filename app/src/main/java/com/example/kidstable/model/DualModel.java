package com.example.kidstable.model;

public class DualModel {

    public String question;
    public int missing_pos;
    public String op_1, op_2, op_3, answer;

    public DualModel(String op_1, String op_2, String op_3, String answer, String question, int missing_pos) {
        this.op_1 = op_1;
        this.op_2 = op_2;
        this.op_3 = op_3;
        this.answer = answer;
        this.question = question;
        this.missing_pos = missing_pos;
    }
}
