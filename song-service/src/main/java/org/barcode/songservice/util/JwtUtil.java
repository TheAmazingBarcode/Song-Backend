package org.barcode.songservice.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;


public class JwtUtil {

    private static JwtUtil instance;
    private final Algorithm algorithm;
    private final PrivateKey privateKey;

    private final PublicKey publicKey;


    private JwtUtil() {
        try {
            byte[] privateBytes = Files.readAllBytes(Path.of(URI.create("file:///home/mateja/Desktop/Development/Crypto/song-search-rsa-private/private.der")));
            byte[] publicBytes = Files.readAllBytes(Path.of(URI.create("file:///home/mateja/Desktop/Development/Crypto/song-search-rsa-private/public.der")));

            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateBytes);
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            this.privateKey = kf.generatePrivate(privateSpec);
            this.publicKey = kf.generatePublic(publicSpec);

            this.algorithm = Algorithm.RSA256((RSAPublicKey) this.publicKey
                    , (RSAPrivateKey) this.privateKey);


        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static JwtUtil getInstance() {
        if (instance == null) instance = new JwtUtil();
        return instance;
    }

    public String generateToken(Map<String,String> claims){
        return JWT.create()
                .withPayload(claims)
                .withIssuer("song-service-auth")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 300000L))
                .sign(this.algorithm);
    }
}
