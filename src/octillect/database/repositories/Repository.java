package octillect.database.repositories;

public interface Repository<Model> {
    void add(Model model);
    Model get(String modelId);
    void delete(Model model);
}
