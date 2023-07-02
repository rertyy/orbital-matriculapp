-- name: AddUser :exec
INSERT INTO users (username, password, email)
VALUES ($1, $2, $3);

-- name: GetUserByUsername :one
SELECT username, password
FROM users
WHERE username = $1;

-- name: CheckUserExists :one
SELECT EXISTS(SELECT 1 from users where username = $1);

-- name: DeleteUser :exec
DELETE
FROM users
WHERE username = $1;

-- name: ChangePassword :exec
UPDATE users
SET password = $2
WHERE username = $1;




