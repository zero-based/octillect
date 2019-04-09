package octillect.database.accessors;

import javafx.scene.image.Image;
import octillect.database.firebase.FirestoreAPI;
import octillect.database.firebase.StorageAPI;
import octillect.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    // add new user data to database and image to storage
    public static void add(User user, String userImagePath) {
        // hash user's email here and set it as a user id
        FirestoreAPI.AddDocument(FirestoreAPI.USERS, user.getId(), convertToMap(user));
        StorageAPI.uploadImage(userImagePath, StorageAPI.USER_PHOTOS_FOLDER, user.getId());
    }

    // Get user's image by userId
    public static Image getImage(String userId) {
        return StorageAPI.getImage(StorageAPI.USER_PHOTOS_FOLDER, userId);
    }

    // Helper method converts needed fields to a map to be saved later in the database
    private static Map<String, Object> convertToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
        return userMap;
    }
}
