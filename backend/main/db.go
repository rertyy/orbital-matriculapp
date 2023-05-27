package main

import (
	_ "github.com/lib/pq"
)

// TODO make an .env or a .config file to store these values

/*
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
	// Replace 3 with an ID from your database or another random
	// value to test the no rows use case.
	row := db.QueryRow(sqlStatement, username, password)
	switch err := row.Scan(&username, &password); err {
	case sql.ErrNoRows:
		log.Println("Wrong username of password")
		return false, err
	case nil:
		log.Println("Login successful")
		return true, nil
	default:
		log.Println("Wrong username of password")
		return false, err
	}

}


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
*/
