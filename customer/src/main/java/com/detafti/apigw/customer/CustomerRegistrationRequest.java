package com.detafti.apigw.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
