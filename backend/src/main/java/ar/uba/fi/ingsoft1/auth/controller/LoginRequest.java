package ar.uba.fi.ingsoft1.auth.controller;

public record LoginRequest (
        String email,
        String password
){
}
