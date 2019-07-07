package main

import (
	"log"
	//"github.com/gorilla/mux"
	"net/http"
	"encoding/json"
	"time"
	zipkinhttp "github.com/openzipkin/zipkin-go/middleware/http"
	zipkin "github.com/openzipkin/zipkin-go"
)



type TraceRequest struct{
	Options []ServiceOptions `json:"options"`
}

type ServiceOptions struct {
	ServiceName string `json:"serviceName"`
	ThrowException bool `json:"throwException"`
	Delay int `json:"delay"`
}

func otherFunc(client *zipkinhttp.Client) http.HandlerFunc {

	return func(w http.ResponseWriter, r *http.Request) {
	
		log.Printf("trace handler")


		span := zipkin.SpanFromContext(r.Context())
		span.Tag("custom_key", "some value")

		for k, v := range r.Header {
			log.Printf("Header field %q, Value %q\n", k, v)
		}
		//params :=mux.Vars(r)
		var traceRequest TraceRequest
		_= json.NewDecoder(r.Body).Decode(&traceRequest)

		serviceOption := GetServiceOptions(traceRequest);

		if serviceOption == nil { 
			json.NewEncoder(w).Encode(traceRequest)
			return
		}

		if serviceOption.ThrowException {
			log.Printf("service-go throw exception")
			http.Error(w, http.StatusText(500), 500)
			return
		}

		if serviceOption.Delay > 0 {
			log.Printf("service-go delays")
			time.Sleep(time.Duration(serviceOption.Delay) * time.Millisecond)
		}

		json.NewEncoder(w).Encode(traceRequest)

		return
	}
}

func GetServiceOptions(traceRequest TraceRequest) (*ServiceOptions) {
		for _, opt := range traceRequest.Options {
			log.Printf(opt.ServiceName)
			if opt.ServiceName == "service-go" {
				return &opt
			}
		}
		return nil
}