package com.jdp.pfxplorer.dao;

import com.jdp.pfxplorer.domain.derived.PitchAttributeSummaryStatistics;
import com.jdp.pfxplorer.domain.derived.PitchCounts;
import com.jdp.pfxplorer.domain.derived.PitchTypeSummary;
import com.jdp.pfxplorer.domain.derived.PitchingSummary;
import com.jdp.pfxplorer.util.PitchQueryOptionalParams;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of custom Pitch lookup methods.
 */
@Repository
public class PitchDaoImpl implements PitchDaoCustom {
    @PersistenceContext
    private EntityManager em;

    /**
     * Get summary statistics for a pitcher - # games, overall pitch counts, and pitch counts by type.
     * Additional filters (e.g. year, inning) applied via PitchQueryOptionalParams.
     */
    @Override
    public PitchingSummary getSummaryForPitcher(int pitcherId, PitchQueryOptionalParams params) {
        String extraFilter = buildExtraFilter(params);

        Query q = em.createNativeQuery("select count(distinct game_id) from pitch where pitcher_id = :pitcherId " + extraFilter);
        q.setParameter("pitcherId", pitcherId);
        filterOptionalParams(q, params);
        Object games = q.getSingleResult();

        q = em.createNativeQuery(
                "SELECT pitch_type, count(pitch_id)," +
                        " sum(if(type = 'S', 1, 0)) as strikes," +
                        " sum(if(type = 'B', 1, 0)) as balls," +
                        " sum(if(type = 'X', 1, 0)) as contacts" +
                        " from pitch " +
                        " where pitcher_id = :pitcherId " +
                        extraFilter +
                        " group by pitch_type" +
                        " order by pitch_type");
        q.setParameter("pitcherId", pitcherId);
        filterOptionalParams(q, params);

        return buildSummary(q.getResultList(), games);
    }

    /**
     * Get statistics on all pitches of a certain type (e.g. four-seam fastballs)
     * for a pitcher - balls, strikes, velocity, spin, movement.
     * Additional filters (e.g. year, inning) applied via PitchQueryOptionalParams.
     */
    @Override
    public PitchTypeSummary getTypeSummaryForPitcher(int pitcherId, String pitchType, PitchQueryOptionalParams params) {
        Query q = em.createNativeQuery("select " +
                "count(pitch_id) as pitches, " +
                "sum(if(type = 'S', 1, 0)) as strikes, " +
                "sum(if(type = 'B', 1, 0)) as balls, " +
                "sum(if(type = 'X', 1, 0)) as contacts, " +
                "avg(start_speed) as avg_velocity, " +
                "stddev(start_speed) as stddev_velocity, " +
                "max(start_speed) as max_velocity, " +
                "min(start_speed) as min_velocity, " +
                "avg(spin_rate) as avg_spin_rate, " +
                "stddev(spin_rate) as stddev_spin_rate, " +
                "max(spin_rate) as max_spin_rate, " +
                "min(spin_rate) as min_spin_rate, " +
                "avg(spin_dir) as avg_spin_dir, " +
                "stddev(spin_dir) as stddev_spin_dir, " +
                "max(spin_dir) as max_spin_dir, " +
                "min(spin_dir) as min_spin_dir, " +
                "avg(pfx_x) as avg_pfx_x, " +
                "stddev(pfx_x) as stddev_pfx_x, " +
                "max(pfx_x) as max_pfx_x, " +
                "min(pfx_x) as min_pfx_x," +
                "avg(pfx_z) as avg_pfx_z, " +
                "stddev(pfx_z) as stddev_pfx_z," +
                "max(pfx_z) as max_pfx_z," +
                "min(pfx_z) as min_pfx_z " +
                "from pitch " +
                "where pitcher_id = :pitcherId " +
                " and pitch_type = :pitchType " +
                buildExtraFilter(params));
        q.setParameter("pitcherId", pitcherId);
        q.setParameter("pitchType", pitchType);
        filterOptionalParams(q, params);

        Object[] row = (Object[])q.getResultList().get(0);

        PitchTypeSummary summary = new PitchTypeSummary();
        int pitchCount = ((BigInteger)row[0]).intValue();

        if (pitchCount == 0) {   // no pitches found for type
            summary.setPitchCounts(new PitchCounts(0, 0, 0, 0));
            return summary;
        }

        PitchCounts counts = new PitchCounts(
                pitchCount,
                ((BigDecimal)row[1]).intValue(),
                ((BigDecimal)row[2]).intValue(),
                ((BigDecimal)row[3]).intValue());
        summary.setPitchCounts(counts);

        summary.setVelocity(buildSummaryStatistics(row, 4));
        summary.setSpinRate(buildSummaryStatistics(row, 8));
        summary.setSpinDir(buildSummaryStatistics(row, 12));
        summary.setHorizontalMovement(buildSummaryStatistics(row, 16));
        summary.setVerticalMovement(buildSummaryStatistics(row, 20));

        return summary;
    }

    /**
     * Build PitchingSummary output from a set of query results.
     */
    private PitchingSummary buildSummary(List<Object[]> results, Object games) {
                // build summary bean
        PitchingSummary summary = new PitchingSummary();

        int totalPitches = 0;
        int totalBalls = 0;
        int totalStrikes = 0;
        int totalContacts = 0;

        for (Object[] row : results) {
            String pitchType = (String)row[0];
            int pitches = ((BigInteger)row[1]).intValue();
            int strikes = ((BigDecimal)row[2]).intValue();
            int balls = ((BigDecimal)row[3]).intValue();
            int contacts = ((BigDecimal)row[4]).intValue();

            totalPitches += pitches;
            totalStrikes += strikes;
            totalBalls += balls;
            totalContacts += contacts;

            summary.getPitchCountByType().put(pitchType, new PitchCounts(pitches, strikes, balls, contacts));
        }

        PitchCounts result = new PitchCounts(totalPitches, totalStrikes, totalBalls, totalContacts);
        summary.setPitchCount(result);
        summary.setGames(((BigInteger)games).intValue());

        return summary;
    }

    /**
     * Build a summary statistics object from a part of a query row.
     * Assumes:  items in row are in avg, stdev, max, min order
     */
    private PitchAttributeSummaryStatistics buildSummaryStatistics(Object[] queryRow, int startIndex) {
        return new PitchAttributeSummaryStatistics(
                Math.round((Double)queryRow[startIndex]*10)/10.0,
                Math.round((Double)queryRow[startIndex+1]*10)/10.0,
                Math.round((Double)queryRow[startIndex+2]*10)/10.0,
                Math.round((Double)queryRow[startIndex+3]*10)/10.0);
    }

    /**
     * Build additional "where" clauses for the query from optional parameters.
     */
    private String buildExtraFilter(PitchQueryOptionalParams params) {
        return (params.getPitchCount().getBalls() != null ? " and count_balls = :balls " : "") +
                (params.getPitchCount().getStrikes() != null ? " and count_strikes = :strikes " : "") +
                (params.getYear() != null ? " and year = :year " : "") +
                (params.getMonth() != null ? " and month = :month " : "") +
                (params.getInning() != null ? " and inning = :inning " : "");
    }

    /**
     * Set query parameters for additional "where" clauses from optional parameters.
     */
    private void filterOptionalParams(Query q, PitchQueryOptionalParams params) {
        if (params.getPitchCount().getBalls() != null) {
            q.setParameter("balls", params.getPitchCount().getBalls());
        }
        if (params.getPitchCount().getStrikes() != null) {
            q.setParameter("strikes", params.getPitchCount().getStrikes());
        }
        if (params.getYear() != null) {
            q.setParameter("year", params.getYear());
        }
        if (params.getMonth() != null) {
            q.setParameter("month", params.getMonth());
        }
        if (params.getInning() != null) {
            q.setParameter("inning", params.getInning());
        }
    }
}
