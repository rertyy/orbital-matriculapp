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

// TODO put http codes into HttpResponse or find how to write it in response

type HttpResponse struct {
	Message string `json:"message"`
}

type AddPostRequest struct {
	CategoryId string `json:"category_id"`
	Post       Post   `json:"post"`
}

type Post struct {
	PostId        int       `json:"post_id"`
	Title         string    `json:"title"`
	Body          string    `json:"body"`
	CategoryId    int       `json:"category_id"`
	CategoryName  string    `json:"category_name"`
	CreatedBy     int       `json:"created_by"`
	CreatedByName string    `json:"created_by_name"`
	CreatedAt     time.Time `json:"created_at"`
	LastUpdated   time.Time `json:"last_updated"`
}

type Event struct {
	EventId   int    `json:"event_id"`
	Name      string `json:"name"`
	Body      string `json:"body"`
	StartDate string `json:"start_date"`
	EndDate   string `json:"end_date"`
}
