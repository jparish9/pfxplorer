package com.jdp.pfxplorer.dao;

import com.jdp.pfxplorer.domain.derived.PitchTypeSummary;
import com.jdp.pfxplorer.domain.derived.PitchingSummary;
import com.jdp.pfxplorer.util.PitchQueryOptionalParams;

/**
 * Custom methods for pitch that can't be handled by standard CRUD operations
 * or simple queries.
 */
public interface PitchDaoCustom {
    PitchingSummary getSummaryForPitcher(int pitcherId, PitchQueryOptionalParams params);

    PitchTypeSummary getTypeSummaryForPitcher(int pitcherId, String pitchType, PitchQueryOptionalParams params);
}
