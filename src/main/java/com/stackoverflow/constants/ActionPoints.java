package com.stackoverflow.constants;

public enum ActionPoints {
    UPVOTE_QUESTION(5L),
    UPVOTE_ANSWER(10L),
    DOWNVOTE_AUTHOR(2L),
    DOWNVOTE_USER(1L),
    QUESTION_ASKER(2L),
    ACCEPTED_ANSWER(15L);

    private final Long points;


    ActionPoints(Long points) {
        this.points = points;
    }

    public Long getPoints() {
        return points;
    }
}
