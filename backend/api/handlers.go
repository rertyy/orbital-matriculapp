package api

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
)

// TODO the err handling is wrong in the entire file http.StatusInternalServerError (err 500)
/* TODO add in defer rows.Close() to all the functions that use rows,
to figure out what is the correct place to rows.Close(),
or else just combine the Handle and the function together
*/

func (h *Handler) HandleGetAllPosts(w http.ResponseWriter, r *http.Request) {
	posts, err := h.getAllPosts()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")

	err = json.NewEncoder(w).Encode(posts)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (h *Handler) getAllPosts() ([]Post, error) {
	log.Println("getAllPosts")
	// TODO rn its hardcoded to WHERE cat_id == 1
	sqlStatement := `SELECT p.title, p.body, c.cat_name, p.created_by, p.created_at, p.last_updated
					FROM posts p
					JOIN categories c ON p.category_id = c.cat_id
					WHERE p.category_id = 1;`
	rows, err := h.DB.Query(sqlStatement)
	if err != nil {
		return nil, err
	}

	defer func(rows *sql.Rows) {
		err := rows.Close()
		if err != nil {
			panic(err)
		}
	}(rows)

	var posts []Post
	for rows.Next() {
		var post Post
		if err := rows.Scan(&post.Title, &post.Body, &post.CategoryName, &post.CreatedBy, &post.CreatedAt, &post.LastUpdated); err != nil {
			return posts, err
		}
		posts = append(posts, post)
	}
	if err = rows.Err(); err != nil {
		return posts, err
	}
	return posts, nil
}

func (h *Handler) HandleLogin(w http.ResponseWriter, r *http.Request) {
	var request LoginRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	result, err := h.loginRequest(request.Username, request.Password)

	if result {
		response := LoginResponse{
			Message: "Login successful",
		}
		w.Header().Set("Content-Type", "application/json")
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			return
		}
		return

	}

	http.Error(w, "Invalid username or password", http.StatusUnauthorized)
}

func (h *Handler) loginRequest(username string, password string) (bool, error) {
	log.Println("loginRequest")

	// to re-look https://go.dev/doc/database/querying

	sqlStatement := `SELECT username, password FROM users WHERE username=$1 AND password=$2;`
	row := h.DB.QueryRow(sqlStatement, username, password)

	// TODO defer func row

	//log.Println("row: ", &row)
	switch err := row.Scan(&username, &password); err {
	case sql.ErrNoRows:
		log.Println("Wrong username or password")
		return false, err
	case nil:
		log.Println("Login successful")
		return true, nil
	default:
		log.Println("Unknown err", err)
		return false, err
	}

}

func (h *Handler) registerRequest(username string, password string) (bool, error) {
	log.Println("loginRequest")

	sqlStatement := `SELECT username FROM users WHERE username=$1;`
	row := h.DB.QueryRow(sqlStatement, username)

	// TODO defer func row

	switch err := row.Scan(&username); err {
	case sql.ErrNoRows:
		log.Println("User not found")
	case nil:
		log.Println("Username already taken")
		return false, err
	default:
		log.Println("Unknown err", err)
		return false, err
	}
	sqlUpdate := `INSERT INTO users (username, password, email) VALUES ($1, $2, $3);`
	_, err := h.DB.Exec(sqlUpdate, username, password, "test3@test.com")
	log.Println("Successfully registered")
	if err != nil {
		return true, err
	}
	return false, err

}

func (h *Handler) HandleRegister(w http.ResponseWriter, r *http.Request) {
	var request LoginRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	result, err := h.registerRequest(request.Username, request.Password)

	if result {
		response := LoginResponse{
			Message: "Login successful",
		}
		w.Header().Set("Content-Type", "application/json")
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			return
		}
		return

	}

	http.Error(w, "Invalid username or password", http.StatusUnauthorized)
}
