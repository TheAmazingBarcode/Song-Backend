package org.barcode.songservice.service;

import org.barcode.songservice.model.User;
import org.barcode.songservice.repository.UserRepo;
import org.barcode.songservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder(6);
    }

    public String registerUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        User userCopy = userRepo.save(user);
        return jwtUtil.generateToken(generateClaims(userCopy));
    }

    public ResponseEntity<String> logInUser(Map<String, String> details) {

        if (details.size() != 2)
            return ResponseEntity
                    .status(HttpURLConnection.HTTP_UNAUTHORIZED)
                    .body("error");

        User user = userRepo.findUserByEmail(details.get("email"));

        if (user == null || !passwordEncoder.matches(details.get("password"), user.getPassword()))
            return ResponseEntity
                    .status(HttpURLConnection.HTTP_UNAUTHORIZED)
                    .body("wrong credentials");

        return ResponseEntity.ok(jwtUtil.generateToken(generateClaims(user)));
    }

    private Map<String, String> generateClaims(User user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", "user"); //Possibly add a dedicated role column in the database?
        return claims;
    }

}
