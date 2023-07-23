package main

import (
	"github.com/joho/godotenv"
	"github.com/rs/cors"
	"log"
	"net/http"
	"orbital-backend/api"
	"orbital-backend/database"
	"orbital-backend/util"
	"os"
)

// https://go.dev/doc/tutorial/database-access#add_data

func main() {
	err := godotenv.Load()
	util.CheckConfig()

	if err != nil {
		log.Fatal("Error loading .env file")
	}

	db, err := database.Connect()

	r := api.SetupRouter(&api.Handler{DB: db})
	http.Handle("/", r)

	serverPort := os.Getenv("SERVER_PORT")

	log.Println("Server started")
	handler := cors.Default().Handler(r)
	//handler = handlers.LoggingHandler(os.Stdout, handler)
	log.Fatal(http.ListenAndServe(serverPort, handler))

}
