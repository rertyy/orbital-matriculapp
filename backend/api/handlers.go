package api

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
)

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
