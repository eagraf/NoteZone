package graf.ethan.notezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 12/28/2014.
 * ListView Adapter for File objects
 */
public class AdapterFile extends BaseAdapter {
    private final Context context;
    private ArrayList<File> files;
    private FileManager fileManager;

    //Id map of children directories
    public HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public AdapterFile(Context context, ArrayList<File> files, FileManager fileManager) {
        this.context = context;
        this.files = files;
        this.fileManager = fileManager;

        //Initialize id map
        for(int i = 0; i < files.size(); i ++) {
            mIdMap.put(files.get(i).getName(), mIdMap.size());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fileView = inflater.inflate(R.layout.text_list_item, parent, false);

        //Subviews for list item
        TextView text = (TextView) fileView.findViewById(R.id.text1);

        //Set Text Views
        text.setText(getItem(position).getName());

        return fileView;
    }

    @Override
    public File getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position).getName();
        return mIdMap.get(item);
    }

    @Override
    public int getCount() {
        return files.size();
    }

    public void addFile(String name) {
        File newFile = fileManager.addFile(name);
        files.add(newFile);
        mIdMap.put(name, mIdMap.size());
    }
}
