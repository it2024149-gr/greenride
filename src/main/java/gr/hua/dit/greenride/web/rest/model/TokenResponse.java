package gr.hua.dit.greenride.web.rest.model;




public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {}
