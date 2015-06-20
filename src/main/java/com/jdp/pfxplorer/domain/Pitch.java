package com.jdp.pfxplorer.domain;

import javax.persistence.*;

/**
 * Entity class for the PITCH table, representing all Pitch F/X data about a single pitch.
 */
@Entity
@Table(name = "PITCH")
public class Pitch {
  private int pitchId;
  private int inning;
  private int batterId;
  private String batterHitting;
  private String batterHeight;
  private int pitcherId;
  private String pitcherThrows;
  private int countBalls;
  private int countStrikes;
  private int year;
  private int month;
  private int day;
  private String gameId;
  private String description;
  private String type;
  private double x;
  private double y;
  private String svId;
  private double startSpeed;
  private double endSpeed;
  private Double szTop;
  private Double szBottom;
  private double pfxX;
  private double pfxZ;
  private double px;
  private double pz;
  private double x0;
  private double y0;
  private double z0;
  private double vx0;
  private double vy0;
  private double vz0;
  private double ax;
  private double ay;
  private double az;
  private double breakY;
  private double breakAngle;
  private double breakLength;
  private String pitchType;
  private Double typeConfidence;
  private Integer zone;
  private Integer nasty;
  private Double spinDir;
  private Double spinRate;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "PITCH_ID", nullable = false, unique = true)
  public int getPitchId() {
    return pitchId;
  }

  @Column(name = "INNING", nullable = false)
  public int getInning() {
    return inning;
  }
  
  @Column(name = "BATTER_ID", nullable = false)
  public int getBatterId() {
    return batterId;
  }

  @Column(name = "BATTER_HITTING", nullable = false)
  public String getBatterHitting() {
    return batterHitting;
  }

  @Column(name = "BATTER_HEIGHT")
  public String getBatterHeight() {
    return batterHeight;
  }

  @Column(name = "PITCHER_ID", nullable = false)
  public int getPitcherId() {
    return pitcherId;
  }

  @Column(name = "YEAR", nullable = false)
  public int getYear() {
    return year;
  }

  @Column(name = "MONTH", nullable = false)
  public int getMonth() {
    return month;
  }

  @Column(name = "DAY", nullable = false)
  public int getDay() {
    return day;
  }

  @Column(name = "AX", nullable = false)
  public double getAx() {
    return ax;
  }

  @Column(name = "AY", nullable = false)
  public double getAy() {
    return ay;
  }

  @Column(name = "AZ", nullable = false)
  public double getAz() {
    return az;
  }

  @Column(name = "BREAK_ANGLE", nullable = false)
  public double getBreakAngle() {
    return breakAngle;
  }

  @Column(name = "BREAK_LENGTH", nullable = false)
  public double getBreakLength() {
    return breakLength;
  }

  @Column(name = "BREAK_Y", nullable = false)
  public double getBreakY() {
    return breakY;
  }

  @Column(name = "DES")
  public String getDescription() {
    return description;
  }

  @Column(name = "END_SPEED", nullable = false)
  public double getEndSpeed() {
    return endSpeed;
  }

  @Column(name = "GAME_ID", nullable = false)
  public String getGameId() {
    return gameId;
  }

  @Column(name = "NASTY")
  public Integer getNasty() {
    return nasty;
  }

  @Column(name = "PFX_X", nullable = false)
  public double getPfxX() {
    return pfxX;
  }

  @Column(name = "PFX_Z", nullable = false)
  public double getPfxZ() {
    return pfxZ;
  }

  @Column(name = "PITCH_TYPE")
  public String getPitchType() {
    return pitchType;
  }

  @Column(name = "PX", nullable = false)
  public double getPx() {
    return px;
  }

  @Column(name = "PZ", nullable = false)
  public double getPz() {
    return pz;
  }

  @Column(name = "SPIN_DIR")
  public Double getSpinDir() {
    return spinDir;
  }

  @Column(name = "SPIN_RATE")
  public Double getSpinRate() {
    return spinRate;
  }

  @Column(name = "START_SPEED", nullable = false)
  public double getStartSpeed() {
    return startSpeed;
  }

  @Column(name = "SV_ID")
  public String getSvId() {
    return svId;
  }

  @Column(name = "SZ_BOT", nullable = false)
  public Double getSzBottom() {
    return szBottom;
  }

  @Column(name = "SZ_TOP", nullable = false)
  public Double getSzTop() {
    return szTop;
  }

  @Column(name = "TYPE")
  public String getType() {
    return type;
  }

  @Column(name = "TYPE_CONFIDENCE")
  public Double getTypeConfidence() {
    return typeConfidence;
  }

  @Column(name = "VX0", nullable = false)
  public double getVx0() {
    return vx0;
  }

  @Column(name = "VY0", nullable = false)
  public double getVy0() {
    return vy0;
  }

  @Column(name = "VZ0", nullable = false)
  public double getVz0() {
    return vz0;
  }

  @Column(name = "X0", nullable = false)
  public double getX0() {
    return x0;
  }

  @Column(name = "X", nullable = false)
  public double getX() {
    return x;
  }

  @Column(name = "Y0", nullable = false)
  public double getY0() {
    return y0;
  }

  @Column(name = "Y", nullable = false)
  public double getY() {
    return y;
  }

  @Column(name = "Z0", nullable = false)
  public double getZ0() {
    return z0;
  }

  @Column(name = "ZONE", nullable = false)
  public Integer getZone() {
    return zone;
  }

  @Column(name = "COUNT_BALLS", nullable = false)
  public int getCountBalls() {
    return countBalls;
  }

  @Column(name = "COUNT_STRIKES", nullable = false)
  public int getCountStrikes() {
    return countStrikes;
  }

  @Column(name = "PITCHER_THROWS")
  public String getPitcherThrows() {
    return pitcherThrows;
  }

  public void setBatterHeight(String batterHeight) {
    this.batterHeight = batterHeight;
  }

  public void setBatterHitting(String batterHitting) {
    this.batterHitting = batterHitting;
  }

  public void setBatterId(int batterId) {
    this.batterId = batterId;
  }

  public void setInning(int inning) {
    this.inning = inning;
  }

  public void setPitcherId(int pitcherId) {
    this.pitcherId = pitcherId;
  }

  public void setPitchId(int pitchId) {
    this.pitchId = pitchId;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setAx(double ax) {
    this.ax = ax;
  }

  public void setAy(double ay) {
    this.ay = ay;
  }

  public void setAz(double az) {
    this.az = az;
  }

  public void setBreakAngle(double breakAngle) {
    this.breakAngle = breakAngle;
  }

  public void setBreakLength(double breakLength) {
    this.breakLength = breakLength;
  }

  public void setBreakY(double breakY) {
    this.breakY = breakY;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEndSpeed(double endSpeed) {
    this.endSpeed = endSpeed;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setNasty(Integer nasty) {
    this.nasty = nasty;
  }

  public void setPfxX(double pfxX) {
    this.pfxX = pfxX;
  }

  public void setPfxZ(double pfxZ) {
    this.pfxZ = pfxZ;
  }

  public void setPitchType(String pitchType) {
    this.pitchType = pitchType;
  }

  public void setPx(double px) {
    this.px = px;
  }

  public void setPz(double pz) {
    this.pz = pz;
  }

  public void setSpinDir(Double spinDir) {
    this.spinDir = spinDir;
  }

  public void setSpinRate(Double spinRate) {
    this.spinRate = spinRate;
  }

  public void setStartSpeed(double startSpeed) {
    this.startSpeed = startSpeed;
  }

  public void setSvId(String svId) {
    this.svId = svId;
  }

  public void setSzBottom(Double szBottom) {
    this.szBottom = szBottom;
  }

  public void setSzTop(Double szTop) {
    this.szTop = szTop;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setTypeConfidence(Double typeConfidence) {
    this.typeConfidence = typeConfidence;
  }

  public void setVx0(double vx0) {
    this.vx0 = vx0;
  }

  public void setVy0(double vy0) {
    this.vy0 = vy0;
  }

  public void setVz0(double vz0) {
    this.vz0 = vz0;
  }

  public void setX0(double x0) {
    this.x0 = x0;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY0(double y0) {
    this.y0 = y0;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void setZ0(double z0) {
    this.z0 = z0;
  }

  public void setZone(Integer zone) {
    this.zone = zone;
  }

  public void setCountBalls(int countBalls) {
    this.countBalls = countBalls;
  }

  public void setCountStrikes(int countStrikes) {
    this.countStrikes = countStrikes;
  }

  public void setPitcherThrows(String pitcherThrows) {
    this.pitcherThrows = pitcherThrows;
  }
}
