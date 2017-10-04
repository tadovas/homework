package org.solar.crawlerlog.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorageHealthIndicator implements HealthIndicator {

  private static final int STORAGE_CAPACITY_THRESHOLD = 100;

  private ConcurrentHashMapRepository repository;

  @Autowired
  public InMemoryStorageHealthIndicator(ConcurrentHashMapRepository repository) {
    this.repository = repository;
  }

  @Override
  public Health health() {

    Health.Builder builder =
        new Health.Builder()
            .withDetail("storage.capacity.threshold", STORAGE_CAPACITY_THRESHOLD)
            .withDetail("storage.count", repository.count());

    if (repository.count() < STORAGE_CAPACITY_THRESHOLD) {
      return builder.up().build();
    }

    return builder.down().build();
  }
}
