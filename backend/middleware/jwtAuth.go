package middleware

import (
	"errors"
	"fmt"
	"github.com/golang-jwt/jwt/v5"
	"log"
	"net/http"
	"os"
	"time"
)

// https://www.golinuxcloud.com/golang-jwt/

// TODO refresh token and shift this to separate folder
// https://medium.com/monstar-lab-bangladesh-engineering/jwt-auth-in-go-dde432440924

type Claims struct {
	Username string `json:"username"`
	jwt.RegisteredClaims
}

func GenerateJwt(username string) (string, error) {
	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	jwtKey := []byte(os.Getenv("JWT_KEY"))

	claims["authorized"] = true
	claims["username"] = username
	claims["exp"] = time.Now().Add(time.Minute * 30).Unix()

	tokenString, err := token.SignedString(jwtKey)
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

func validateToken(w http.ResponseWriter, r *http.Request) (err error) {
	jwtKey := []byte(os.Getenv("JWT_KEY"))
	if r.Header["Token"] == nil {
		log.Println("Validate Token: cannot find token in header")
		_, _ = fmt.Fprintf(w, "cannot find token in header")
		return errors.New("cannot find token in header")
	}

	token, err := jwt.Parse(r.Header["Token"][0], func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("there was an error in parsing")
		}
		return jwtKey, nil
	})

	if token == nil {
		log.Println("Validate Token: invalid token")
		_, _ = fmt.Fprintf(w, "invalid token")
		return errors.New("invalid token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		_, _ = fmt.Fprintf(w, "couldn't parse claims")
		return errors.New("token error")
	}

	exp := claims["exp"].(float64)
	if int64(exp) < time.Now().Local().Unix() {
		_, _ = fmt.Fprintf(w, "token expired")
		return errors.New("token error")
	}

	return nil
}

func JwtAuthMiddleWare(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		err := validateToken(w, r)
		if err != nil {
			log.Println("JWT Auth Middleware Error: ", err)
			return
		}
		next.ServeHTTP(w, r)
	})
}
