package com.jdp.pfxplorer.domain.derived;

/**
 * Convenience class for storing data about all pitches of a particular type.
 * (API output)
 */
public class PitchTypeSummary {
    private PitchCounts pitchCounts;

    private PitchAttributeSummaryStatistics velocity;
    private PitchAttributeSummaryStatistics spinRate;
    private PitchAttributeSummaryStatistics spinDir;
    private PitchAttributeSummaryStatistics horizontalMovement;
    private PitchAttributeSummaryStatistics verticalMovement;

    public PitchCounts getPitchCounts() {
        return pitchCounts;
    }

    public void setPitchCounts(PitchCounts pitchCounts) {
        this.pitchCounts = pitchCounts;
    }

    public PitchAttributeSummaryStatistics getVelocity() {
        return velocity;
    }

    public void setVelocity(PitchAttributeSummaryStatistics velocity) {
        this.velocity = velocity;
    }

    public PitchAttributeSummaryStatistics getSpinRate() {
        return spinRate;
    }

    public void setSpinRate(PitchAttributeSummaryStatistics spinRate) {
        this.spinRate = spinRate;
    }

    public PitchAttributeSummaryStatistics getSpinDir() {
        return spinDir;
    }

    public void setSpinDir(PitchAttributeSummaryStatistics spinDir) {
        this.spinDir = spinDir;
    }

    public PitchAttributeSummaryStatistics getHorizontalMovement() {
        return horizontalMovement;
    }

    public void setHorizontalMovement(PitchAttributeSummaryStatistics horizontalMovement) {
        this.horizontalMovement = horizontalMovement;
    }

    public PitchAttributeSummaryStatistics getVerticalMovement() {
        return verticalMovement;
    }

    public void setVerticalMovement(PitchAttributeSummaryStatistics verticalMovement) {
        this.verticalMovement = verticalMovement;
    }
}
