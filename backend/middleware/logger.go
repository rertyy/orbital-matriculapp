package middleware

import (
	"bytes"
	"log"
	"net/http"
)

func LoggerMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Log the incoming request details
		log.Printf("Incoming request: %s %s from %s\n", r.Method, r.URL.Path, r.RemoteAddr)

		// Create a buffer to capture the response body
		buf := new(bytes.Buffer)
		// Wrap the original ResponseWriter with our custom responseWriter
		responseWriter := &responseWriter{ResponseWriter: w, status: http.StatusOK, body: buf}

		// Call the next handler in the chain and capture the response
		next.ServeHTTP(responseWriter, r)

		// Log the response details (status code and status text)
		log.Printf("Response: %d %s\n", responseWriter.status, http.StatusText(responseWriter.status))

		// Log the response body (limited to 512 bytes for illustration)
		responseBody := buf.String()
		if len(responseBody) > 512 {
			responseBody = responseBody[:512] + "..."
		}
		log.Printf("Response Body: %s\n", responseBody)
	})
}

// Custom responseWriter to capture the response status code and body
type responseWriter struct {
	http.ResponseWriter
	status int
	body   *bytes.Buffer
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.status = code
	rw.ResponseWriter.WriteHeader(code)
}

func (rw *responseWriter) Write(b []byte) (int, error) {
	// Capture the response body
	n, err := rw.body.Write(b)
	if err != nil {
		return n, err
	}
	return rw.ResponseWriter.Write(b)
}
