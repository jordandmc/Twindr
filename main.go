package main

import (
	"fantasy/domain"
	"fantasy/util"
	"fmt"
	"github.com/gorilla/mux"
	"golang.org/x/crypto/bcrypt"
	"net/http"
)

func LoginHandler(w http.ResponseWriter, r *http.Request) {
	username := r.Header.Get("username")
	password := r.Header.Get("password")
	if username != "" && password != "" {
		requestedUser := domain.GetUserByEmail(username)
		if requestedUser != nil {
			if bcrypt.CompareHashAndPassword([]byte(requestedUser.Password), []byte(password)) == nil {
				//TODO: create token in database and send that token instead of the word "token"
				fmt.Fprintf(w, "token")
			} else {
				//TODO send with status code 400 instead of 200
				fmt.Fprintf(w, "Error: Password Incorrect")
			}
		} else {
			//TODO send with status code 400 instead of 200
			fmt.Fprintf(w, "Error: Username does not exist")
		}
	} else {
		//TODO send with status code 400 instead of 200
		fmt.Fprintf(w, "Error: Invalid Login Header")
	}
}

func main() {
	util.InitDB()
	router := mux.NewRouter()
	router.HandleFunc("/login", LoginHandler).Methods("POST")
	http.ListenAndServe(":9000", router)
}
