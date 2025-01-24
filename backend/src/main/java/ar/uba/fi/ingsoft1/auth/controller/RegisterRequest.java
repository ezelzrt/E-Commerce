package ar.uba.fi.ingsoft1.auth.controller;

public record RegisterRequest (
        String firstName,
        String lastName,
        String photo,
        Integer age,
        String gender,
        String address,
        String email,
        String password
) {}
