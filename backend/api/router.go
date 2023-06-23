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

	// User endpoints
	r.HandleFunc("/login", h.HandleLogin).Methods("POST")
	r.HandleFunc("/register", h.HandleRegister).Methods("POST")

	// Forum endpoints
	r.HandleFunc("/categories", h.HandleGetCategories).Methods("GET")           // show all categories
	r.HandleFunc("/:categoryId", h.HandleGetCategory).Methods("GET")            //
	r.HandleFunc("/:categoryId/posts", h.HandleGetCategoryPosts).Methods("GET") // show all posts in a category
	r.HandleFunc("/:categoryId/:postId", h.HandleGetPost).Methods("GET")        // show a specific post in a category

	r.HandleFunc("/:categoryId/:postId", h.HandleModifyPost).Methods("PUT")
	r.HandleFunc("/:categoryId/", h.HandleModifyCategory).Methods("PUT")
	r.HandleFunc("/:postID", h.HandleDeletePost).Methods("DELETE")
	r.HandleFunc("/:categoryId", h.HandleDeleteCategory).Methods("DELETE")

	return r
}
