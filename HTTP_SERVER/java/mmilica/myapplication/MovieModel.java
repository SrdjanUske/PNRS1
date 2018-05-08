package mmilica.myapplication;

public class MovieModel {

    private String id;
    private String name;

    public MovieModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
