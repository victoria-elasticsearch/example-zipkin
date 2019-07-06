package main

import (
	"github.com/gorilla/mux"
	"net/http"
	"log"
	zipkinhttp "github.com/openzipkin/zipkin-go/middleware/http"
)

func makeRouter(client *zipkinhttp.Client) *mux.Router {
    router := mux.NewRouter()
	// Add the URI /article to be handled by the ArticleHandler method
	router.Methods("POST").Path("/trace").HandlerFunc(otherFunc(client))


    // Add the URI / to be handled by a closure
    router.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		log.Printf("Root URI")
	})
	
    return router
}
