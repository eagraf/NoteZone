package graf.ethan.notezone;

import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends ActionBarActivity implements NewFolderDialogFragment.NewFolderDialogListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private TableLayout mDrawerTable;
    private Directory directoryRoot;
    private Directory directoryCurrent;
    private MenuInflater menuInflater = getMenuInflater();
    private View rowBack, rowAddFolder, rowList, rowSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates root and current directories
        directoryRoot = new Directory(getString(R.string.my_notes), this);
        directoryCurrent = directoryRoot;

        //Initializing Nav Drawer stuff
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mDrawerList = (ListView) findViewById(R.id.folder_list);
        mDrawerTable = (TableLayout) findViewById(R.id.drawer_main);
        mDrawerList.setAdapter(directoryRoot.adapter);

        //Table stuff
        rowBack = findViewById(R.id.row_back);
        rowAddFolder = findViewById(R.id.row_add_folder);
        rowList = findViewById(R.id.row_list);
        rowSettings = findViewById(R.id.row_settings);

        rowBack.setVisibility(View.GONE);
        rowList.setVisibility(View.GONE);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
                getSupportActionBar().setTitle(getString(R.string.my_notes));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
                return true;
            case R.id.action_edit:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showNewFolderDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NewFolderDialogFragment();
        dialog.show(getSupportFragmentManager(), "NewFolderDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String folderName) {
        // User touched the dialog's positive button
        directoryCurrent.addChild(folderName);
        directoryUpdate();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /*Click listener for popup menus */
    private class PopupItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_directory_edit:
                    return true;
                case R.id.action_directory_copy:
                    return true;
                case R.id.action_directory_delete:
                    return true;
                default:
                    return false;
            }
        }
    }

    private void selectItem(int position) {
        // update selected item and title, then close the drawers
        mDrawerList.setItemChecked(position, true);
        directoryCurrent = directoryCurrent.children.get(position);
        getSupportActionBar().setTitle(directoryCurrent.name);
        mDrawerList.setAdapter(directoryCurrent.adapter);
        directoryUpdate();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupItemClickListener());
        menuInflater.inflate(R.menu.menu_directory, popup.getMenu());
        popup.show();
    }

    public void showSettings(View v) {

    }

    public void directoryBack(View v) {
        directoryCurrent = directoryCurrent.parent;
        mDrawerList.setAdapter(directoryCurrent.adapter);
        directoryUpdate();
    }

    private void directoryUpdate() {
        if(directoryCurrent.children.isEmpty()) {
            rowAddFolder.setVisibility(View.VISIBLE);
            rowList.setVisibility(View.GONE);
        }
        else {
            rowAddFolder.setVisibility(View.GONE);
            rowList.setVisibility(View.VISIBLE);
        }
        if(directoryCurrent.parent == null) {
            rowBack.setVisibility(View.GONE);
        }
        else {
            rowBack.setVisibility(View.VISIBLE);
        }
    }
}
