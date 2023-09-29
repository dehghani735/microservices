package com.detafti.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
