package com.yuko.packliste;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class AddItemDialogFragment extends DialogFragment  implements SimpleNameListItemAdapter.OnDialogItemCheckedListener {
    private OnItemAddedListener addedListener;
    private OnItemEditedListener editedListener;
    private SimpleNameListItemAdapter categoryAdapter;
    private ArrayList<SimpleNameListItem> categoryList;
    private final PackingItem itemInQuestion;
    private ListView categoryListView;

    public AddItemDialogFragment() {
        this.itemInQuestion = new PackingItem("");
    }

    public AddItemDialogFragment(PackingItem item) {
        this.itemInQuestion = item;
    }

    public interface OnItemAddedListener {
        void onItemAdded(String name, ArrayList<String> categories);
        ArrayList<SimpleNameListItem> getCategoryList();
        void addCategory(String name);
    }

    public interface OnItemEditedListener extends OnItemAddedListener {
        void onItemEdited(PackingItem oldItem, PackingItem newItem);
        ArrayList<SimpleNameListItem> getCategoryList(PackingItem item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            addedListener = (OnItemAddedListener) getActivity();
         } catch (ClassCastException ignore) {}
        try {
            editedListener = (OnItemEditedListener) getActivity();
        } catch (ClassCastException ignore) {}

        if (addedListener == null && editedListener == null) {
            throw new ClassCastException("Must implement either OnItemAddedListener or OnItemEditedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog, null);

        categoryList = (itemInQuestion.getName().equals("")) ? addedListener.getCategoryList() : editedListener.getCategoryList(itemInQuestion);

        categoryAdapter = new SimpleNameListItemAdapter(getContext(), (itemInQuestion.getName().equals("")) ? addedListener.getCategoryList() : editedListener.getCategoryList(itemInQuestion), this);

        categoryListView = (ListView) view.findViewById(R.id.dialogCategoryList);
        categoryListView.setAdapter(categoryAdapter);
        updateCategoryListItems();

        TextView addCategoryButton = (TextView) view.findViewById(R.id.addCategoryButton);
        final View editText = inflater.inflate(R.layout.dialog_edit_text_field, null);
        EditText textView = (EditText) editText.findViewById(R.id.dialogEditText);
        textView.setText("");
        ViewGroup parent = (ViewGroup) addCategoryButton.getParent();
        addCategoryButton.setOnClickListener(v -> {
            parent.addView(editText, parent.indexOfChild(addCategoryButton));
            parent.removeView(addCategoryButton);
        });
        Button okButton = (Button) editText.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            String newCategoryName = textView.getText().toString();
            if (!newCategoryName.equals("")) {
                if (itemInQuestion.getName().equals("")) {
                    addedListener.addCategory(newCategoryName);
                } else {
                    editedListener.addCategory(newCategoryName);
                }
                categoryList.add(new SimpleNameListItem(newCategoryName, false));
                updateCategoryListItems();
            }
            parent.addView(addCategoryButton, parent.indexOfChild(editText));
            parent.removeView(editText);
        });

        if (!itemInQuestion.getName().equals("")) {
            EditText editText2 = (EditText) view.findViewById(R.id.itemName);
            editText2.setText(itemInQuestion.getName());
        }

        builder.setView(view);

        builder
                .setPositiveButton("OK", (dialog, which) -> {
                    TextView name = (TextView) view.findViewById(R.id.itemName);
                    final String[] itemName = {name.getText().toString()};
                    if (itemName[0].equals("")) {
                        Toast.makeText(getContext(), "Name fehlt!", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<String> categories = new ArrayList<>();
                        for (int i = 0; i < categoryList.size(); i++) {
                            SimpleNameListItem categoryItem = categoryList.get(i);
                            if (categoryItem.isChecked()) {
                                categories.add(categoryItem.getName());
                            }
                        }
                        if (categories.size() == 0) {
                            categories.add("Unsortiert");
                        }
                        if (!itemInQuestion.getName().equals("")) {
                            itemName[0] = itemName[0].split("\\(")[0].trim();
                        }
                        if (itemInQuestion.getName().equals("")) {
                            addedListener.onItemAdded(itemName[0], categories);
                        } else {
                            PackingItem newItem = new PackingItem(itemName[0]);
                            categories.forEach(newItem::addCategory);
                            editedListener.onItemEdited(itemInQuestion, newItem);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());

        return builder.create();
    }

    private void updateCategoryListItems() {
        categoryAdapter.clear();
        categoryAdapter.addAll(categoryList);
        if(categoryAdapter.getCount() > 5){
            View item = categoryAdapter.getView(0, null, categoryListView);
            item.measure(0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
            categoryListView.setLayoutParams(params);
        }
    }

    @Override
    public void onItemChecked(SimpleNameListItem item) {
        categoryList.forEach(simpleNameListItem -> {
            if (simpleNameListItem.getName().equals(item.getName())) {
                simpleNameListItem.setChecked(item.isChecked());
            }
        });
    }
}
