package db

import (
	"database/sql"
	_ "github.com/lib/pq"
	"log"
	"os"
)

func Connect() (*sql.DB, error) {
	//psqlHost := os.Getenv("PSQL_HOST")
	//psqlPort := os.Getenv("PSQL_PORT")
	//psqlUser := os.Getenv("PSQL_USER")
	//psqlPass := os.Getenv("PSQL_PASS")
	//psqlDbname := os.Getenv("PSQL_DBNAME")
	psqlConn := os.Getenv("PSQL_CONN")

	//psqlInfo := fmt.Sprintf("host=%s port=%s user=%s "+
	//	"password=%s dbname=%s sslmode=disable",
	//	psqlHost, psqlPort, psqlUser, psqlPass, psqlDbname)

	//db, err := sql.Open("postgres", psqlInfo)
	db, err := sql.Open("postgres", psqlConn)
	if err != nil {
		log.Println(err)
		panic(err)
	}

	// Note that this closes the db when Connect() returns so don't use it
	//defer func(db *sql.DB) {
	//	err := db.Close()
	//	if err != nil {
	//		panic(err)
	//	}
	//}(db)

	if err := db.Ping(); err != nil {
		log.Println(err)
		panic(err)
	}
	log.Println("Successfully connected!")

	//sqlStatement := `SELECT username, password FROM users WHERE username=$1 AND password=$2;`
	//var username string
	//var password string
	//err2 := db.QueryRow(sqlStatement, "user1", "password").Scan(&username, &password)
	//fmt.Println(err2)
	//fmt.Println(username, password)

	return db, err
}
