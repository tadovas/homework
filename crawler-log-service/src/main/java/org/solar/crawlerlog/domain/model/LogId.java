package org.solar.crawlerlog.domain.model;

public class LogId {

  private String id;

  private LogId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }

  public static LogId fromString(String value) {
    return new LogId(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LogId logId = (LogId) o;

    return id.equals(logId.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
