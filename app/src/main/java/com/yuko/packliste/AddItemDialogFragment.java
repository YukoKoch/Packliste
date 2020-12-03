package com.yuko.packliste;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private SimpleNameListItemAdapter peopleAdapter;
    private ArrayList<SimpleNameListItem> categoryList;
    private ArrayList<SimpleNameListItem> peopleList;
    private PackingItem itemInQuestion;
    private ListView categoryListView;
    private ListView peopleListView;

    public AddItemDialogFragment() {
        this.itemInQuestion = new PackingItem("");
    }

    public AddItemDialogFragment(PackingItem item) {
        this.itemInQuestion = item;
    }

    public interface OnItemAddedListener {
        void onItemAdded(String name, ArrayList<String> categories, ArrayList<String> people);
        ArrayList<SimpleNameListItem> getCategoryList();
        ArrayList<SimpleNameListItem> getPeopleList();
        void addCategory(String name);
        void addPerson(String name);
    }

    public interface OnItemEditedListener extends OnItemAddedListener {
        void onItemEdited(PackingItem oldItem, PackingItem newItem);
        ArrayList<SimpleNameListItem> getCategoryList(PackingItem item);
        ArrayList<SimpleNameListItem> getPeopleList(PackingItem item);
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog, null);

        categoryList = (itemInQuestion.getName().equals("")) ? addedListener.getCategoryList() : editedListener.getCategoryList(itemInQuestion);
        peopleList = (itemInQuestion.getName().equals("")) ? addedListener.getPeopleList() : editedListener.getPeopleList(itemInQuestion);

        categoryAdapter = new SimpleNameListItemAdapter(getContext(), (itemInQuestion.getName().equals("")) ? addedListener.getCategoryList() : editedListener.getCategoryList(itemInQuestion), this);
        peopleAdapter = new SimpleNameListItemAdapter(getContext(), (itemInQuestion.getName().equals("")) ? addedListener.getPeopleList() : editedListener.getPeopleList(itemInQuestion), this);

        categoryListView = (ListView) view.findViewById(R.id.dialogCategoryList);
        peopleListView = (ListView) view.findViewById(R.id.dialogPeopleList);
        categoryListView.setAdapter(categoryAdapter);
        peopleListView.setAdapter(peopleAdapter);
        updateCategoryListItems();
        updatePeopleListItems();

        TextView addCategoryButton = (TextView) view.findViewById(R.id.addCategoryButton);
        final View editText = inflater.inflate(R.layout.dialog_edit_text_field, null);
        EditText textView = (EditText) editText.findViewById(R.id.dialogEditText);
        textView.setText("");
        ViewGroup parent = (ViewGroup) addCategoryButton.getParent();
        addCategoryButton.setOnClickListener(v -> {
            parent.addView(editText, parent.indexOfChild(addCategoryButton));
            parent.removeView(addCategoryButton);
        });
        EditText dialogEditText = (EditText) editText.findViewById(R.id.dialogEditText);
        Button okButton = (Button) editText.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            String newCategoryName = dialogEditText.getText().toString();
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

        TextView addPersonButton = (TextView) view.findViewById(R.id.addPersonButton);
        final View editText1 = inflater.inflate(R.layout.dialog_edit_text_field, null);
        EditText textView1 = (EditText) editText1.findViewById(R.id.dialogEditText);
        textView1.setText("");
        ViewGroup parent1 = (ViewGroup) addPersonButton.getParent();
        addPersonButton.setOnClickListener(v -> {
            parent1.addView(editText1, parent1.indexOfChild(addPersonButton));
            parent1.removeView(addPersonButton);
        });
        EditText dialogEditText1 = (EditText) editText1.findViewById(R.id.dialogEditText);
        Button okButton1 = (Button) editText1.findViewById(R.id.okButton);
        okButton1.setOnClickListener(v -> {
            String newPersonName = dialogEditText1.getText().toString();
            if (!newPersonName.equals("")) {
                if (itemInQuestion.getName().equals("")) {
                    addedListener.addPerson(newPersonName);
                } else {
                    editedListener.addPerson(newPersonName);
                }
                peopleList.add(new SimpleNameListItem(newPersonName, false));
                updatePeopleListItems();
            }
            parent1.addView(addPersonButton, parent1.indexOfChild(editText1));
            parent1.removeView(editText1);
        });

        if (!itemInQuestion.getName().equals("")) {
            EditText editText2 = (EditText) view.findViewById(R.id.itemName);
            editText2.setText(itemInQuestion.getName());
        }

        builder.setView(view);

        builder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView name = (TextView) view.findViewById(R.id.itemName);
                        final String[] itemName = {name.getText().toString()};
                        if (itemName[0].equals("")) {
                            Toast.makeText(getContext(), "Name fehlt!", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<String> categories = new ArrayList<>();
                            ArrayList<String> people = new ArrayList<>();
                            for (int i = 0; i < categoryList.size(); i++) {
                                SimpleNameListItem categoryItem = categoryList.get(i);
                                if (categoryItem.isChecked()) {
                                    categories.add(categoryItem.getName());
                                }
                            }
                            for (int i = 0; i < peopleList.size(); i++) {
                                SimpleNameListItem peopleItem = peopleList.get(i);
                                if (peopleItem.isChecked()) {
                                    people.add(peopleItem.getName());
                                }
                            }
                            if (categories.size() == 0 && people.size() == 0) {
                                categories.add("Unsortiert");
                            }
                            if (!itemInQuestion.getName().equals("")) {
                                itemName[0] = itemName[0].split("\\(")[0].trim();
                            }
                            if (people.size() > 0) {
                                itemName[0] = itemName[0].concat(" (");
                                people.forEach(person -> {
                                    itemName[0] = itemName[0].concat(person);
                                    itemName[0] = itemName[0].concat(", ");
                                });
                                itemName[0] = itemName[0].substring(0, itemName[0].length() - 2);
                                itemName[0] = itemName[0].concat(")");
                            }
                            if (itemInQuestion.getName().equals("")) {
                                addedListener.onItemAdded(itemName[0], categories, people);
                            } else {
                                PackingItem newItem = new PackingItem(itemName[0]);
                                categories.forEach(s -> newItem.addCategory(s));
                                people.forEach(s -> newItem.addPerson(s));
                                editedListener.onItemEdited(itemInQuestion, newItem);
                            }
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

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

    private void updatePeopleListItems() {
        peopleAdapter.clear();
        peopleAdapter.addAll(peopleList);
        if(peopleAdapter.getCount() > 5){
            View item = peopleAdapter.getView(0, null, peopleListView);
            item.measure(0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
            peopleListView.setLayoutParams(params);
        }
    }

    @Override
    public void onItemChecked(SimpleNameListItem item) {
        categoryList.forEach(simpleNameListItem -> {
            if (simpleNameListItem.getName().equals(item.getName())) {
                simpleNameListItem.setChecked(item.isChecked());
            }
        });
        peopleList.forEach(simpleNameListItem -> {
            if (simpleNameListItem.getName().equals(item.getName())) {
                simpleNameListItem.setChecked(item.isChecked());
            }
        });
    }
}
