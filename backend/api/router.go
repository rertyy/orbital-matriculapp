package api

import (
	"github.com/gorilla/mux"
	_ "github.com/lib/pq"
	"net/http"
	"orbital-backend/database/sql/sqlc"
	"orbital-backend/middleware"
)

type Handler struct {
	DB *sqlc.Queries
}

func SetupRouter(h *Handler) *mux.Router {
	r := mux.NewRouter()

	//r.Use(middleware.EnableCorsMiddleware)

	r.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		NewJSONResponse(w, http.StatusOK, struct {
			Success bool   `json:"success"`
			Message string `json:"message"`
		}{Success: true, Message: "Server is running"})
	}).Methods("GET")

	// User endpoints
	userRoute := r.PathPrefix("/user").Subrouter()
	userRoute.HandleFunc("/login", h.HandleLogin).Methods("POST", "OPTIONS")
	userRoute.HandleFunc("/register", h.HandleRegister).Methods("POST")
	userRoute.HandleFunc("/delete", h.HandleDeleteUser).Methods("DELETE")

	//// Forum Thread endpoints
	forumRoute := r.PathPrefix("/threads").Subrouter()
	forumRoute.HandleFunc("/", h.HandleGetAllThreads).Methods("GET")

	forumRoute.HandleFunc("/newThread", h.HandleAddThread).Methods("POST")
	forumRoute.HandleFunc("/{threadId}", h.HandleGetThread).Methods("GET")
	forumRoute.HandleFunc("/{threadId}/replies", h.HandleGetThreadReplies).Methods("GET")
	forumRoute.HandleFunc("/{threadId}/edit", h.HandleEditThread).Methods("PUT")
	forumRoute.HandleFunc("/{threadId}/delete", h.HandleDeleteThread).Methods("DELETE")

	forumRoute.HandleFunc("/{threadId}/addReply", h.HandleAddThreadReply).Methods("POST")

	//test
	//forumRoute.HandleFunc("/{threadId}/{replyId}", h.HandleGetReply).Methods("GET")
	//forumRoute.HandleFunc("/{threadId}/{replyId}/edit", h.HandleAddThreadReply).Methods("PUT")
	//forumRoute.HandleFunc("/{threadId}/delete", h.HandleDeleteReply).Methods("DELETE")

	// Event endpoints
	eventRoute := r.PathPrefix("/events").Subrouter()
	eventRoute.HandleFunc("/all", h.HandleGetAllEvents).Methods("GET")

	//forumRoute.Use(middleware.JwtAuthMiddleWare)
	//r.Use(middleware.EnableCorsMiddleware)
	r.Use(mux.CORSMethodMiddleware(r))
	r.Use(middleware.LoggerMiddleware)

	//headersOk := handlers.AllowedHeaders([]string{"Content-Type", "Authorization"})
	//originsOk := handlers.AllowedOrigins([]string{"*"})
	//methodsOk := handlers.AllowedMethods([]string{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"})
	//r.Use(handlers.CORS(headersOk, originsOk, methodsOk))
	//userRoute.Use(handlers.CORS(headersOk, originsOk, methodsOk))

	return r
}
