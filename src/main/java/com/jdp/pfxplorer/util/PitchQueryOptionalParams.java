package com.jdp.pfxplorer.util;

public class PitchQueryOptionalParams {
    public PitchQueryOptionalParams(PitchCount pitchCount, Integer year, Integer month, Integer inning) {
        this.pitchCount = pitchCount;
        this.year = year;
        this.month = month;
        this.inning = inning;
    }

    private PitchCount pitchCount;
    private Integer year;
    private Integer month;
    private Integer inning;

    public PitchCount getPitchCount() {
        return pitchCount;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getInning() {
        return inning;
    }

    public Integer getMonth() {
        return month;
    }
}
