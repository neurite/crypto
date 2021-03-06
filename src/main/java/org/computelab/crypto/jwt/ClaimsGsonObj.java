package org.computelab.crypto.jwt;

/**
 * Claims object that can be serialized/deserialized by gson.
 */
final class ClaimsGsonObj implements Claims {

    private String iss;
    private String sub;
    private String aud;
    private Long exp;
    private Long nbf;
    private Long iat;
    private String jti;

    @Override
    public String issuer() {
        return iss;
    }
    @Override
    public String subject() {
        return sub;
    }
    @Override
    public String audience() {
        return aud;
    }
    @Override
    public Long expiration() {
        return exp;
    }
    @Override
    public Long notBefore() {
        return nbf;
    }
    @Override
    public Long issuedAt() {
        return iat;
    }
    @Override
    public String jwtId() {
        return jti;
    }
}
