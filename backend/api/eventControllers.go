package api

import (
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
)

// TODO check if the response I am sending is correct

func (h *Handler) HandleGetAllEvents(w http.ResponseWriter, r *http.Request) {
	log.Println("getAllEvents")

	sqlStatement := `SELECT e.event_id, e.event_name, e.event_body, e.event_start_date
					FROM events e
-- 					JOIN user_keep_track ukt on e.event_id = ukt.event_id
-- 					WHERE ukt.user_id = 1;` // TODO change this to the user id
	rows, err := h.DB.Query(sqlStatement)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	defer func(rows *sql.Rows) {
		err := rows.Close()
		if err != nil {
			panic(err)
		}
	}(rows)

	var events []Event
	for rows.Next() {
		var event Event
		if err := rows.Scan(&event.Name, &event.Body, &event.StartDate, &event.EndDate); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		events = append(events, event)
	}
	if err := rows.Err(); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")

	err = json.NewEncoder(w).Encode(events)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (h *Handler) HandleFollowEvent(writer http.ResponseWriter, request *http.Request) {

}

func (h *Handler) HandleUnfollowEvent(writer http.ResponseWriter, request *http.Request) {

}
