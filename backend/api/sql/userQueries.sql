-- name: AddUser :exec
INSERT INTO users (username, password, email) VALUES ($1, $2, $3);

-- name: GetUserByUsername :one
SELECT username, password FROM users WHERE username=$1

