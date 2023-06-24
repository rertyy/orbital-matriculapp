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

// TODO put http codes into HttpResponse

type HttpResponse struct {
	Message string `json:"message"`
}

type AddPostRequest struct {
	CategoryId string `json:"category_id"`
	Post       Post   `json:"post"`
}

// Post TODO add id to the structs so that we can use it to delete and modify
// intentionally is a string because json.decode cannot handle time.Time without time.Parse
type Post struct {
	Title         string    `json:"title"`
	Body          string    `json:"body"`
	CategoryId    int       `json:"category_id"`
	CategoryName  string    `json:"category_name"`
	CreatedBy     int       `json:"created_by"`
	CreatedByName string    `json:"created_by_name"`
	CreatedAt     time.Time `json:"created_at"`
	LastUpdated   time.Time `json:"last_updated"`
}

// Event TODO add id to the struct
type Event struct {
	Name      string `json:"name"`
	Body      string `json:"body"`
	StartDate string `json:"start_date"`
	EndDate   string `json:"end_date"`
}
