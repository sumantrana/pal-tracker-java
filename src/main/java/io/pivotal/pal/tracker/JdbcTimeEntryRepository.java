package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {


    private JdbcTemplate m_template = null;

    public JdbcTimeEntryRepository ( DataSource source ){

            m_template = new JdbcTemplate( source );

    }

    @Override
    public TimeEntry create(TimeEntry timeEntryToCreate) {

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        m_template.update( connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?,?,?,?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntryToCreate.getProjectId());
            statement.setLong(2, timeEntryToCreate.getUserId());
            statement.setDate(3, Date.valueOf(timeEntryToCreate.getDate()));
            statement.setInt(4, timeEntryToCreate.getHours());

            return statement;
        }, generatedKeyHolder );

        return find( generatedKeyHolder.getKey().longValue());

    }

    @Override
    public TimeEntry find(long timeEntryId) {

        return m_template.query( "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?", new Object[] {timeEntryId}, extractor );

    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        m_template.update("UPDATE time_entries SET project_id = ?, user_id = ?, date = ?,  hours = ? WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);

    }

    @Override
    public void delete(long timeEntryId) {
        m_template.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return m_template.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor = (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

}
