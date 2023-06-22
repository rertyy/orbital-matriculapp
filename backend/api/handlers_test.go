package api

import (
	"database/sql"
	"net/http"
	"reflect"
	"testing"
)

func TestHandler_HandleGetAllPosts(t *testing.T) {
	type fields struct {
		DB *sql.DB
	}
	type args struct {
		w http.ResponseWriter
		r *http.Request
	}
	tests := []struct {
		name   string
		fields fields
		args   args
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			h := &Handler{
				DB: tt.fields.DB,
			}
			h.HandleGetAllPosts(tt.args.w, tt.args.r)
		})
	}
}

func TestHandler_HandleLogin(t *testing.T) {
	type fields struct {
		DB *sql.DB
	}
	type args struct {
		w http.ResponseWriter
		r *http.Request
	}
	tests := []struct {
		name   string
		fields fields
		args   args
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			h := &Handler{
				DB: tt.fields.DB,
			}
			h.HandleLogin(tt.args.w, tt.args.r)
		})
	}
}

func TestHandler_HandleRegister(t *testing.T) {
	type fields struct {
		DB *sql.DB
	}
	type args struct {
		w http.ResponseWriter
		r *http.Request
	}
	tests := []struct {
		name   string
		fields fields
		args   args
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			h := &Handler{
				DB: tt.fields.DB,
			}
			h.HandleRegister(tt.args.w, tt.args.r)
		})
	}
}

func TestHandler_getAllPosts(t *testing.T) {
	type fields struct {
		DB *sql.DB
	}
	tests := []struct {
		name    string
		fields  fields
		want    []Post
		wantErr bool
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			h := &Handler{
				DB: tt.fields.DB,
			}
			got, err := h.getAllPosts()
			if (err != nil) != tt.wantErr {
				t.Errorf("getAllPosts() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("getAllPosts() got = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestHandler_loginRequest(t *testing.T) {
	type fields struct {
		DB *sql.DB
	}
	type args struct {
		username string
		password string
	}
	tests := []struct {
		name    string
		fields  fields
		args    args
		want    bool
		wantErr bool
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			h := &Handler{
				DB: tt.fields.DB,
			}
			got, err := h.loginRequest(tt.args.username, tt.args.password)
			if (err != nil) != tt.wantErr {
				t.Errorf("loginRequest() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if got != tt.want {
				t.Errorf("loginRequest() got = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestHandler_registerRequest(t *testing.T) {
	type fields struct {
		DB *sql.DB
	}
	type args struct {
		username string
		password string
	}
	tests := []struct {
		name    string
		fields  fields
		args    args
		want    bool
		wantErr bool
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			h := &Handler{
				DB: tt.fields.DB,
			}
			got, err := h.registerRequest(tt.args.username, tt.args.password)
			if (err != nil) != tt.wantErr {
				t.Errorf("registerRequest() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if got != tt.want {
				t.Errorf("registerRequest() got = %v, want %v", got, tt.want)
			}
		})
	}
}
