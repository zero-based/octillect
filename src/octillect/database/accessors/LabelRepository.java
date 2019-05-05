package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import javafx.scene.paint.Color;

import octillect.database.documents.LabelDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Label;
import octillect.models.builders.LabelBuilder;

public class LabelRepository {

    // add new label data to database.
    public static void add(Label label) {
        LabelDocument document = new LabelDocument();
        document.setId(label.getId());
        document.setName(label.getName());
        document.setColor(label.getColorHex());
        FirestoreAPI.insertDocument(FirestoreAPI.LABELS, document.getId(), document);
    }

    public static Label get(String labelId) {
        Label label = null;
        LabelDocument document = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.LABELS, labelId)).toObject(LabelDocument.class);

        if (document != null) {

            label = new LabelBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.color = Color.valueOf(document.getColor());
            }).build();
        }
        return label;
    }

    public static void delete(String id) {
        FirestoreAPI.deleteDocument(FirestoreAPI.LABELS, id);
    }

}
