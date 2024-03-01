package com.yuko.packliste;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RemoveOrEditItemDialogFragment extends DialogFragment {
    private OnItemRemovedListener listener;
    private final PackingItem item;

    public interface OnItemRemovedListener {
        void onItemRemovedOK(PackingItem item);
        void onEditItem(PackingItem item);
    }

    public RemoveOrEditItemDialogFragment(PackingItem item) {
        this.item = item;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (OnItemRemovedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity()
                    + "must implement RemoveOrEditItemDialogFragment");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(item.getName() + " löschen oder bearbeiten?")
                .setPositiveButton("Löschen", (dialog, which) -> listener.onItemRemovedOK(item))
                .setNeutralButton("Bearbeiten", (dialog, which) -> listener.onEditItem(item))
                .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss());
        return builder.create();
    }
}
