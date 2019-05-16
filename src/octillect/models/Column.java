package octillect.models;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Column extends TaskBase {

    private FilteredList<Task> filteredTasks;


    public Column(String id, String name, ObservableList<Task> tasks) {
        super(id, name, null, tasks);
        filteredTasks = new FilteredList<>(tasks);
    }


    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Columns have no Description");
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Columns have no Description");
    }


    public FilteredList<Task> getFilteredTasks() {
        return filteredTasks;
    }

    public void setFilteredTasks(FilteredList<Task> filteredTasks) {
        this.filteredTasks = filteredTasks;
    }

}
