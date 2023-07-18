package api

import (
	"context"
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
	"orbital-backend/api/sql/sqlc"
)

// TODO add in the rest of the handlers

//func (h *Handler) HandleGetCategories(w http.ResponseWriter, r *http.Request) {
//
//}
//
//func (h *Handler) HandleGetCategory(w http.ResponseWriter, r *http.Request) {
//
//}
//
//func (h *Handler) HandleGetCategoryPosts(w http.ResponseWriter, r *http.Request) {
//
//}

func (h *Handler) HandleAddPost(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()
	log.Println("HandleAddPost")
	//vars := mux.Vars(r)
	//categoryId := vars["categoryId"]
	//log.Println("categoryId is: ", categoryId)

	var post sqlc.AddPostParams
	err := json.NewDecoder(r.Body).Decode(&post)
	if err != nil {
		log.Println("HandleAddPost: unable to decode post")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	log.Println("post is: ", post)
	// NB as of rn the frontend is not sending post.CreatedAt intentionally.
	//log.Println("post createdAT is: ", post.CreatedAt)
	//log.Println("post LastUpdated is: ", post.LastUpdated)

	addPost, err := h.DB.AddPost(ctx, post)
	if err != nil {
		log.Println("unable to insert post", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	log.Println("post added")
	NewJSONResponse(w, http.StatusCreated, addPost)
}

//
//func (h *Handler) HandleAddCategory(w http.ResponseWriter, r *http.Request) {
//
//}
//
//func (h *Handler) HandleGetPost(w http.ResponseWriter, r *http.Request) {
//
//}
//

func (h *Handler) HandleEditPost(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleEditPost")
	ctx := context.Background()
	//vars := mux.Vars(r)
	//categoryId := vars["categoryId"]
	//postId := vars["postId"]
	//log.Println("categoryId is", categoryId)
	//log.Println("postId is: ", postId)

	var newPost sqlc.UpdatePostParams
	err := json.NewDecoder(r.Body).Decode(&newPost)
	if err != nil {
		log.Println("unable to decode post")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	post, err := h.DB.UpdatePost(ctx, newPost)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	NewJSONResponse(w, http.StatusOK, post)
}

func (h *Handler) HandleDeletePost(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleDeletePost")
	//vars := mux.Vars(r)
	//postId := vars["postId"]
	//log.Println("postId is: ", postId)

	ctx := context.Background()
	var postId int
	if err := json.NewDecoder(r.Body).Decode(&postId); err != nil {
		log.Println("unable to delete post")
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	_, err := h.DB.GetPostById(ctx, int32(postId))
	switch err {
	case sql.ErrNoRows:
		http.Error(w, "post not found", http.StatusNotFound)
		return
	case nil:
		// do nothing
	default:
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	err = h.DB.DeletePost(ctx, int32(postId))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	NewJSONResponse(w, http.StatusOK, HttpResponse{Message: "Post Deleted"})

}

// func (h *Handler) HandleDeleteCategory(w http.ResponseWriter, r *http.Request) {
//
// }
//
// func (h *Handler) HandleEditCategory(w http.ResponseWriter, r *http.Request) {
//
// }

func (h *Handler) HandleGetAllPosts(w http.ResponseWriter, _ *http.Request) {
	ctx := context.Background()
	posts, err := h.DB.GetAllPosts(ctx)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// TODO recheck with the frontend the best way to handle this
	if len(posts) == 0 {
		NewJSONResponse(w, http.StatusOK, HttpResponse{Message: "No posts found"})
		return
	}

	NewJSONResponse(w, http.StatusOK, posts)
}
