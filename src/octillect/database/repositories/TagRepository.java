package octillect.database.repositories;

import javafx.scene.paint.Color;

import octillect.database.documents.TagDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Tag;
import octillect.models.builders.TagBuilder;

public class TagRepository implements Repository<Tag> {

    private static TagRepository ourInstance = new TagRepository();

    public static TagRepository getInstance() {
        return ourInstance;
    }

    private TagRepository() {
    }


    @Override
    public void add(Tag tag) {
        TagDocument document = new TagDocument();
        document.setId(tag.getId());
        document.setName(tag.getName());
        document.setColor(tag.getColorHex());
        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.TAGS, document.getId(), document);
    }

    @Override
    public Tag get(String tagId) {

        TagDocument document = FirestoreAPI.getInstance().selectDocument(FirestoreAPI.TAGS, tagId).toObject(TagDocument.class);

        Tag tag = new TagBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();
            $.color = Color.valueOf(document.getColor());
        }).build();

        return tag;
    }

    @Override
    public void delete(Tag tag) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.TAGS, tag.getId());
    }

}
