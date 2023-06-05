package util

import (
	"math/rand"
	"strings"
	"time"
)

func init() {
	rand.New(rand.NewSource(time.Now().UnixNano()))
}

var alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
	"1234567890" +
	"!@#$%^&*()_+-=[]{}|;:',./<>?\"\\"

func RandomString(n int) string {
	var sb strings.Builder
	k := len(alphabet)

	for i := 0; i < n; i++ {
		c := alphabet[rand.Intn(k)]
		sb.WriteByte(c)
	}

	return sb.String()
}
