package middleware

import (
	"github.com/gorilla/handlers"
	"net/http"
)

func EnableCorsMiddleware(next http.Handler) http.Handler {
	return handlers.CORS(
		handlers.AllowedOrigins([]string{"*"}),                                      // Allow any origin
		handlers.AllowedMethods([]string{"GET", "POST", "OPTIONS", "PATCH", "PUT"}), // Allow GET and POST methods
		handlers.AllowedHeaders([]string{"Content-Type"}),                           // Allow only "Content-Type" header
	)(next)
}
