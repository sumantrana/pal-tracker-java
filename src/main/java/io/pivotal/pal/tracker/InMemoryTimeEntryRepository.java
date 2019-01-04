package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {


    private HashMap<Long, TimeEntry> m_timeEntryMap;


    public InMemoryTimeEntryRepository(){

        m_timeEntryMap = new HashMap<Long, TimeEntry>();

    }

    @Override
    public TimeEntry create(TimeEntry timeEntryToCreate) {

        long theId = m_timeEntryMap.size() + 1;

        timeEntryToCreate.setId(theId);

        m_timeEntryMap.put(theId, timeEntryToCreate );

        return timeEntryToCreate;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return m_timeEntryMap.get(timeEntryId);
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {

        any.setId(eq);
        m_timeEntryMap.put(eq, any);

        return any;
    }

    @Override
    public void delete(long timeEntryId) {

        m_timeEntryMap.remove( timeEntryId );

    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(m_timeEntryMap.values());
    }

}
