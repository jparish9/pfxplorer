package com.jdp.pfxplorer.domain.derived;

import java.util.LinkedHashMap;

/**
 * Convenience class for storing pitch summary data.
 * (API output)
 */
public class PitchingSummary {
    private int games;

    private LinkedHashMap<String, PitchCounts> pitchCountByType = new LinkedHashMap<>();

    private PitchCounts pitchCount;

    public PitchCounts getPitchCount() {
        return pitchCount;
    }

    public void setPitchCount(PitchCounts pitchCount) {
        this.pitchCount = pitchCount;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public LinkedHashMap<String, PitchCounts> getPitchCountByType() {
        return pitchCountByType;
    }

    public void setPitchCountByType(LinkedHashMap<String, PitchCounts> pitchCountByType) {
        this.pitchCountByType = pitchCountByType;
    }
}
