package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.image.Image;

import octillect.Main;
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

    /**
     * Save user's id and hashed password to .octillect file
     *
     * @param user signing user
     */
    public static void rememberUser(User user) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        /* Use Map to convert a key/value object to a json object*/
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", user.getId());

        String userCredentials = gson.toJson(map);

        try {
            Main.octillectFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(Main.octillectFile);
            outputStream.write(userCredentials.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the user from database by id saved in .octillect file.
     *
     * @return a user object filled from the database.
     */
    public static User getRememberedUser() {

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(Main.octillectFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkedHashMap<String, String> fileContent;
        Gson gson = new Gson();
        fileContent = gson.fromJson(fileReader, LinkedHashMap.class);
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UserRepository.get(fileContent.get("id"));
    }
}
