package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import octillect.Main;
import octillect.database.documents.UserDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.database.firebase.StorageAPI;
import octillect.models.Project;
import octillect.models.User;
import octillect.models.builders.UserBuilder;

public class UserRepository {

    // add new user data to database.
    public static void add(User user) {

        UserDocument document = new UserDocument();
        document.setId(user.getId());
        document.setName(user.getName());
        document.setEmail(user.getEmail());
        document.setPassword(user.getPassword());
        /* TODO: Add Welcome Project to document here. */

        FirestoreAPI.insertDocument(FirestoreAPI.USERS, document.getId(), document);
        setImage(document.getId(), SwingFXUtils.fromFXImage(user.getImage(), null));
    }

    // add user's image to CloudStorage
    public static void setImage(String userId, BufferedImage userBufferedImage) {
        StorageAPI.uploadImage(userBufferedImage, StorageAPI.USER_PHOTOS_FOLDER, userId);
    }

    public static User get(String id) {

        User user = null;
        UserDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.USERS, id)).toObject(UserDocument.class);

        if (document != null) {

            user = new UserBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.email = document.getEmail();
                $.password = document.getPassword();
                $.image = getImage(document.getId());
                $.projects = FXCollections.observableArrayList();

                if (document.getProjectsIds() != null) {
                    ArrayList<Project> projectsIds = new ArrayList<>();
                    for (String projectId : document.getProjectsIds()) {
                        projectsIds.add(ProjectRepository.get(projectId));
                    }
                    $.projects = FXCollections.observableArrayList(projectsIds);
                }
            }).build();

        }

        return user;
    }

    public static User getContributor(String userId) {
        User user = null;
        UserDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.USERS, userId)).toObject(UserDocument.class);

        if (document != null) {

            user = new UserBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.email = document.getEmail();
                $.password = document.getPassword();
                $.image = getImage(document.getId());
            }).build();
        }

        return user;
    }

    // Assign project to a specific contributor
    public static void addProject(String projectId, String currentUserId) {
        FirestoreAPI.appendAttribute(FirestoreAPI.USERS, currentUserId, "projectsIds", projectId);
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


    /**
     * Generates an Identicon for any given User's ID,
     * where the ID must be hashed using any hashing algorithm.
     *
     * @param userId User's ID that will be converted into Identicon.
     * @param size   Width and Height of the Identicon.
     * @return Square BufferedImage of the Identicon.
     */
    public static BufferedImage generateIdenticon(String userId, int size) {

        int width = 5;
        int height = 5;

        byte[] hash = userId.getBytes();
        BufferedImage identicon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = identicon.getRaster();

        int[] background = new int[]{246, 246, 246, 255};
        int[] foreground = new int[]{hash[0] & 255, hash[1] & 255, hash[2] & 255, 255};

        for (int x = 0; x < width; x++) {

            int i = x < 3 ? x : 4 - x;

            for (int y = 0; y < height; y++) {
                if ((hash[i] >> y & 1) == 1) {
                    raster.setPixel(x, y, foreground);
                } else {
                    raster.setPixel(x, y, background);
                }
            }

        }

        // Scaling the Image
        BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        AffineTransform transform = new AffineTransform();
        transform.scale(size / width, size / height);

        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        scaledImage = transformOp.filter(identicon, scaledImage);

        return scaledImage;

    }

}
