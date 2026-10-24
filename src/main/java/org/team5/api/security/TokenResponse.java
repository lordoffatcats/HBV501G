package org.team5.api.security;

public record TokenResponse(String jwtToken, String expiresIn) { }