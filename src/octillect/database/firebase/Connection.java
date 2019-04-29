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

    public  static final String PROJECT_ID           = "octillect-a01c0";
    private static final String DATABASE_URL         = "https://octillect-a01c0.firebaseio.com";
    private static final String SERVICE_ACCOUNT_KEY  = "src/octillect/database/firebase/firebase-key.json";
    private static final String STORAGE_BUCKET_NAME  = "octillect-a01c0.appspot.com";
    static Firestore firestore;

    public static void initializeFirebase(){

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
