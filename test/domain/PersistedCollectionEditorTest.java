package domain;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class PersistedCollectionEditorTest {
    private ArrayList<EntityForPersistedCollectionEditorTest> modified;
    private ArrayList<EntityForPersistedCollectionEditorTest> existing;
    private PersistedCollectionEditor<EntityForPersistedCollectionEditorTest> collection;

    @BeforeMethod
    public void setUp(){
        modified = new ArrayList<EntityForPersistedCollectionEditorTest>();
        existing = new ArrayList<EntityForPersistedCollectionEditorTest>();
        collection = new PersistedCollectionEditor<EntityForPersistedCollectionEditorTest>(existing);
    }

    @Test
    public void editEmpty(){
        collection.edit(modified);
        assert modified.size() == 0;
    }

    @Test
    public void edit() {
        populate(existing, 1, 2, 3);
        populate(modified, 1, 3, 4);
        collection.edit(modified);

        assert modified.size() == 3;
        assert modified.contains(entity(1));
        assert modified.contains(entity(3));
        assert modified.contains(entity(4));
    }

    private void populate(ArrayList<EntityForPersistedCollectionEditorTest> list, int ... ids) {
        for (int id : ids) {
            list.add(entity(id));
        }
    }

    private EntityForPersistedCollectionEditorTest entity(int id) {
        return new EntityForPersistedCollectionEditorTest(id);
    }
}
