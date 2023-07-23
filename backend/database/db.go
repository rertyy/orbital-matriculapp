package database

import (
	"database/sql"
	_ "github.com/lib/pq"
	"log"
	"orbital-backend/database/sql/sqlc"
	"os"
)

func Connect() (*sqlc.Queries, error) {
	//psqlHost := os.Getenv("PSQL_HOST")
	//psqlPort := os.Getenv("PSQL_PORT")
	//psqlUser := os.Getenv("PSQL_USER")
	//psqlPass := os.Getenv("PSQL_PASS")
	//psqlDbname := os.Getenv("PSQL_DBNAME")
	//psqlInfo := fmt.Sprintf("host=%s port=%s user=%s "+
	//	"password=%s dbname=%s sslmode=disable",
	//	psqlHost, psqlPort, psqlUser, psqlPass, psqlDbname)
	//db, err := sql.Open("postgres", psqlInfo)

	psqlConn := os.Getenv("PSQL_CONN")
	db, err := sql.Open("postgres", psqlConn)
	if err != nil {
		log.Println(err)
		panic(err)
	}

	// Note that this closes the database when Connect() returns so don't use it
	//defer func(database *sql.DB) {
	//	err := database.Close()
	//	if err != nil {
	//		panic(err)
	//	}
	//}(database)

	if err := db.Ping(); err != nil {
		log.Println(err)
		panic(err)
	}
	log.Println("Successfully connected to db!")
	queries := sqlc.New(db)

	return queries, err
}
