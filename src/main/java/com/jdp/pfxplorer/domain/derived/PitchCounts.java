package com.jdp.pfxplorer.domain.derived;

/**
 * Convenience class for storing pitch counts, either overall or for a specific pitch type.
 * (API output)
 */
public class PitchCounts {
    private int total;
    private int strikes;
    private int balls;
    private int inPlay;

    public PitchCounts(int total, int strikes, int balls, int inPlay) {
        this.total = total;
        this.strikes = strikes;
        this.balls = balls;
        this.inPlay = inPlay;
    }

    public int getTotal() {
        return total;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getInPlay() {
        return inPlay;
    }

    public void setInPlay(int inPlay) {
        this.inPlay = inPlay;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }
}
