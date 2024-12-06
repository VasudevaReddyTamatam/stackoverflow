package com.stackoverflow.constants;

public enum ActionPoints {
    UPVOTE_QUESTION(5),
    UPVOTE_ANSWER(10),
    DOWNVOTE(2),
    QUESTION_Asker(2),
    ACCEPTED_ANSWER(15);

    private final int points;


    ActionPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
