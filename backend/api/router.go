package api

import (
	"database/sql"
	"github.com/gorilla/mux"
	_ "github.com/lib/pq"
)

type Handler struct {
	DB *sql.DB
}

func SetupRouter(h *Handler) *mux.Router {
	r := mux.NewRouter()

	r.HandleFunc("/login", h.HandleLogin).Methods("POST")
	r.HandleFunc("/register", h.HandleRegister).Methods("POST")

	return r
}
