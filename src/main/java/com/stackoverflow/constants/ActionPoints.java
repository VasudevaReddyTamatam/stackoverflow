package com.stackoverflow.constants;

public enum ActionPoints {
    UPVOTE_QUESTION(5),
    UPVOTE_ANSWER(10),
    DOWNVOTE_AUTHOR(2),
    DOWNVOTE_USER(1),
    QUESTION_ASKER(2),
    ACCEPTED_ANSWER(15);

    private final int points;


    ActionPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
