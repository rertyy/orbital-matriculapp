package util

import (
	"log"
	"os"
)

var envList = []string{
	"PSQL_CONN",
	"JWT_KEY",
	"SERVER_PORT",
}

func CheckConfig() {
	for _, key := range envList {
		if os.Getenv(key) == "" {
			log.Fatal(key + " not found in env")
		}
	}
	log.Println(".env config checked")
}
