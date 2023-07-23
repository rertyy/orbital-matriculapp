-- set default values for events
ALTER TABLE events
    ALTER COLUMN event_start_date SET DEFAULT '2100-01-01 00:00:00'::timestamptz,
    ALTER COLUMN event_end_date SET DEFAULT '2100-01-01 00:00:00'::timestamptz,
    ALTER COLUMN event_body SET DEFAULT '';

UPDATE events
SET event_body = ''
WHERE event_body IS NULL;

-- remove nullable from events so values are not mapped to sql.NullString
ALTER TABLE events
    ALTER COLUMN event_body SET NOT NULL,
    ALTER COLUMN event_start_date SET NOT NULL,
    ALTER COLUMN event_end_date SET NOT NULL;