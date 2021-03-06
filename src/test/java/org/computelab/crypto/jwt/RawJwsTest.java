package org.computelab.crypto.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RawJwsTest {

    private final JsonParser parser = new JsonParser();

    @Test
    public void testBase64UrlEncoding() {
        final String jwt =
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKb2UifQ." +
                "fstrLx7Q7fMxl6mgibNvmuoz0d2VzG5-EFYov-Zy" +
                "DrzflneYX276269fUCPhsU01cCorWDq4CuHu4RUN" +
                "2fZsEw";
        final RawJws token = RawJws.parseJwt(jwt);
        assertEquals("{\"alg\":\"HS512\"}", token.header());
        assertEquals("{\"sub\":\"Joe\"}", token.payload());
    }

    @Test
    public void testParseJwtHs256() {
        final String jwt =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
                "kpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA9" +
                "5OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
        final RawJws token = RawJws.parseJwt(jwt);
        final JsonObject expectedHeader = new JsonObject();
        expectedHeader.addProperty("alg", "HS256");
        expectedHeader.addProperty("typ", "JWT");
        assertEquals(expectedHeader, parser.parse(token.header()));
        final JsonObject expectedPayload = new JsonObject();
        expectedPayload.addProperty("sub", "1234567890");
        expectedPayload.addProperty("name", "John Doe");
        expectedPayload.addProperty("admin", true);
        assertEquals(expectedPayload, parser.parse(token.payload()));
        assertEquals("TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ", token.signature());
    }

    @Test
    public void testParseJwtRs256() {
        final String jwt =
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
                "kpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.EkN-D" +
                "OsnsuRjRO6BxXemmJDm3HbxrbRzXglbN2S4sO" +
                "kopdU4IsDxTI8jO19W_A4K8ZPJijNLis4EZsH" +
                "eY559a4DFOd50_OqgHGuERTqYZyuhtF39yxJP" +
                "AjUESwxk2J5k_4zM3O-vtd1Ghyo4IbqKKSy6J" +
                "9mTniYJPenn5-HIirE";
        final RawJws token = RawJws.parseJwt(jwt);
        final JsonObject expectedHeader = new JsonObject();
        expectedHeader.addProperty("alg", "RS256");
        expectedHeader.addProperty("typ", "JWT");
        assertEquals(expectedHeader, parser.parse(token.header()));
        final JsonObject expectedPayload = new JsonObject();
        expectedPayload.addProperty("sub", "1234567890");
        expectedPayload.addProperty("name", "John Doe");
        expectedPayload.addProperty("admin", true);
        assertEquals(expectedPayload, parser.parse(token.payload()));
        assertTrue(token.signature().startsWith("EkN-DOsnsuRjRO6Bx"));
        assertTrue(token.signature().endsWith("J9mTniYJPenn5-HIirE"));
    }

    @Test
    public void testParseNoSignature() {
        final String jwt =
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
                "kpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.";
        final RawJws token = RawJws.parseJwt(jwt);
        final JsonObject expectedHeader = new JsonObject();
        expectedHeader.addProperty("alg", "RS256");
        expectedHeader.addProperty("typ", "JWT");
        assertEquals(expectedHeader, parser.parse(token.header()));
        final JsonObject expectedPayload = new JsonObject();
        expectedPayload.addProperty("sub", "1234567890");
        expectedPayload.addProperty("name", "John Doe");
        expectedPayload.addProperty("admin", true);
        assertEquals(expectedPayload, parser.parse(token.payload()));
        assertTrue(token.signature().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTwoParts() {
        RawJws.parseJwt(
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
                "kpvaG4gRG9lIiwiYWRtaW4iOnRydWV9");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFourParts() {
        RawJws.parseJwt(
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
                "kpvaG4gRG9lIiwiYWRtaW4iOnRydWV9..");
    }
}
