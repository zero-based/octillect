package octillect.database.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreAPI {

    // Collections' names constants
    public static final String USERS    = "users";
    public static final String PROJECTS = "projects";
    public static final String TASKS    = "tasks";
    public static final String LABELS   = "labels";
    public static final String COLUMNS  = "columns";

    // Select a whole document
    public static Object selectDocument(String collection, String document) {
        DocumentReference docRef = Connection.firestore.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
        try {
            return documentSnapshot.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    // Select a specific attribute from a document
    public static Object selectAttribute(String collection, String document, String key) {
        DocumentReference docRef = Connection.firestore.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
        try {
            return documentSnapshot.get().getData().get(key);
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    // Add new document
    public static void insertDocument(String collection, String document, Object object) {
        Connection.firestore.collection(collection).document(document).set(object);
    }

    // Add new attribute to a document
    public static void insertAttribute(String collection, String document, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        Connection.firestore.collection(collection).document(document).set(map);
    }

    // Update a document.
    public static void updateDocument(String collection, String document, Object object) {
        Connection.firestore.collection(collection).document(document).set(object);
    }

    // Update a specific attribute.
    public static void updateAttribute(String collection, String document, String key, String value) {
        Connection.firestore.collection(collection).document(document).update(key, value);
    }

    // Append a specific attribute
    public static void appendAttribute(String collection, String document, String key, Object value) {
        DocumentReference documentReference = Connection.firestore.collection(collection).document(document);
        documentReference.update(key, FieldValue.arrayUnion(value));
    }

    // Delete the whole document
    public static void deleteDocument(String collection, String document) {
        Connection.firestore.collection(collection).document(document).delete();
    }

    // Delete a specific attribute in a document
    public static void deleteAttribute(String collection, String document, String key) {
        Connection.firestore.collection(collection).document(document).update(key, null);
    }

    /**
     * Encrypts a given string using SHA-1 Algorithm.
     *
     * @param textToEncrypt text to be encrypted
     * @return encrypted String
     */
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

    /**
     * Encrypts a given string using DateTime Algorithm.
     *
     * @param textToEncrypt text to be encrypted
     * @return encrypted String
     */
    public static String encryptWithDateTime(String textToEncrypt) {
        return encrypt(textToEncrypt + java.time.LocalDateTime.now().toString());
    }

}
