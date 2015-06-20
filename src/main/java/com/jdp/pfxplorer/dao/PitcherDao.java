package com.jdp.pfxplorer.dao;

import com.jdp.pfxplorer.domain.Pitcher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PitcherDao extends CrudRepository<Pitcher, Long> {
    public List<Pitcher> findByNameContaining(String name);
}
