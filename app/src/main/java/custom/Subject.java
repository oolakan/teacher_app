package custom;

/**
 * Created by HighStrit on 27/03/2017.
 */
public class Subject {
    private String name, stdClass;
    public Subject(){
        this.name = "";
        this.stdClass = "";

    }
    public Subject(String name, String stdClass){
        this.name = name;
        this.stdClass = stdClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStdClass() {
        return stdClass;
    }

    public void setStdClass(String stdClass) {
        this.stdClass = stdClass;
    }
}
