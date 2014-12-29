package graf.ethan.notezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 12/28/2014.
 * Folder Adapter class for ListView
 */
public class AdapterFolder extends BaseAdapter {
    private final Context context;
    private ArrayList<File> folders;
    private ArrayList<File> files;
    private FileManager fileManager;

    //Id map of children directories
    public HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public AdapterFolder(Context context, ArrayList<File> folders, FileManager fileManager) {
        this.context = context;
        this.folders = folders;
        this.fileManager = fileManager;

        //Get files
        this.files = fileManager.getFileChildren();

        //Initialize id map
        for(int i = 0; i < folders.size(); i ++) {
            mIdMap.put(folders.get(i).getName(), mIdMap.size());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View folderView = inflater.inflate(R.layout.folder_list_item, parent, false);

        //Subviews for list item
        TextView firstLine = (TextView) folderView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) folderView.findViewById(R.id.secondLine);
        ImageView icon = (ImageView) folderView.findViewById(R.id.icon);

        //Set Text Views
        firstLine.setText(getItem(position).getName());
        secondLine.setText("Ethan is pretty snazzy");

        icon.setImageResource(R.drawable.ic_action_collection);

        return folderView;
    }

    @Override
    public File getItem(int position) {
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position).getName();
        return mIdMap.get(item);
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    public void addFolder(String name) {
        File newFolder = fileManager.addFolder(name);
        String editedName = newFolder.getName();
        folders.add(newFolder);
        mIdMap.put(editedName, mIdMap.size());
    }
}