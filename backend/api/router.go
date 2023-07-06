package api

import (
	"github.com/gorilla/mux"
	_ "github.com/lib/pq"
	"net/http"
	"orbital-backend/api/sql/sqlc"
)

type Handler struct {
	DB *sqlc.Queries
}

func SetupRouter(h *Handler) *mux.Router {
	r := mux.NewRouter()

	// TODO check whether a sub-router is better

	r.HandleFunc("/ok", func(w http.ResponseWriter, r *http.Request) {
		NewJSONResponse(w, http.StatusOK, struct{ success bool }{success: true})
	}).Methods("GET")

	// User endpoints
	r.HandleFunc("/login", h.HandleLogin).Methods("POST")
	r.HandleFunc("/register", h.HandleRegister).Methods("POST")
	r.HandleFunc("/delete", h.HandleDeleteUser).Methods("DELETE")

	//// Forum endpoints
	r.HandleFunc("/posts", h.HandleGetAllPosts).Methods("GET") // show all posts
	//
	////r.HandleFunc("/addCategory", h.HandleAddCategory).Methods("POST")
	////r.HandleFunc("/categories", h.HandleGetCategories).Methods("GET") // show all categories
	////r.HandleFunc("/{categoryId}", h.HandleGetCategory).Methods("GET") // show a specific category
	r.HandleFunc("/addPost", h.HandleAddPost).Methods("POST")
	////r.HandleFunc("/{categoryId}/posts", h.HandleGetCategoryPosts).Methods("GET") // show all posts in a category
	////r.HandleFunc("/{categoryId}/{postId}", h.HandleGetPost).Methods("GET")       // show a specific post in a category
	//
	r.HandleFunc("/{postId}/edit", h.HandleEditPost).Methods("PUT")
	////r.HandleFunc("/{categoryId}/", h.HandleEditCategory).Methods("PUT")
	r.HandleFunc("/{postID}/delete", h.HandleDeletePost).Methods("DELETE")
	////r.HandleFunc("/{categoryId}", h.HandleDeleteCategory).Methods("DELETE")
	//
	//// Event endpoints
	//r.HandleFunc("/events", h.HandleGetAllEvents).Methods("GET")
	////r.HandleFunc("/{eventsId}", h.HandleGetEvent).Methods("GET")
	////r.HandleFunc("/{eventId}/follow", h.HandleFollowEvent).Methods("POST")
	////r.HandleFunc("/{eventId}/unfollow", h.HandleFollowEvent).Methods("POST")

	return r
}
