package graf.ethan.notezone;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Ethan on 12/22/14.
 * Placeholder directory class
 */
public class Directory {
    public String name;
    public Directory parent;
    public ArrayList<Directory> children = new ArrayList<> ();
    public DirectoryAdapter adapter;
    public Context context;

    //For root directories
    public Directory(String name, Context context) {
        this.name = name;
        this.context = context;
        this.adapter = new DirectoryAdapter (this.context, children, this);
    }

    public Directory(String name, Directory parent, Context context) {
        this.name = name;
        this.parent = parent;
        this.context = context;
        this.adapter = new DirectoryAdapter (this.context, children, this);
        addChild(parent);
    }

    public void addChild(String name) {
        children.add(new Directory(name, this, context));
        adapter.mIdMap.put(name, adapter.mIdMap.size());
    }

    public void addChild(Directory child) {
        children.add(child);
        adapter.mIdMap.put(child.name, adapter.mIdMap.size());
    }

    @Override
    public String toString() {
        return name;
    }
}
