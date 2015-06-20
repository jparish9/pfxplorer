package com.jdp.pfxplorer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for the PITCHER table, representing name and MLB ID for a specific pitcher.
 */
@Entity
@Table(name = "PITCHER")
public class Pitcher {
    private int pitcherId;
    private String name;

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    @Id
    @Column(name = "PITCHER_ID", nullable = false, unique = true)
    public int getPitcherId() {
        return pitcherId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPitcherId(int pitcherId) {
        this.pitcherId = pitcherId;
    }
}
