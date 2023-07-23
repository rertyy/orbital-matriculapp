-- name: GetAllThreads :many
SELECT t.thread_id,
       t.thread_name,
       t.thread_body,
       t.thread_created_by,
       t.thread_created_at,
       u.user_id,
       u.username
FROM threads t
         JOIN users u on t.thread_created_by = u.user_id;


-- name: AddThread :one
INSERT INTO threads (thread_name, thread_body, thread_created_by)
VALUES ($1, $2, $3)
RETURNING *;

-- name: GetThreadById :one
SELECT t.thread_id,
       t.thread_name,
       t.thread_body,
       t.thread_created_by,
       t.thread_created_at,
       u.user_id,
       u.username
FROM threads t
         JOIN users u on t.thread_created_by = u.user_id
WHERE thread_id = $1;


-- name: EditThread :one
UPDATE threads
SET thread_name = $1,
    thread_body = $2
WHERE thread_id = $3
RETURNING *;

-- name: DeleteThread :exec
DELETE
FROM threads
WHERE thread_id = $1;

-- name: GetAllReplies :many
SELECT r.reply_id,
       r.reply_body,
       r.reply_created_at,
       r.reply_last_updated,
       u.user_id,
       u.username
FROM replies r
         JOIN threads t on r.thread_id = t.thread_id
         JOIN users u on r.reply_created_by = u.user_id;


-- name: GetRepliesByThread :many
SELECT r.reply_id,
       r.reply_body,
       r.reply_created_at,
       r.reply_last_updated,
       u.user_id,
       u.username
FROM replies r
         JOIN threads t on r.thread_id = t.thread_id
         JOIN users u on r.reply_created_by = u.user_id
WHERE t.thread_id = $1;


-- name: AddReply :many
INSERT INTO replies (reply_body, thread_id, reply_created_by)
VALUES ($1, $2, $3)
RETURNING *;

