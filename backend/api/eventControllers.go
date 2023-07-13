package api

import (
	"context"
	"log"
	"net/http"
)

func (h *Handler) HandleGetAllEvents(w http.ResponseWriter, r *http.Request) {
	log.Println("getAllEvents")
	ctx := context.Background()

	allEvents, err := h.DB.GetAllEvents(ctx)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	NewJSONResponse(w, http.StatusOK, allEvents)
}

//func (h *Handler) HandleFollowEvent(writer http.ResponseWriter, request *http.Request) {
//
//}
//
//func (h *Handler) HandleUnfollowEvent(writer http.ResponseWriter, request *http.Request) {
//
//}
