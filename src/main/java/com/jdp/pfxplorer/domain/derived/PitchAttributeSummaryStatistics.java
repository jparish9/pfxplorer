package com.jdp.pfxplorer.domain.derived;

/**
 * Convenience class for storing summary statistics about a particular pitch attribute.
 * (API output)
 */
public class PitchAttributeSummaryStatistics {
    private double avg;
    private double min;
    private double max;
    private double stdev;

    public PitchAttributeSummaryStatistics(double avg, double stdev, double max, double min) {
        this.avg = avg;
        this.stdev = stdev;
        this.max = max;
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getStdev() {
        return stdev;
    }
}
