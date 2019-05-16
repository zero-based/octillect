package octillect.models;

import javafx.collections.ObservableList;

public class Column extends TaskBase {

    public Column(String id, String name, ObservableList<Task> tasks) {
        super(id, name, null, tasks);
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Columns have no Description");
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Columns have no Description");
    }

}
