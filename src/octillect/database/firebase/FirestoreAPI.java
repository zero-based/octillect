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
    public static final String USERS     = "users";
    public static final String PROEJECTS = "projects";
    public static final String TASKS     = "tasks";
    public static final String LABELS    = "labels";
    public static final String COLUMNS   = "columns";

    // Initialize our Firestore instance
    static Firestore database = FirestoreClient.getFirestore();

    // Get the document attributes
    public static DocumentSnapshot getDocument(String collection, String document) {

        DocumentReference docRef = database.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Checking to make sure that the document is unique
    public static boolean isDocumentExist(String collection, String document) {

        return getDocument(collection, document).exists();
    }

    // Assigning the attributes of the document
    public static void AddDocument(String collection, String document, Object object) {
        database.collection(collection).document(document).set(object);
    }

    public static void AddDocument(String collection, String document, HashMap<String, Object> fields) {

        Map<String, Object> updates = fields;
        database.collection(collection).document(document).set(fields);
    }

    // Updating the attributes of the document
    public static void updateDocumentAttribute(String collection, String document, String key, String value) {

        Map<String, Object> updates = new HashMap<>();
        updates.put(key, value);

        DocumentReference docRef = database.collection(collection).document(document);
        docRef.update(updates);
    }

    // Delete the document
    public static void deleteDocument(String collection, String document) {
        database.collection(collection).document(document).delete();
    }
}
