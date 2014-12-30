package graf.ethan.notezone;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ethan on 12/27/2014.
 * File Manager Class
 */
public class FileManager {
    private Context context;
    private SharedPreferences preferences;

    private File root, noteZone, myNotes, current;


    public FileManager(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;

        //Create root directories
        this.noteZone = context.getDir("NoteZone", Context.MODE_PRIVATE);
        this.myNotes = new File(noteZone, context.getResources().getString(R.string.my_notes));
        this.myNotes.mkdirs();

        //Set current directory
        this.current = this.myNotes;
    }

    public ArrayList<File> getFileChildren() {
        ArrayList<File> fileChildren = new ArrayList<>();
        File[] listFiles = current.listFiles();
        for(int i = 0; i < listFiles.length; i ++) {
            if(listFiles[i].isFile()) {
                fileChildren.add(listFiles[i]);
            }
        }
        return fileChildren;
    }

    public ArrayList<File> getFolderChildren() {
        ArrayList<File> folderChildren = new ArrayList<>();
        File[] listFiles = current.listFiles();
        for(int i = 0; i < listFiles.length; i ++) {
            if(listFiles[i].isDirectory()) {
                folderChildren.add(listFiles[i]);
            }
        }
        return folderChildren;
    }

    public File getCurrent() {
        return current;
    }

    public File getMyNotes() {
        return myNotes;
    }

    public File addFile(String name) {
        File newFile = new File(current, name);
        boolean exists = newFile.exists();
        int nameCount = 1;
        while(exists) {
            exists = !newFile.renameTo(new File(current, name + "(" + nameCount + ")"));
            nameCount += 1;
        }
        try {
            newFile.createNewFile();
        }
        catch(IOException e) {
            Log.i("FileManager", "FAILED TO CREATE FILE");
        }

        return newFile;
    }

    public File addFolder(String name) {
        File newFolder = new File(current, name);
        boolean exists = newFolder.exists();
        int nameCount = 1;
        while(exists) {
            exists = !newFolder.renameTo(new File(current, name + "(" + nameCount + ")"));
            nameCount += 1;
        }
        newFolder.mkdir();

        return newFolder;
    }

    public void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(int f = 0; f < files.length; f ++) {
                if(files[f].isDirectory()) {
                    deleteFolder(files[f]);
                } else {
                    files[f].delete();
                }
            }
        }
        folder.delete();
    }

    public void selectFolder(File selected) {
        current = selected;
    }

    public void selectParent() {
        current = current.getParentFile();
    }
}