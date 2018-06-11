package nure.ua.mediaclient.model;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";


    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
