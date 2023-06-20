package main

import (
	"database/sql"
	"github.com/joho/godotenv"
	"log"
	"net/http"
	"os"
)

type LoginRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

type LoginResponse struct {
	Message string `json:"message"`
}

var db *sql.DB

// https://go.dev/doc/tutorial/database-access#add_data

// TODO modularise
func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	db, err = Connect()

	r := SetupRouter(&Handler{DB: db})
	http.Handle("/", r)

	serverPort := os.Getenv("SERVER_PORT")

	log.Println("Server started on http://localhost" + serverPort)
	log.Fatal(http.ListenAndServe(serverPort, r))
}
