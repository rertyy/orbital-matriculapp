-- name: GetAllPosts :many
SELECT p.post_id, p.title, p.body, p.category_id, c.cat_name, p.created_by, u.username, p.created_at, p.last_updated
FROM posts p
         JOIN categories c ON p.category_id = c.cat_id
         JOIN users u ON p.created_by = u.user_id;