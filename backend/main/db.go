package main

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	"log"
	"os"
)

func Connect() (*sql.DB, error) {
	psqlHost := os.Getenv("PSQL_HOST")
	psqlPort := os.Getenv("PSQL_PORT")
	psqlUser := os.Getenv("PSQL_USER")
	psqlPass := os.Getenv("PSQL_PASS")
	psqlDbname := os.Getenv("PSQL_DBNAME")

	psqlInfo := fmt.Sprintf("host=%s port=%s user=%s "+
		"password=%s dbname=%s sslmode=disable",
		psqlHost, psqlPort, psqlUser, psqlPass, psqlDbname)

	// not sure if reassignment err here is a good idea
	// note that if you introduce db first, don't shadow it
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

	//sqlStatement := `SELECT username, password FROM users WHERE username=$1 AND password=$2;`
	//row := db.QueryRow(sqlStatement, "user1", "password")
	//log.Println("row: ", row)

	return db, err
}
