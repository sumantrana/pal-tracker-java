package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    @Autowired
    public TimeEntryRepository m_repo;

    public TimeEntryController(){

    }

    public TimeEntryController( TimeEntryRepository repo ){
        m_repo = repo;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry){

        TimeEntry theCreatedEntry = m_repo.create(entry);

        return new ResponseEntity<TimeEntry>(theCreatedEntry, HttpStatus.CREATED);

    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id){

        TimeEntry theFoundEntry = m_repo.find(id);

        if ( theFoundEntry == null ){
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<TimeEntry>(theFoundEntry, HttpStatus.OK);

    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){

        List<TimeEntry> theList = m_repo.list();

        return new ResponseEntity<List<TimeEntry>>(theList, HttpStatus.OK);

    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long id, @RequestBody TimeEntry entry){

        TimeEntry updatedEntry = m_repo.update(id, entry);

        if ( updatedEntry == null ){
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<TimeEntry>(updatedEntry, HttpStatus.OK);

    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id){

        m_repo.delete(id);

        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }

}
