package patterns;

import java.util.ArrayList;

public class UpdatePatternLanguageContent {
    private String name;
    private ArrayList<Integer> ids;

    public String getName() { return name; }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setName(String name) { this.name = name; }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }
}
