package api

import (
	"github.com/gorilla/mux"
	_ "github.com/lib/pq"
	"net/http"
	"orbital-backend/api/sql/sqlc"
	"orbital-backend/middleware"
)

type Handler struct {
	DB *sqlc.Queries
}

func SetupRouter(h *Handler) *mux.Router {
	r := mux.NewRouter()

	r.HandleFunc("/ok", func(w http.ResponseWriter, r *http.Request) {
		NewJSONResponse(w, http.StatusOK, struct{ success bool }{success: true})
	}).Methods("GET")

	// User endpoints
	userRoute := r.PathPrefix("/user").Subrouter()
	userRoute.HandleFunc("/login", h.HandleLogin).Methods("POST")
	userRoute.HandleFunc("/register", h.HandleRegister).Methods("POST")
	userRoute.HandleFunc("/delete", h.HandleDeleteUser).Methods("DELETE")

	//// Forum endpoints
	forumRoute := r.PathPrefix("/forum").Subrouter()
	forumRoute.HandleFunc("/posts", h.HandleGetAllPosts).Methods("GET") // show all posts
	////r.HandleFunc("/addCategory", h.HandleAddCategory).Methods("POST")
	////r.HandleFunc("/categories", h.HandleGetCategories).Methods("GET") // show all categories
	////r.HandleFunc("/{categoryId}", h.HandleGetCategory).Methods("GET") // show a specific category
	forumRoute.HandleFunc("/addPost", h.HandleAddPost).Methods("POST")
	////r.HandleFunc("/{categoryId}/posts", h.HandleGetCategoryPosts).Methods("GET") // show all posts in a category
	////r.HandleFunc("/{categoryId}/{postId}", h.HandleGetPost).Methods("GET")       // show a specific post in a category
	forumRoute.HandleFunc("/{postId}/edit", h.HandleEditPost).Methods("PUT")
	////r.HandleFunc("/{categoryId}/", h.HandleEditCategory).Methods("PUT")
	forumRoute.HandleFunc("/{postID}/delete", h.HandleDeletePost).Methods("DELETE")
	////r.HandleFunc("/{categoryId}", h.HandleDeleteCategory).Methods("DELETE")

	// Event endpoints
	eventRoute := r.PathPrefix("/event").Subrouter()
	eventRoute.HandleFunc("/getEvents", h.HandleGetAllEvents).Methods("GET")
	////r.HandleFunc("/{eventsId}", h.HandleGetEvent).Methods("GET")
	////r.HandleFunc("/{eventId}/follow", h.HandleFollowEvent).Methods("POST")
	////r.HandleFunc("/{eventId}/unfollow", h.HandleFollowEvent).Methods("POST")

	forumRoute.Use(middleware.JwtAuthMiddleWare)
	r.Use(mux.CORSMethodMiddleware(r))
	return r
}
