package graf.ethan.notezone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Ethan on 12/19/14.
 * Fragment for dialog asking for folder name
 */
public class NameDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NameDialogListener {
        public void onDialogPositiveClick(NameDialogFragment dialog, String folderName);
        public void onDialogNegativeClick(NameDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NameDialogListener mListener;

    public String title = "default_title";
    public String name = "default_name";
    public String type;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NameDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NewFolderDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_folder, null);

        //Get the EditText element from the dialog box
        final EditText editText = (EditText) view.findViewById(R.id.folder_name);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        builder.setTitle(title).setPositiveButton(R.string.dialog_done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String folderName = editText.getText().toString();
                if (folderName.equals("")) {
                    folderName = getResources().getString(R.string.create_new_folder);
                }
                mListener.onDialogPositiveClick(NameDialogFragment.this, folderName);
            }
        })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(NameDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
