package com.yuko.packliste;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ImportDialogFragment extends DialogFragment {
    private OnImportListener listener;

    public interface OnImportListener {
        void onImport(String string);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ImportDialogFragment.OnImportListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnImportListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.import_edit_text, null);

        builder.setTitle("Import")
                .setView(view)
                .setPositiveButton("Import", (dialog, which) -> {
                    EditText editText = (EditText) view.findViewById(R.id.importEditText);
                    listener.onImport(editText.getText().toString());
                })
                .setNegativeButton("Abbrechen", (dialog, which) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }
}
