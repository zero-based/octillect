package octillect.models;

public class Column {

    private String name;
    private Task[] card;

    public Column() {}

    public Column(String name, Task[] card) {
        this.name = name;
        this.card = card;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Task[] getCard() { return card; }
    public void setCard(Task[] card) { this.card = card; }

}
