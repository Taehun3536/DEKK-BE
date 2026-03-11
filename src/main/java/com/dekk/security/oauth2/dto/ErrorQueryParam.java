package com.dekk.security.oauth2.dto;

public record ErrorQueryParam(String error, String provider) {
    public static ErrorQueryParam of(String error, String provider) {
        return new ErrorQueryParam(error, provider);
    }
}
