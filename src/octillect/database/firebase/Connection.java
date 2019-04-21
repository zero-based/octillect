package octillect.database.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class Connection {

    public static final String PROJECT_ID           = "octillect-a01c0";
    public static final String DATABASE_URL         = "https://octillect-a01c0.firebaseio.com";
    public static final String SERVICE_ACCOUNT_KEY  = "src/octillect/database/firebase/firebase-key.json";
    public static final String STORAGE_BUCKET_NAME  = "octillect-a01c0.appspot.com";


    public static void initializeFirebase(){

        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream(SERVICE_ACCOUNT_KEY);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .setStorageBucket(STORAGE_BUCKET_NAME)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
