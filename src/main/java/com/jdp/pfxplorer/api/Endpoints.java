package com.jdp.pfxplorer.api;

import com.jdp.pfxplorer.dao.PitchDao;
import com.jdp.pfxplorer.dao.PitcherDao;
import com.jdp.pfxplorer.domain.Pitcher;
import com.jdp.pfxplorer.domain.derived.PitchTypeSummary;
import com.jdp.pfxplorer.domain.derived.PitchingSummary;
import com.jdp.pfxplorer.util.PitchCount;
import com.jdp.pfxplorer.util.PitchQueryOptionalParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Define and handle the endpoints that are available for this API.
 */
@RestController
@RequestMapping("/pitchers")
public class Endpoints {
    @Autowired
    private PitchDao pitchDao;

    @Autowired
    private PitcherDao pitcherDao;

    /**
     * Base query to return pitch count breakdown for all types.
     * Optional extra filter parameters (via URL query parameters): balls, strikes, year, month, inning.
     */
    @RequestMapping("/{pitcherId}")
    public PitchingSummary getSummary(@PathVariable Integer pitcherId,
                                      @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month,
                                      @RequestParam(required = false) String count,
                                      @RequestParam(required = false) Integer balls, @RequestParam(required = false) Integer strikes,
                                      @RequestParam(required = false) Integer inning) {
        return pitchDao.getSummaryForPitcher(pitcherId,
                new PitchQueryOptionalParams(
                        PitchCount.parsePitchCountFromOptionalQueryParameters(count, balls, strikes),
                        year, month, inning));
    }

    /**
     * Drilling down to a type provides more details on all found pitches of that type.
     * Optional extra filter parameters (via URL query parameters): balls, strikes, year, month. inning.
     */
    @RequestMapping("/{pitcherId}/{pitchType}")
    public PitchTypeSummary getPitchTypeDetails(@PathVariable Integer pitcherId, @PathVariable String pitchType,
                                                @RequestParam(required = false) String count,
                                                @RequestParam(required = false) Integer balls, @RequestParam(required = false) Integer strikes,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam(required = false) Integer month, @RequestParam(required = false) Integer inning) {
        return pitchDao.getTypeSummaryForPitcher(pitcherId, pitchType,
                new PitchQueryOptionalParams(
                        PitchCount.parsePitchCountFromOptionalQueryParameters(count, balls, strikes),
                        year, month, inning));
    }

    /**
     * Simple search API to get the MLB ID of a pitcher by some part of their name.
     */
    @RequestMapping("/search")
    public List<Pitcher> searchPitchers(@RequestParam String name) {
        return pitcherDao.findByNameContaining(name);
    }
}
