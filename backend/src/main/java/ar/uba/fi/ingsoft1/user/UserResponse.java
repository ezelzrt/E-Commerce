package ar.uba.fi.ingsoft1.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("email") String email,
        @JsonProperty("photo") String photo,
        @JsonProperty("age") Integer age,
        @JsonProperty("gender") String gender,
        @JsonProperty("address") String address,
        @JsonProperty("emailVerified") boolean emailVerified
) {}
