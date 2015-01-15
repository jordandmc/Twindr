package domain

import (
	"fantasy/util"
)

type User struct {
	Id       string
	Name     string
	Email    string
	Password string
}

func GetUserById(id string) *User {
	result := new(User)
	conn := util.GetTransaction()
	row := conn.QueryRow("SELECT * FROM users WHERE id=$1", id)
	scanErr := row.Scan(&result.Id, &result.Name, &result.Email, &result.Password)
	if scanErr == nil {
		return nil
	}
	return result
}

func GetUserByEmail(email string) *User {
	result := new(User)
	conn := util.GetTransaction()
	row := conn.QueryRow("SELECT * FROM users WHERE email=$1", email)
	scanErr := row.Scan(&result.Id, &result.Name, &result.Email, &result.Password)
	if scanErr != nil {
		return nil
	}
	return result
}
