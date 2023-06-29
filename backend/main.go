package main

import (
	"github.com/joho/godotenv"
	"log"
	"net/http"
	"orbital-backend/api"
	db2 "orbital-backend/db"
	"os"
)

// https://go.dev/doc/tutorial/database-access#add_data

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	db, err := db2.Connect()
	if db == nil || err != nil {
		log.Println("test2", err)
		panic(err)
	}

	r := api.SetupRouter(&api.Handler{DB: db})
	http.Handle("/", r)

	serverPort := os.Getenv("SERVER_PORT")

	log.Println("Server started on http://localhost" + serverPort)
	log.Fatal(http.ListenAndServe(serverPort, r))
}
