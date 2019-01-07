package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {


    private TimeEntryRepository m_repo;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController( TimeEntryRepository repo, MeterRegistry meterRegistry ){
        m_repo = repo;

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry){

        TimeEntry theCreatedEntry = m_repo.create(entry);

        actionCounter.increment();
        timeEntrySummary.record(m_repo.list().size());

        return new ResponseEntity<TimeEntry>(theCreatedEntry, HttpStatus.CREATED);

    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id){

        TimeEntry theFoundEntry = m_repo.find(id);

        if ( theFoundEntry == null ){
            actionCounter.increment();
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<TimeEntry>(theFoundEntry, HttpStatus.OK);

    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){

        List<TimeEntry> theList = m_repo.list();
        actionCounter.increment();

        return new ResponseEntity<List<TimeEntry>>(theList, HttpStatus.OK);

    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long id, @RequestBody TimeEntry entry){

        TimeEntry updatedEntry = m_repo.update(id, entry);

        if ( updatedEntry == null ){
            actionCounter.increment();
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<TimeEntry>(updatedEntry, HttpStatus.OK);

    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id){

        m_repo.delete(id);

        actionCounter.increment();
        timeEntrySummary.record(m_repo.list().size());

        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }

}
