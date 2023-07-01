package main

import (
	"github.com/joho/godotenv"
	"log"
	"net/http"
	"orbital-backend/api"
	"orbital-backend/database"
	"os"
)

// https://go.dev/doc/tutorial/database-access#add_data

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	db, err := database.Connect()

	r := api.SetupRouter(&api.Handler{DB: db})
	http.Handle("/", r)

	serverPort := os.Getenv("SERVER_PORT")

	log.Println("Server started")
	log.Fatal(http.ListenAndServe(serverPort, r))
}
