package octillect.database.repositories;

import com.google.cloud.firestore.DocumentSnapshot;

import javafx.scene.paint.Color;

import octillect.database.documents.TagDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Tag;
import octillect.models.builders.TagBuilder;

public class TagRepository {

    private static TagRepository ourInstance = new TagRepository();

    public static TagRepository getInstance() {
        return ourInstance;
    }

    private TagRepository() {
    }

    // add new tag data to database.
    public void add(Tag tag) {
        TagDocument document = new TagDocument();
        document.setId(tag.getId());
        document.setName(tag.getName());
        document.setColor(tag.getColorHex());
        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().TAGS, document.getId(), document);
    }

    public Tag get(String tagId) {

        TagDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().TAGS, tagId)).toObject(TagDocument.class);

        Tag tag = new TagBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();
            $.color = Color.valueOf(document.getColor());
        }).build();

        return tag;
    }

    public void delete(String tagId) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().TAGS, tagId);
    }

}
