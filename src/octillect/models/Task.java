package octillect.models;

import java.util.Date;

public class Task {

    private String name;
    private String description;
    private User [] assignee;
    private Date dueDate;
    private Task [] subTasks;
    private boolean isCompleted = false;

    public Task() {}

    public Task(String name, String description, User[] assignee, Date dueDate, Task[] subTasks, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.assignee = assignee;
        this.dueDate = dueDate;
        this.subTasks = subTasks;
        this.isCompleted = isCompleted;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User[] getAssignee() { return assignee; }
    public void setAssignee(User[] assignee) { this.assignee = assignee; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Task[] getSubTasks() { return subTasks; }
    public void setSubTasks(Task[] subTasks) { this.subTasks = subTasks; }

    public boolean isCompleted() { return isCompleted;  }
    public void setCompleted(boolean completed) { isCompleted = completed; }

 }
