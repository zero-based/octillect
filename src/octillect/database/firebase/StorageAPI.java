package octillect.database.firebase;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class StorageAPI {

    public static final String USER_PHOTOS_FOLDER = "userPohtos/";
    public static final String IMAGE_ATTACHMENTS_FOLDER = "imageAttachments/";
    private static StorageClient storageClient = StorageClient.getInstance();

    // Upload Image to Firebase CloudStorage
    public static void uploadImage(String filePath, String storageFolder, String imageName) {
        InputStream imageFile = null;
        try {
            imageFile = new FileInputStream(filePath);
            String storagePath = storageFolder + imageName;
            storageClient.bucket().create(storagePath, imageFile, Bucket.BlobWriteOption.userProject(Connection.PROJECT_ID));
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    // Search for image by name on CloudStorage and return it as an Image data type
    public static Image getImage(String folder, String imageName) {
        byte[] imageByteArray = storageClient.bucket().get(folder + imageName).getContent();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream);
    }

    // Delete image by name from CloudStorage
    public static void deleteImage(String folder, String imageName) {
        storageClient.bucket().get(folder + imageName).delete();
    }

    // Download image to the Local by image name
    public static void downloadImage(String folder, String imageName, String downloadPath) {
        Image image = getImage(folder, imageName);
        try {
            ImageIO.write((RenderedImage) image, "jpg", new File(downloadPath + imageName + ".jpg"));
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
