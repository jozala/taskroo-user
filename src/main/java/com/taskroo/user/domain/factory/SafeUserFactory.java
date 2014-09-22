package com.taskroo.user.domain.factory;

import com.taskroo.user.domain.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

@Component
public class SafeUserFactory {

    public User createUserWithHashedPassword(User user) {
        Objects.requireNonNull(user);
        String salt = generateNewSalt();
        String saltedPassword = saltPassword(user.getPassword(), salt);
        return new User(user.get_id(), user.getEmail(), user.getFirstName(), user.getLastName(), saltedPassword,
                user.getRoles(), user.isEnabled(), salt);
    }

    private String generateNewSalt() {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] salt = new byte[20];
        sr.nextBytes(salt);

        return (Base64.getEncoder().encodeToString(salt));
    }

    private String saltPassword(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), 8192, 160);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
