package api

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"orbital-backend/api/sql/sqlc"
	"orbital-backend/util"
)

func (h *Handler) HandleLogin(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()

	var request sqlc.GetUserByUsernameRow

	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	userReq, err := h.DB.GetUserByUsername(ctx, request.Username)
	wrongUserNameOrPassword := "Wrong username or password"

	if err != nil {
		log.Println("HandleLogin: Username not found")
		_, _ = util.HashPassword("password") // line is solely here to set timeout for security reasons
		http.Error(w, wrongUserNameOrPassword, http.StatusUnauthorized)
		return
	}

	if err := util.CheckPassword(request.Password, userReq.Password); err != nil {
		log.Println("HandleLogin: CheckPassword", err)
		http.Error(w, wrongUserNameOrPassword, http.StatusUnauthorized)
		return
	}

	response := HttpResponse{
		Message: "Login successful",
	}

	NewJSONResponse(w, http.StatusOK, response)
}

// when testing, for security, check that failing username check is not faster than failing password check.
// E.g.
// (y, y), 100ms
// (y, n), 102ms
// (n, y), 108ms
// (n, n), 108ms
//
// maybe there is a better way than hashing a random string

func (h *Handler) HandleRegister(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()
	var request sqlc.AddUserParams
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	userExists, err := h.DB.CheckUserExists(ctx, request.Username)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if userExists {
		log.Println("HandleRegister: Username already exists in DB")
		http.Error(w, err.Error(), http.StatusConflict)
		return
	}

	hashedPassword, err := util.HashPassword(request.Password)
	if err != nil {
		log.Println("HandleRegister: Cannot hash password")
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	request.Password = hashedPassword

	if err := h.DB.AddUser(ctx, request); err != nil {
		log.Println("HandleRegister: Cannot add user")
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	response := HttpResponse{
		Message: "Registration successful",
	}
	NewJSONResponse(w, http.StatusCreated, response)
}

// HandleDeleteUser user must have correct username and password
func (h *Handler) HandleDeleteUser(w http.ResponseWriter, r *http.Request) {
	// check username exists
	ctx := context.Background()

	var request sqlc.GetUserByUsernameRow
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	userExists, err := h.DB.CheckUserExists(ctx, request.Username)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if !userExists {
		log.Println("HandleDeleteUser: user exists")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// check password is correct
	userReq, err := h.DB.GetUserByUsername(ctx, request.Username)
	if err := util.CheckPassword(request.Password, userReq.Password); err != nil {
		log.Println("HandleLogin: CheckPassword", err)
		http.Error(w, "Wrong password", http.StatusUnauthorized)
		return
	}

	if err := h.DB.DeleteUser(ctx, request.Username); err != nil {
		http.Error(w, "Cannot delete user", http.StatusInternalServerError)
		return
	}
	NewJSONResponse(w, http.StatusOK, HttpResponse{Message: "User deleted"})
}
