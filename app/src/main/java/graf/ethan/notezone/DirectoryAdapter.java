package graf.ethan.notezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 12/22/14.
 */
public class DirectoryAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<Directory> directoryChildren;
    private Directory directoryParent;

    //Id map of children directories
    public HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public DirectoryAdapter(Context context, ArrayList<Directory> directoryChildren, Directory directoryParent) {
        this.context = context;
        this.directoryChildren = directoryChildren;
        this.directoryParent = directoryParent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View directoryView = inflater.inflate(R.layout.folder_list_item, parent, false);

        //Subviews for list item
        TextView firstLine = (TextView) directoryView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) directoryView.findViewById(R.id.secondLine);
        ImageView icon = (ImageView) directoryView.findViewById(R.id.icon);

        //Set Text Views
        firstLine.setText(getItem(position).name);
        secondLine.setText("Ethan is pretty snazzy");

        if(directoryParent.parent != null && position == 0) {
            icon.setImageResource(R.drawable.ic_action_back);
            directoryView.setBackgroundColor(directoryView.getResources().getColor(R.color.Orange900));
        }
        else {
            icon.setImageResource(R.drawable.ic_action_collection);
        }

        return directoryView;
    }

    @Override
    public Directory getItem(int position) {
        return directoryChildren.get(position);
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position).name;
        return mIdMap.get(item);
    }

    @Override
    public int getCount() {
        return directoryChildren.size();
    }
}
