package com.yuko.packliste;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RemoveItemDialogFragment extends DialogFragment {
    private OnItemRemovedListener listener;
    private PackingItem item;

    public interface OnItemRemovedListener {
        void onItemRemovedOK(PackingItem item);
    }

    public RemoveItemDialogFragment(PackingItem item) {
        this.item = item;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (OnItemRemovedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + "must implement OnItemRemovedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Wirklich " + item.getName() + " entfernen?")
                .setPositiveButton("OK", (dialog, which) -> {
                    listener.onItemRemovedOK(item);
                })
                .setNegativeButton("Abbrechen", (dialog, which) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }
}
