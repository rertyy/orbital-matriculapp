ALTER TABLE events
    ALTER COLUMN event_start_date DROP DEFAULT,
    ALTER COLUMN event_end_date DROP DEFAULT,
    ALTER COLUMN event_body DROP DEFAULT;

UPDATE events
SET event_body = NULL
WHERE event_body = '';

ALTER TABLE events
    ALTER COLUMN event_body DROP NOT NULL,
    ALTER COLUMN event_start_date DROP NOT NULL,
    ALTER COLUMN event_end_date DROP NOT NULL;
