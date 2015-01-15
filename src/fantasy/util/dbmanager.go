package util

import (
	"database/sql"
	_ "github.com/lib/pq"
	"log"
)

type dbmanager struct {
	db *sql.DB
}

var db *dbmanager

func InitDB() {
	db = NewDBManager()
}

func NewDBManager() *dbmanager {
	db, err := sql.Open("postgres", "user=fantasy password=password dbname=fantasy sslmode=disable")

	if err != nil {
		log.Fatal(err)
	}

	manager := new(dbmanager)
	manager.db = db
	return manager
}

func GetTransaction() *sql.Tx {
	conn, err := db.db.Begin()
	if err != nil {
		log.Fatal(err)
	}
	return conn
}
