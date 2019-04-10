package octillect.database.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

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

    // Initialize our Firestore instance
    static Firestore database = FirestoreClient.getFirestore();

    // Select a whole document
    public static Object selectDocument(String collection, String document) {
        DocumentReference docRef = database.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
        try {
            return documentSnapshot.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    // Select a specific attribute from a document
    public static Object selectAttribute(String collection, String document, String key) {
        DocumentReference docRef = database.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> documentSnapshot = docRef.get();
        try {
            return documentSnapshot.get().getData().get(key);
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    // Add new document
    public static void insertDocument(String collection, String document, Object object) {
        database.collection(collection).document(document).set(object);
    }

    // Add new attribute to a document
    public static void insertAttribute(String collection, String document, String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        database.collection(collection).document(document).set(map);
    }

    // Update a document.
    public static void updateDocument(String collection, String document, Object object) {
        database.collection(collection).document(document).set(object);
    }

    // Update a specific attribute.
    public static void updateAttribute(String collection, String document, String key, String value) {
        database.collection(collection).document(document).update(key, value);
    }

    // Delete the whole document
    public static void deleteDocument(String collection, String document) {
        database.collection(collection).document(document).delete();
    }

    // Delete a specific attribute in a document
    public static void deleteAttribute(String collection, String document, String key) {
        database.collection(collection).document(document).update(key, null);
    }
}
