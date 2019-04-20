package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.scene.image.Image;

import octillect.database.firebase.FirestoreAPI;
import octillect.database.firebase.StorageAPI;
import octillect.models.User;

public class UserRepository {

    // add new user data to database.
    public static void add(User user) {
        FirestoreAPI.insertDocument(FirestoreAPI.USERS, user.getId(), user);
    }

    // add user's image to CloudStorage
    public static void setImage(String userId, String userImagePath) {
        StorageAPI.uploadImage(userImagePath, StorageAPI.USER_PHOTOS_FOLDER, userId);
    }

    public static User get(String id) {
        User user = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.USERS, id)).toObject(User.class);
        Image image = getImage(id);
        if (image != null)
            user.setImage(image);
        return user;
    }

    // Get user's image by userId
    public static Image getImage(String userId) {
        return StorageAPI.selectImage(StorageAPI.USER_PHOTOS_FOLDER, userId);
    }

    // Encryption method
    public static String encrypt(String textToEncrypt) {
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
