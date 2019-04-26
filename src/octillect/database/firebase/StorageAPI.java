package octillect.database.firebase;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;

public class StorageAPI {

    public static final String USER_PHOTOS_FOLDER       = "userPhotos/";
    public static final String IMAGE_ATTACHMENTS_FOLDER = "imageAttachments/";
    private static StorageClient storageClient          = StorageClient.getInstance();

    /**
     * Uploads given Image to Firebase CloudStorage.
     *
     * @param bufferedImage    Image to Upload.
     * @param storageFolder    Firebase Storage Folder name.
     * @param imageStorageName The Name which the image will be uploaded by in the database.
     */
    public static void uploadImage(BufferedImage bufferedImage, String storageFolder, String imageStorageName) {
        String storagePath = storageFolder + imageStorageName + ".jpg";
        try {
            // Convert Image to InputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            byte[] imageByteArray = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(imageByteArray);

            // Upload Image inputStream to Storage
            storageClient.bucket().create(storagePath, inputStream, Bucket.BlobWriteOption.userProject(Connection.PROJECT_ID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Search for image by name on CloudStorage and return it as an Image data type
    public static Image selectImage(String folder, String imageStorageName) {
        try {
            byte[] imageByteArray = storageClient.bucket().get(folder + imageStorageName + ".jpg").getContent();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
            return new Image(byteArrayInputStream);
        } catch (Exception e) {
            return null;
        }
    }

    // Delete image by name from CloudStorage
    public static void deleteImage(String folder, String imageStorageName) {
        storageClient.bucket().get(folder + imageStorageName + ".jpg").delete();
    }

    // Download image to the Local by image name
    public static void downloadImage(String folder, String imageStorageName, String downloadPath) {
        storageClient.bucket().get(folder + imageStorageName + ".jpg").downloadTo(Paths.get(downloadPath));
    }
}
