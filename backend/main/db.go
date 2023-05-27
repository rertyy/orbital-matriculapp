package main

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	"log"
)

// test

const (
	host     = "localhost"
	port     = 5432
	user     = "postgres"
	password = "password"
	dbname   = "orbitaldb"
)

func main() {
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s "+
		"password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)
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
	fmt.Println("Successfully connected!")
	log.Println("Successfully connected!")

	// // Insert
	//sqlStatement := `INSERT INTO users (age, email, first_name, last_name)
	//	VALUES ($1, $2, $3, $4)
	//	RETURNING id`
	//id := 0
	//err = db.QueryRow(sqlStatement, 30, "jon2@calhoun.io", "Jonathan", "Calhoun").Scan(&id)
	//if err != nil {
	//	panic(err)
	//}
	//fmt.Println("New record ID is:", id)
	//log.Println("New record ID is:", id)

	// // Update
	//sqlStatement := `UPDATE users
	//		SET first_name = $2, last_name = $3
	//		WHERE id = $1
	//		RETURNING id, email;`
	//var email string
	//var id int
	//err = db.QueryRow(sqlStatement, 1, "NewFirst", "NewLast").Scan(&id, &email)
	//
	//if err != nil {
	//	panic(err)
	//}
	//fmt.Println(id, email)

	sqlStatement := `SELECT id, email FROM users WHERE id=$1;`
	var email string
	var id int
	// Replace 3 with an ID from your database or another random
	// value to test the no rows use case.
	row := db.QueryRow(sqlStatement, 3)
	switch err := row.Scan(&id, &email); err {
	case sql.ErrNoRows:
		fmt.Println("No rows were returned!")
	case nil:
		fmt.Println(id, email)
	default:
		panic(err)
	}

}
