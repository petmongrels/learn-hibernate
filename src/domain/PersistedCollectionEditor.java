package domain;

import java.util.ArrayList;
import java.util.List;

public class PersistedCollectionEditor<T extends Entity> {
    private List<T> collection;

    public PersistedCollectionEditor(List<T> collection) {
        this.collection = collection;
    }

    public void edit(ArrayList<T> edited) {
        collection.retainAll(edited);
        addAndModify(edited);
    }

    private void addAndModify(ArrayList<T> edited) {
        for(T t : edited) {
            int index = collection.indexOf(t);

            if (index == -1) collection.add(t);
            else collection.get(index).copyFrom(t);
        }
    }
}
