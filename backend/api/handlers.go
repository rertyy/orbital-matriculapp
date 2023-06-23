package api

import (
	"database/sql"
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"orbital-backend/util"
)

// TODO the err handling is wrong in the entire file eg http.StatusInternalServerError (err 500)
/* TODO add in defer rows.Close() to all the functions
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

	// you only need to defer func rows.Close() when using Query statement which returns multiple results
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

	err = h.loginRequest(request.Username, request.Password)

	if err == nil {
		response := HttpResponse{
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

func (h *Handler) loginRequest(username string, password string) error {
	log.Println("loginRequest")

	// to re-look https://go.dev/doc/database/querying

	var storedHashPassword string
	sqlStatement := `SELECT password FROM users WHERE username=$1;`
	row := h.DB.QueryRow(sqlStatement, username)
	switch err := row.Scan(&storedHashPassword); err {
	case sql.ErrNoRows:
		log.Println("Username not found")
		return err
	case nil:
		log.Println("Username found")
	default:
		log.Println("Unknown err", err)
		return err
	}

	err := util.CheckPassword(password, storedHashPassword)

	if err != nil {
		log.Println("Wrong password")
		return err
	}
	log.Println("Correct password")
	return nil

}

func (h *Handler) registerRequest(username string, password string, email string) error {
	log.Println("registerRequest")

	sqlStatement := `SELECT username FROM users WHERE username=$1;`
	row := h.DB.QueryRow(sqlStatement, username)

	// TODO defer func row

	var storedUsername string
	switch err := row.Scan(&storedUsername); err {
	case sql.ErrNoRows: // TODO definitely wrong to use ErrNoRows as a good sign
		log.Println("User not found, registration can proceed")
	default:
		log.Println("Unknown exception", err)
		return errors.New("unknown exception")
	}
	log.Println(storedUsername)

	hashedPassword, err := util.HashPassword(password)
	log.Println(hashedPassword)
	sqlUpdate := `INSERT INTO users (username, password, email) VALUES ($1, $2, $3);`
	_, err = h.DB.Exec(sqlUpdate, username, hashedPassword, email)
	if err != nil {
		log.Println(err)
		return err
	}
	log.Println("Successfully registered")
	return nil

}

func (h *Handler) HandleRegister(w http.ResponseWriter, r *http.Request) {
	var request RegisterRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	err = h.registerRequest(request.Username, request.Password, request.Email)

	if err == nil {
		response := HttpResponse{
			Message: "Registration successful",
		}
		w.Header().Set("Content-Type", "application/json")
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			return
		}
		return

	}

	http.Error(w, "Unable to register user", http.StatusUnauthorized)
}

// TODO add in the rest of the handlers

func (h *Handler) HandleGetCategories(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetCategory(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetCategoryPosts(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetPost(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleModifyPost(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleDeletePost(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleDeleteCategory(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleModifyCategory(w http.ResponseWriter, r *http.Request) {

}
