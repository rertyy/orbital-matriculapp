package api

import (
	"context"
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"orbital-backend/api/sql/sqlc"
	"strconv"
)

func (h *Handler) HandleGetAllThreads(w http.ResponseWriter, _ *http.Request) {
	ctx := context.Background()
	threads, err := h.DB.GetAllThreads(ctx)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// TODO recheck with the frontend the best way to handle this
	if len(threads) == 0 {
		NewJSONResponse(w, http.StatusOK, HttpResponse{Message: "No threads found"})
		return
	}

	NewJSONResponse(w, http.StatusOK, threads)
}

func (h *Handler) HandleAddThread(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()
	log.Println("HandleAddThread")

	//vars := mux.Vars(r)
	//threadId := vars["threadId"]
	//log.Println("threadId is", threadId)
	//threadIdInt, err := strconv.Atoi(threadId)

	var thread sqlc.AddThreadParams
	err := json.NewDecoder(r.Body).Decode(&thread)
	if err != nil {
		log.Println("HandleAddThread: unable to decode")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	log.Println("thread is: ", thread)

	addThread, err := h.DB.AddThread(ctx, thread)
	if err != nil {
		log.Println("unable to insert thread", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	log.Println("thread added")
	NewJSONResponse(w, http.StatusCreated, addThread)
}

func (h *Handler) HandleGetThread(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()
	vars := mux.Vars(r)
	threadId := vars["threadId"]
	log.Println("threadId is", threadId)
	threadIdInt, err := strconv.Atoi(threadId)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	thread, err := h.DB.GetThreadById(ctx, int32(threadIdInt))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	NewJSONResponse(w, http.StatusOK, thread)
}

func (h *Handler) HandleGetThreadReplies(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	threadId := vars["threadId"]
	log.Println("threadId is", threadId)
	threadIdInt, err := strconv.Atoi(threadId)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	ctx := context.Background()
	replies, err := h.DB.GetRepliesByThread(ctx, int32(threadIdInt))
	if len(replies) == 0 {
		NewJSONResponse(w, http.StatusOK, HttpResponse{Message: "No threads found"})
		return
	}

	NewJSONResponse(w, http.StatusOK, replies)
}

func (h *Handler) HandleEditThread(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleEditPost")
	ctx := context.Background()
	//vars := mux.Vars(r)
	//threadId := vars["threadId"]
	//log.Println("threadId is", threadId)
	//threadIdInt, err := strconv.Atoi(threadId)

	var newPost sqlc.EditThreadParams
	err := json.NewDecoder(r.Body).Decode(&newPost)
	if err != nil {
		log.Println("unable to decode post")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	post, err := h.DB.EditThread(ctx, newPost)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	NewJSONResponse(w, http.StatusOK, post)
}

func (h *Handler) HandleDeleteThread(w http.ResponseWriter, r *http.Request) {
	log.Println("HandleDeleteThread")
	//vars := mux.Vars(r)
	//threadId := vars["threadId"]
	//log.Println("threadId is", threadId)
	//threadIdInt, err := strconv.Atoi(threadId)

	ctx := context.Background()
	var threadId int
	if err := json.NewDecoder(r.Body).Decode(&threadId); err != nil {
		log.Println("unable to delete post")
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	_, err := h.DB.GetThreadById(ctx, int32(threadId))
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
	err = h.DB.DeleteThread(ctx, int32(threadId))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	NewJSONResponse(w, http.StatusOK, HttpResponse{Message: "Post Deleted"})

}

func (h *Handler) HandleAddThreadReply(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()
	//vars := mux.Vars(r)
	//threadId := vars["threadId"]
	//log.Println("threadId is", threadId)
	//threadIdInt, err := strconv.Atoi(threadId)

	var reply sqlc.AddReplyParams
	err := json.NewDecoder(r.Body).Decode(&reply)
	if err != nil {
		log.Println("HandleAddThreadReply: unable to decode")
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	log.Println("reply is: ", reply)

	addPost, err := h.DB.AddReply(ctx, reply)
	if err != nil {
		log.Println("unable to insert reply", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

	log.Println("reply added")
	NewJSONResponse(w, http.StatusCreated, addPost)
}
