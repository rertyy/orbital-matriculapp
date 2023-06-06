package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

type LoginRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

type LoginResponse struct {
	Message string `json:"message"`
}

// https://go.dev/doc/tutorial/database-access#add_data

// TODO modularise
func main() {
	r := mux.NewRouter()

	r.HandleFunc("/login", handleLogin).Methods("POST")
	r.HandleFunc("/register", handleRegister).Methods("POST")
	log.Println("Server started on http://localhost:8080")
	log.Fatal(http.ListenAndServe(":8080", r))
}

const (
	host   = "localhost"
	port   = 5432
	user   = "postgres"
	pass   = "password"
	dbname = "orbitaldb"
)

func loginRequest(username string, password string) (bool, error) {
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s "+
		"password=%s dbname=%s sslmode=disable",
		host, port, user, pass, dbname)
	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		panic(err)
	}
	defer func(db *sql.DB) {
		err := db.Close()
		if err != nil {
			panic(err)
		}
	}(db)

	err = db.Ping()
	if err != nil {
		panic(err)
	}
	log.Println("Successfully connected!")

	// to re-look https://go.dev/doc/database/querying

	sqlStatement := `SELECT username, password FROM users WHERE username=$1 AND password=$2;`
	row := db.QueryRow(sqlStatement, username, password)
	switch err := row.Scan(&username, &password); err {
	case sql.ErrNoRows:
		log.Println("Wrong username or password")
		return false, err
	case nil:
		log.Println("Login successful")
		return true, nil
	default:
		log.Println("Wrong username of password")
		return false, err
	}

}

func handleLogin(w http.ResponseWriter, r *http.Request) {
	var request LoginRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	result, err := loginRequest(request.Username, request.Password)

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

func handleRegister(w http.ResponseWriter, r *http.Request) {
	var request LoginRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	result, err := registerRequest(request.Username, request.Password)

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

func registerRequest(username string, password string) (bool, error) {
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s "+
		"password=%s dbname=%s sslmode=disable",
		host, port, user, pass, dbname)
	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		panic(err)
	}
	defer func(db *sql.DB) {
		err := db.Close()
		if err != nil {
			panic(err)
		}
	}(db)

	err = db.Ping()
	if err != nil {
		panic(err)
	}
	log.Println("Successfully connected!")

	sqlStatement := `SELECT username FROM users WHERE username=$1;`
	row := db.QueryRow(sqlStatement, username)
	switch err := row.Scan(&username); err {
	case sql.ErrNoRows:
		log.Println("User not found")
	case nil:
		log.Println("Username already taken")
		return false, err
	default:
		log.Println("Unknown error")
		return false, err
	}
	sqlUpdate := `INSERT INTO users (username, password, email) VALUES ($1, $2, $3);`
	_, err = db.Exec(sqlUpdate, username, password, "test3@test.com")
	log.Println("Successfully registered")
	if err != nil {
		return true, err
	}
	return false, err

}
