-- name: GetAllPosts :many
SELECT p.post_id,
       p.title,
       p.body,
       p.category_id,
       c.cat_name,
       p.created_by,
       u.username,
       p.created_at,
       p.last_updated
FROM posts p
         JOIN categories c ON p.category_id = c.cat_id
         JOIN users u ON p.created_by = u.user_id;

-- name: GetPostById :one
SELECT p.post_id,
       p.title,
       p.body,
       p.category_id,
       c.cat_name,
       p.created_by,
       u.username,
       p.created_at,
       p.last_updated
FROM posts p
         JOIN categories c ON p.category_id = c.cat_id
         JOIN users u ON p.created_by = u.user_id
WHERE p.post_id = $1;


-- name: UpdatePost :one
UPDATE posts
SET title        = $1,
    body         = $2,
    category_id  = $3,
    last_updated = now()
WHERE post_id = $4
RETURNING *;


-- name: DeletePost :exec
DELETE
FROM posts
WHERE post_id = $1;


-- name: AddPost :one
INSERT INTO posts (title, body, category_id, created_by)
VALUES ($1, $2, $3, $4)
RETURNING *;
