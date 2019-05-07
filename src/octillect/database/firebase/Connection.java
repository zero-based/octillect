package octillect.database.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class Connection {

    Firestore firestore;
    final String BOARD_ID                    = "octillect-a01c0";
    private final String DATABASE_URL        = "https://octillect-a01c0.firebaseio.com";
    private final String SERVICE_ACCOUNT_KEY = "src/octillect/database/firebase/firebase-key.json";
    private final String STORAGE_BUCKET_NAME = "octillect-a01c0.appspot.com";

    private static Connection ourInstance = new Connection();

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream(SERVICE_ACCOUNT_KEY);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setFirestoreOptions(FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true).build())
                    .setDatabaseUrl(DATABASE_URL)
                    .setStorageBucket(STORAGE_BUCKET_NAME)
                    .build();
            FirebaseApp.initializeApp(options);
            firestore = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
