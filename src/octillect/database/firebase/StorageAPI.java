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

    public static final String USER_PHOTOS_FOLDER       = "userPohtos/";
    public static final String IMAGE_ATTACHMENTS_FOLDER = "imageAttachments/";
    private static StorageClient storageClient          = StorageClient.getInstance();

    // Upload Image to Firebase CloudStorage
    public static void uploadImage(String filePath, String storageFolder, String imageStorageName) {
        InputStream imageFile = null;
        try {
            imageFile = new FileInputStream(filePath);
            String storagePath = storageFolder + imageStorageName + ".jpg";
            storageClient.bucket().create(storagePath, imageFile, Bucket.BlobWriteOption.userProject(Connection.PROJECT_ID));
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    // Search for image by name on CloudStorage and return it as an Image data type
    public static Image getImage(String folder, String imageStorageName) {
        byte[] imageByteArray = storageClient.bucket().get(folder + imageStorageName + ".jpg").getContent();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream);
    }

    // Delete image by name from CloudStorage
    public static void deleteImage(String folder, String imageStorageName) {
        storageClient.bucket().get(folder + imageStorageName + ".jpg").delete();
    }

    // Download image to the Local by image name
    public static void downloadImage(String folder, String imageStorageName, String downloadPath) {
        Image image = getImage(folder, imageStorageName);
        try {
            ImageIO.write((RenderedImage) image,"jpg", new File(downloadPath + "/" + imageStorageName + ".jpg"));
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
