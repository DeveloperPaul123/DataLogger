package com.devpaul.datalogger.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devpaul.datalogger.R;
import com.devpaul.datalogger.data.Subject;
import com.devpaul.datalogger.utils.EditTextChecker;
import com.github.mrengineer13.snackbar.SnackBar;

/**
 * Created by Pauly D on 3/16/2015.
 */
public class NewSubjectDialog extends DialogFragment {

    public static interface Callback {
        public void onSubjectCreated(Subject subject);
    }
    private Callback mCallback;

    public static NewSubjectDialog newInstance() {
        NewSubjectDialog dialog = new NewSubjectDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (Callback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private EditText subjectAge;
    private EditText subjectHeight;
    private EditText subjectWeight;
    private EditText subjectNumber;
    private Spinner subjectCategory;
    private Spinner subjectGender;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getActivity());
        dialog.title("New Subject");
        dialog.autoDismiss(false);
        View v = getActivity().getLayoutInflater().inflate(R.layout.new_subject_dialog, null);

        subjectAge = (EditText) v.findViewById(R.id.new_subject_age);
        subjectHeight = (EditText) v.findViewById(R.id.new_subject_height);
        subjectWeight = (EditText) v.findViewById(R.id.new_subject_weight);
        subjectNumber = (EditText) v.findViewById(R.id.new_subject_number);
        subjectCategory = (Spinner) v.findViewById(R.id.new_subject_category);
        subjectGender = (Spinner) v.findViewById(R.id.new_subject_gender);

        dialog.customView(v, true);

        dialog.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                if(checkEntries()) {
                    Subject s = createNewSubject();
                    if(mCallback != null) {
                        mCallback.onSubjectCreated(s);
                    }
                } else {
                    popErrorSnackbar();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.positiveText("Done");
        dialog.negativeText("Cancel");

        return dialog.build();
    }

    private boolean checkEntries() {
        return EditTextChecker.checkInput(subjectAge) && EditTextChecker.checkInput(subjectHeight)
                && EditTextChecker.checkInput(subjectWeight) && EditTextChecker.checkInput(subjectNumber);
    }

    private Subject createNewSubject() {
        Subject s = new Subject();
        s.setGender(subjectGender.getSelectedItem().toString());
        s.setAge(Integer.parseInt(subjectAge.getText().toString()));
        s.setNumber(Integer.parseInt(subjectNumber.getText().toString()));
        s.setCategory(subjectCategory.getSelectedItem().toString());
        s.setHeight(Integer.parseInt(subjectHeight.getText().toString()));
        s.setWeight(Integer.parseInt(subjectWeight.getText().toString()));
        return s;
    }

    private void popErrorSnackbar() {
        SnackBar.Builder snack = new SnackBar.Builder(getActivity()).withMessage("Please fill out all fields")
                .withDuration((short) 1000);
        snack.show();
    }
}
