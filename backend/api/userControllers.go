package api

import (
	"database/sql"
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"orbital-backend/util"
)

// TODO clean the logic

func (h *Handler) HandleDeleteUser(writer http.ResponseWriter, request *http.Request) {

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
		w.WriteHeader(http.StatusOK)
		return

	}

	http.Error(w, "Invalid username or password", http.StatusUnauthorized)
}

// when testing, for security, check that failing username check is not faster than failing password check.
// E.g.
// (y, y), 100ms
// (y, n), 102ms
// (n, y), 108ms
// (n, n), 108ms
//
// maybe there is a better way than hashing a random string
func (h *Handler) loginRequest(username string, password string) error {
	log.Println("loginRequest")

	// to re-look https://go.dev/doc/database/querying

	var storedHashPassword string
	sqlStatement := `SELECT password FROM users WHERE username=$1;`
	row := h.DB.QueryRow(sqlStatement, username)
	switch err := row.Scan(&storedHashPassword); err {
	case sql.ErrNoRows:
		log.Println("Username not found")
		_, _ = util.HashPassword("password") // line is solely here to set timeout for security reasons
		return err
	case nil:
		log.Println("Username found")
	default:
		_, _ = util.HashPassword("password") // line is solely here to set timeout for security reasons
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
	log.Printf("Successfully registered User: %s Email: %s", username, email)
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
		w.WriteHeader(http.StatusCreated)
		return

	}

	http.Error(w, "Unable to register user", http.StatusUnauthorized)
}
