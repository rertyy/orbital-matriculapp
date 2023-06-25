package api

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"log"
	"net/http"
)

// TODO clean the logic

// TODO add in the rest of the handlers

func (h *Handler) HandleGetCategories(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetCategory(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetCategoryPosts(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleAddPost(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleAddPost")
	vars := mux.Vars(r)
	categoryId := vars["categoryId"]
	log.Println("categoryId is: ", categoryId)

	var post Post
	err := json.NewDecoder(r.Body).Decode(&post)
	if err != nil {
		log.Println("unable to decode post")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	log.Println("post is: ", post)
	// NB as of rn the frontend is not sending post.CreatedAt intentionally.
	//log.Println("post createdAT is: ", post.CreatedAt)
	//log.Println("post LastUpdated is: ", post.LastUpdated)
	sqlStatement := `INSERT INTO posts (title, body, category_id, created_by) 
    					VALUES ($1, $2, $3, $4);`

	_, err = h.DB.Exec(sqlStatement, post.Title, post.Body, categoryId, post.CreatedBy)
	if err != nil {
		log.Println("unable to insert post", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
	} else {
		log.Println("post added")
		response := HttpResponse{
			Message: "post added",
		}
		w.Header().Set("Content-Type", "application/json")
		err = json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Println("unable to encode response", err)
		}
	}
	w.WriteHeader(http.StatusCreated)
}

func (h *Handler) HandleAddCategory(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetPost(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleEditPost(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleEditPost")
	vars := mux.Vars(r)
	categoryId := vars["categoryId"]
	postId := vars["postId"]
	log.Println("categoryId is", categoryId)
	log.Println("postId is: ", postId)

	var newPost Post
	err := json.NewDecoder(r.Body).Decode(&newPost)
	if err != nil {
		log.Println("unable to decode post")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	log.Println("post is: ", newPost)

	// TODO handle cases where updating values which dont exist
	sqlStatement := `UPDATE posts SET title=$1, body=$2 WHERE post_id=$4;`
	_, err = h.DB.Exec(sqlStatement, postId)
	if err != nil {
		log.Println("unable to delete post")
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
	err = json.NewEncoder(w).Encode(HttpResponse{Message: "post updated"})
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
}

func (h *Handler) HandleDeletePost(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleDeletePost")
	vars := mux.Vars(r)
	postId := vars["postId"]
	log.Println("postId is: ", postId)

	sqlStatement := `DELETE FROM posts WHERE post_id=$1;`
	_, err := h.DB.Exec(sqlStatement, postId)
	if err != nil {
		log.Println("unable to delete post")
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	w.WriteHeader(http.StatusNoContent)

}

func (h *Handler) HandleDeleteCategory(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleEditCategory(w http.ResponseWriter, r *http.Request) {

}

func (h *Handler) HandleGetAllPosts(w http.ResponseWriter, r *http.Request) {
	posts, err := h.getAllPosts()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")

	err = json.NewEncoder(w).Encode(posts)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
}

// TODO handle no rows returned
func (h *Handler) getAllPosts() ([]Post, error) {
	log.Println("getAllPosts")
	// TODO rn its hardcoded to WHERE cat_id == 1
	sqlStatement := `SELECT p.post_id, p.title, p.body, p.category_id, c.cat_name, p.created_by, u.username, p.created_at, p.last_updated
					FROM posts p
					JOIN categories c ON p.category_id = c.cat_id
					JOIN users u ON p.created_by = u.user_id
					--WHERE p.category_id = 1;`
	rows, err := h.DB.Query(sqlStatement)
	if err != nil {
		return nil, err
	}

	// you only need to defer func rows.Close() when using Query statement which returns multiple results
	defer func(rows *sql.Rows) {
		err := rows.Close()
		if err != nil {
			panic(err)
		}
	}(rows)

	var posts []Post
	for rows.Next() {
		var post Post
		if err := rows.Scan(&post.PostId, &post.Title, &post.Body,
			&post.CategoryId, &post.CategoryName, &post.CreatedBy,
			&post.CreatedByName, &post.CreatedAt, &post.LastUpdated); err != nil {
			return posts, err
		}
		posts = append(posts, post)
	}
	if err = rows.Err(); err != nil {
		return posts, err
	}
	return posts, nil
}
