package graf.ethan.notezone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity implements NameDialogFragment.NameDialogListener {
    //Drawer Variables
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerTable;

    //File List Variables
    private ListView mFileList;

    //File Variables
    private FileManager fileManager;
    private AdapterFile fileAdapter;
    private AdapterFolder folderAdapter;

    //Other
    private MenuInflater menuInflater = getMenuInflater();
    private View rowBack, rowAddFolder, rowList;

    //Shared Preferences
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Shared Preferences
        preferences = getPreferences(MODE_PRIVATE);

        //Creates root and current directories
        //directoryRoot = new Directory(getString(R.string.my_notes), this);
        //directoryCurrent = directoryRoot;

        //FileManager stuff
        fileManager = new FileManager(this, preferences);
        fileAdapter = new AdapterFile(this, fileManager.getFileChildren(), fileManager);
        folderAdapter = new AdapterFolder(this, fileManager.getFolderChildren(), fileManager);

        //Initializing File List
        //testArray = getResources().getStringArray(R.array.array_test);
        mFileList = (ListView) findViewById(R.id.file_list);
        mFileList.setAdapter(fileAdapter);
        mFileList.setOnItemClickListener(new FileListItemClickListener());

        //Initializing Nav Drawer stuff
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerTable = (RelativeLayout) findViewById(R.id.drawer_main);
        mDrawerList.setAdapter(folderAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Table stuff
        rowBack = findViewById(R.id.nav_back);
        rowAddFolder = findViewById(R.id.nav_add_folder);
        rowList = findViewById(R.id.nav_list);

        rowBack.setVisibility(View.GONE);
        rowList.setVisibility(View.GONE);

        //Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(fileManager.getCurrent().getName());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        directoryUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (preferences.getBoolean("initialize", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            getSupportActionBar().setSubtitle("FUCK");
            preferences.edit().putBoolean("initialize", false).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        menuInflater.inflate(R.menu.menu_main, menu);

        //Hide Drawer Icons
        menu.setGroupVisible(R.id.group_drawer, false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerTable);
        menu.setGroupVisible(R.id.group_main, !drawerOpen);
        menu.setGroupVisible(R.id.group_drawer, drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
            case R.id.action_share:
                return true;
            case R.id.action_upload:
                return true;
            case R.id.action_add_folder:
                showNewFolderDialog();
                directoryUpdate();
                return true;
            case R.id.action_edit:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showNewFolderDialog() {
        // Create an instance of the dialog fragment and show it
        NameDialogFragment dialog = new NameDialogFragment();
        dialog.setTitle(getResources().getString(R.string.dialog_new_folder));
        dialog.setType("new_folder");
        dialog.show(getSupportFragmentManager(), "NameDialogFragment");

    }

    public void showNewFileDialog() {
        //Create a dialog fragment for file names
        NameDialogFragment dialog = new NameDialogFragment();
        dialog.setTitle(getResources().getString(R.string.dialog_new_note));
        dialog.setType("new_file");
        dialog.show(getSupportFragmentManager(), "NameDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(NameDialogFragment dialog, String name) {
        // User touched the dialog's positive button
        if(dialog.type.equals("new_folder")) {
            folderAdapter.addFolder(name);
            mDrawerList.setAdapter(folderAdapter);
            directoryUpdate();
        }
        else if(dialog.type.equals("new_file")) {
            fileAdapter.addFile(name);
            mFileList.setAdapter(fileAdapter);
        }
    }

    @Override
    public void onDialogNegativeClick(NameDialogFragment dialog) {
        // User touched the dialog's negative button
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class FileListItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }

    /*Click listener for popup menus */
    private class PopupItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;
        private PopupItemClickListener(int position) {
            this.position = position;
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_directory_edit:
                    return true;
                case R.id.action_directory_copy:
                    return true;
                case R.id.action_directory_delete:
                    fileManager.deleteFolder(folderAdapter.getItem(position));
                    directoryUpdate();
                    return true;
                default:
                    return false;
            }
        }
    }

    /*Opens a new directory every time a list item is tapped*/
    private void selectItem(int position) {
        // update selected item and title, then close the drawers
        mDrawerList.setItemChecked(position, true);
        fileManager.selectFolder(folderAdapter.getItem(position));
        getSupportActionBar().setTitle(fileManager.getCurrent().getName());
        directoryUpdate();
    }


    /*Updates views in the nav drawer every time it is called*/
    private void directoryUpdate() {
        folderAdapter = new AdapterFolder(this, fileManager.getFolderChildren(), fileManager);
        mDrawerList.setAdapter(folderAdapter);
        fileAdapter = new AdapterFile(this, fileManager.getFileChildren(), fileManager);
        mFileList.setAdapter(fileAdapter);

        if(fileManager.getCurrent().listFiles().length == 0) {
            rowAddFolder.setVisibility(View.VISIBLE);
            rowList.setVisibility(View.GONE);
        }
        else {
            rowAddFolder.setVisibility(View.GONE);
            rowList.setVisibility(View.VISIBLE);
        }
        if(fileManager.getCurrent().equals(fileManager.getMyNotes())) {
            rowBack.setVisibility(View.GONE);
        }
        else {
            rowBack.setVisibility(View.VISIBLE);
        }
    }

    /*Displays popup for overflow icon in list view items*/
    public void onClickShowPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        int position = mDrawerList.getPositionForView(v);
        popup.setOnMenuItemClickListener(new PopupItemClickListener(position));
        menuInflater.inflate(R.menu.menu_directory, popup.getMenu());
        popup.show();
    }

    /*Creates intent for showing the settings activity*/
    public void onClickShowSettings(View v) {

    }

    /*Adds a file*/
    public void onClickAddFile(View v) {
        showNewFileDialog();
    }

    /*Moves the directory back one level*/
    public void onClickDirectoryBack(View v) {
        fileManager.selectParent();
        getSupportActionBar().setTitle(fileManager.getCurrent().getName());
        directoryUpdate();
    }
}
