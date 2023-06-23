package api

import "time"

type LoginRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

type RegisterRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
	Email    string `json:"email"`
}

// TODO put http codes in here

type HttpResponse struct {
	Message string `json:"message"`
}

type Post struct {
	Title        string    `json:"title"`
	Body         string    `json:"body"`
	CategoryName string    `json:"category_name"`
	CreatedBy    string    `json:"created_by"`
	CreatedAt    time.Time `json:"created_at"`
	LastUpdated  time.Time `json:"last_updated"`
}
