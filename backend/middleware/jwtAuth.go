package middleware

import (
	"fmt"
	"log"
	"net/http"
	"orbital-backend/util"
)

func JwtAuthMiddleWare(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		err := util.ValidateToken(w, r)
		if err != nil {
			log.Println("JWT Auth Middleware Error: ", err)
			err := fmt.Errorf("JWT Auth Middleware Error: %v", err)
			http.Error(w, err.Error(), http.StatusUnauthorized)
			return
		}
		next.ServeHTTP(w, r)
	})
}
