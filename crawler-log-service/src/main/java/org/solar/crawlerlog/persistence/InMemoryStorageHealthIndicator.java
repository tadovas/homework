package org.solar.crawlerlog.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorageHealthIndicator implements HealthIndicator{

    private ConcurrentHashMapRepository repository;

    @Autowired
    public InMemoryStorageHealthIndicator(ConcurrentHashMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {

        Health.Builder builder = new Health.Builder().withDetail("storage.count" , repository.count());

        if( repository.count() < 100 ) {
            return builder.up().build();
        }

        return builder.down().build();
    }
}
