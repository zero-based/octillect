package octillect.database.repositories;

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
import octillect.models.*;
import octillect.models.builders.ContributorBuilder;
import octillect.models.builders.UserBuilder;

public class UserRepository implements Repository<User> {

    private static UserRepository ourInstance = new UserRepository();

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private UserRepository() {
    }


    @Override
    public void add(User user) {

        UserDocument document = new UserDocument();
        document.setId(user.getId());
        document.setName(user.getName());
        document.setEmail(user.getEmail());
        document.setPassword(user.getPassword());

        ArrayList<String> boardsIds = new ArrayList<>();
        boardsIds.add(user.getBoards().get(0).getId());
        document.setBoardsIds(boardsIds);

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().USERS, document.getId(), document);
        setImage(document.getId(), SwingFXUtils.fromFXImage(user.getImage(), null));

        BoardRepository.getInstance().add(user.getBoards().get(0));
        for (TaskBase column : user.getBoards().get(0).getChildren()) {
            ColumnRepository.getInstance().add((Column) column);
            for (TaskBase task : column.getChildren()) {
                TaskRepository.getInstance().add((Task) task);
            }
        }
    }

    @Override
    public User get(String userId) {

        User user = null;
        UserDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().USERS, userId)).toObject(UserDocument.class);

        if (document != null) {

            user = new UserBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.email = document.getEmail();
                $.password = document.getPassword();
                $.image = getImage(document.getId());

                ArrayList<Board> boardsIds = new ArrayList<>();
                for (String boardId : document.getBoardsIds()) {
                    boardsIds.add(BoardRepository.getInstance().get(boardId));
                }
                $.boards = FXCollections.observableArrayList(boardsIds);

            }).build();

        }

        return user;
    }

    @Override
    public void delete(User user) {
    }

    public Contributor getContributor(String contributorId) {

        Contributor contributor = null;
        UserDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().USERS, contributorId)).toObject(UserDocument.class);

        if (document != null){
            contributor = new ContributorBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.email = document.getEmail();
                $.image = getImage(document.getId());
            }).build();
        }

        return contributor;
    }

    // add user's image to CloudStorage
    public void setImage(String userId, BufferedImage userBufferedImage) {
        StorageAPI.getInstance().uploadImage(userBufferedImage, StorageAPI.getInstance().USER_PHOTOS_FOLDER, userId);
    }

    // Get user's image by userId
    public Image getImage(String userId) {
        return StorageAPI.getInstance().selectImage(StorageAPI.getInstance().USER_PHOTOS_FOLDER, userId);
    }

    // Assign board to a specific contributor
    public void addBoardId(String userId, String boardId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().USERS, userId, "boardsIds", boardId);
    }

    // Deletes boardId from User's boardsIds Field
    public void deleteBoardId(String userId, String boardId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().USERS, userId, "boardsIds", boardId);
    }

    /**
     * Updates User's Name field in database.
     *
     * @param userId Id of user whom name will be changed.
     * @param name   Updated Name.
     */
    public void updateName(String userId, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().USERS, userId, "name", name);
    }

    /**
     * Updates User's email field in database and consequently it's Id too.
     *
     * @param user     Object of user whom email will be changed.
     * @param newEmail Updated email.
     */
    public void updateEmail(User user, String newEmail) {

        // Update all instances of user's id in all Database
        updateId(user, newEmail);

        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().USERS, user.getId());
        StorageAPI.getInstance().deleteImage(StorageAPI.getInstance().USER_PHOTOS_FOLDER, user.getId());

        UserDocument document = new UserDocument();
        document.setId(FirestoreAPI.getInstance().encrypt(newEmail));
        document.setName(user.getName());
        document.setEmail(newEmail);
        document.setPassword(user.getPassword());

        ArrayList<String> boardsIds = new ArrayList<>();
        user.getBoards().forEach(board -> boardsIds.add(board.getId()));
        document.setBoardsIds(boardsIds);

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().USERS, document.getId(), document);
        setImage(document.getId(), generateIdenticon(document.getId(),256));
    }

    private static void updateId(User user, String updatedEmail) {

        for (Board board : user.getBoards()) {

            // Update Contributors emails
            for (Contributor contributor : board.getContributors()) {
                if (contributor.getEmail().equals(user.getEmail())) {
                    BoardRepository.getInstance().deleteContributor(board.getId(), contributor.getEmail(), contributor.getRole());
                    BoardRepository.getInstance().addContributor(board.getId(), updatedEmail, contributor.getRole());
                }
            }

            for (TaskBase column : board.getChildren()) {

                // Update tasks' creatorId
                for (TaskBase task : column.getChildren()) {
                    if (((Task) task).getCreator().getEmail().equals(user.getEmail())) {
                        TaskRepository.getInstance().updateCreatorId(task.getId(),
                                FirestoreAPI.getInstance().encrypt(updatedEmail));
                    }

                    // Update tasks' assignees' emails
                    ArrayList<String> assigneesIds = new ArrayList<>();
                    ((Task) task).getAssignees().forEach(assignee -> {

                        if (assignee.getEmail().equals(user.getEmail())) {
                            assigneesIds.add(FirestoreAPI.getInstance().encrypt(updatedEmail));
                        } else {
                            assigneesIds.add(assignee.getId());
                        }

                        TaskRepository.getInstance().updateAssigneeIds(task.getId(), assigneesIds);
                    });

                }
            }
        }
    }

    /**
     * Updates User's password field in database.
     *
     * @param userId            Id of user whom password will be changed.
     * @param encryptedPassword Updated password after encryption.
     */
    public void updatePassword(String userId, String encryptedPassword) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().USERS, userId, "password", encryptedPassword);
    }

    /**
     * Save user's id and hashed password to .octillect file
     *
     * @param user signing user
     */
    public void rememberUser(User user) {

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
     * @return a user object filled from the database
     *         or null if .octillect file doesn't exist.
     */
    public User getRememberedUser() {

        if (!Main.octillectFile.exists()) {
            return null;
        }

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

        return UserRepository.getInstance().get(fileContent.get("id"));
    }


    /**
     * Generates an Identicon for any given User's ID,
     * where the ID must be hashed using any hashing algorithm.
     *
     * @param userId User's ID that will be converted into Identicon.
     * @param size   Width and Height of the Identicon.
     * @return Square BufferedImage of the Identicon.
     */
    public BufferedImage generateIdenticon(String userId, int size) {

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
