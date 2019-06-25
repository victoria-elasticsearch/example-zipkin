package main

import (
	"log"
	//"github.com/gorilla/mux"
	"net/http"
	"encoding/json"
	"time"
)



type TraceRequest struct{
	Options []ServiceOptions `json:"options"`
}

type ServiceOptions struct {
	ServiceName string `json:"serviceName"`
	ThrowException bool `json:"throwException"`
	Delay int `json:"delay"`
}


func TraceHandler(w http.ResponseWriter, r *http.Request) {
	
	log.Printf("trace handler")

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
		log.Printf("Service B throw exception")
		http.Error(w, http.StatusText(500), 500)
		return
	}

	if serviceOption.Delay > 0 {
		log.Printf("Service B delays")
		time.Sleep(time.Duration(serviceOption.Delay) * time.Millisecond)
	}

	json.NewEncoder(w).Encode(traceRequest)

	return
}

func GetServiceOptions(traceRequest TraceRequest) (*ServiceOptions) {
		for _, opt := range traceRequest.Options {
			log.Printf(opt.ServiceName)
			if opt.ServiceName == "ServiceB" {
				return &opt
			}
		}
		return nil
}