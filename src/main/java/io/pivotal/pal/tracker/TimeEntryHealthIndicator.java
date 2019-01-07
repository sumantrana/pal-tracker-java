package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator  implements HealthIndicator {

    private static final int MAX_TIME_ENTRIES = 5;

    private TimeEntryRepository m_repo = null;

    public TimeEntryHealthIndicator( TimeEntryRepository repo ){
        m_repo = repo;
    }

    @Override
    public Health health() {

        if (m_repo.list().size() < MAX_TIME_ENTRIES ){

            return Health.up().build();

        } else {

            return Health.down().build();
        }
    }
}
