package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import javafx.scene.paint.Color;

import octillect.database.documents.LabelDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Label;
import octillect.models.builders.LabelBuilder;

public class LabelRepository {

    private static LabelRepository ourInstance = new LabelRepository();

    public static LabelRepository getInstance() {
        return ourInstance;
    }

    private LabelRepository() {}

    // add new label data to database.
    public void add(Label label) {
        LabelDocument document = new LabelDocument();
        document.setId(label.getId());
        document.setName(label.getName());
        document.setColor(label.getColorHex());
        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().LABELS, document.getId(), document);
    }

    public Label get(String labelId) {
        Label label = null;
        LabelDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().LABELS, labelId)).toObject(LabelDocument.class);

        if (document != null) {

            label = new LabelBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.color = Color.valueOf(document.getColor());
            }).build();
        }
        return label;
    }

    public void delete(String id) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().LABELS, id);
    }

}
