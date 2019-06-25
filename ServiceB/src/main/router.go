package main

import (
	"github.com/gorilla/mux"
	"net/http"
	"log"
)

func makeRouter() *mux.Router {
    router := mux.NewRouter()
	// Add the URI /article to be handled by the ArticleHandler method
    router.HandleFunc("/trace", TraceHandler).Methods("POST")

    // Add the URI / to be handled by a closure
    router.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		log.Printf("Root URI")
	})
	
    return router
}
