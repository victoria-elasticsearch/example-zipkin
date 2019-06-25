package main

import (
    "net/http"
)

func main() {
    // Create a new Router instance
    router := makeRouter()

    // Pass our router to net/http
    http.Handle("/", router)

    // Listen and serve on localhost:8080
    http.ListenAndServe(":8080", nil)
}