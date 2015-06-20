package com.jdp.pfxplorer.dao;

import com.jdp.pfxplorer.domain.Pitch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PitchDao extends CrudRepository<Pitch, Long>, PitchDaoCustom {
    // derived query: select count(*) from pitch where game_id = %gameId;
    Long countByGameId(String gameId);
}
