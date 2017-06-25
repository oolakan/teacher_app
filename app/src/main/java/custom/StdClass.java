package custom;

/**
 * Created by HighStrit on 27/03/2017.
 */

public class StdClass {

    private String name, category;

    public StdClass(){
        this.name = "";
        this.category = "";
    }

    public StdClass(String name, String category){
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}