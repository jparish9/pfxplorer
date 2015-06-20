package com.jdp.pfxplorer.util;

public class PitchCount {
    private Integer balls;
    private Integer strikes;

    public PitchCount(Integer balls, Integer strikes) {
        this.balls = balls;
        this.strikes = strikes;
    }

    /**
     * Parse a string pitch count in the form balls-strikes, e.g. "0-2"
     * @return PitchCount object with balls and strikes set, or nulls if parsing fails.
     */
    public static PitchCount parsePitchCount(String count) {
        Integer balls = null;
        Integer strikes = null;
        if (count != null) {
            try {
                String[] parts = count.split("-");
                if (parts.length == 2) {
                    balls = Integer.parseInt(parts[0]);
                    strikes = Integer.parseInt(parts[1]);
                }
            } catch (NumberFormatException e) {}
        }

        return new PitchCount(balls, strikes);
    }

    /**
     * Given optional url parameters "count", "balls", "strikes", parse a pitch count from them.
     * If "count" is present, use that, otherwise use explicit "balls" and "strikes".
     * @return PitchCount object with balls and strikes set, or nulls if parsing fails.
     */
    public static PitchCount parsePitchCountFromOptionalQueryParameters(String count, Integer balls, Integer strikes) {
        if (count != null) {
            return parsePitchCount(count);
        } else {
            return new PitchCount(balls, strikes);
        }
    }

    public Integer getBalls() {
        return balls;
    }

    public Integer getStrikes() {
        return strikes;
    }
}
