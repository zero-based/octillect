package octillect.database.accessors;

import javafx.scene.image.Image;
import octillect.database.firebase.FirestoreAPI;
import octillect.database.firebase.StorageAPI;
import octillect.models.User;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    // add new user data to database.
    public static void add(User user) {
        // hash user's email here and set it as a user id
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
        FirestoreAPI.AddDocument(FirestoreAPI.USERS, user.getId(), userMap);
    }

    // add user's image to CloudStorage
    public static void setImage(String userId, String userImagePath) {
        StorageAPI.uploadImage(userImagePath, StorageAPI.USER_PHOTOS_FOLDER, userId);
    }

    // Get user's image by userId
    public static Image getImage(String userId) {
        return StorageAPI.getImage(StorageAPI.USER_PHOTOS_FOLDER, userId);
    }

    // Encryption method
    public String encrypt(String textToEncrypt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(textToEncrypt.getBytes());
            BigInteger bigInteger = new BigInteger(1, messageDigest);
            String hashtext = bigInteger.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
