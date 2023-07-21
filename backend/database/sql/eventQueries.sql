-- name: GetAllEvents :many
SELECT event_id, event_name, event_body, event_start_date, event_end_date
FROM events;