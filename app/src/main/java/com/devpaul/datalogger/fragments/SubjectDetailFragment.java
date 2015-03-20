package com.devpaul.datalogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devpaul.circulartextview.CircularTextView;
import com.devpaul.datalogger.DataLoggingActivity;
import com.devpaul.datalogger.R;
import com.devpaul.datalogger.data.Subject;
import com.devpaul.datalogger.utils.StringFormatter;

/**
 * Created by Pauly D on 3/18/2015.
 */
public class SubjectDetailFragment extends BaseDatasourceFragment {

    private static final String[] titles = new String[] {
            "Normal Squats",
            "Varied Speed",
            "Quarter Squats",
            "Half Squats",
            "Full Squats",
            "Block-Heel Squats",
            "Block-Toes Squats",
            "Block-Left Squats",
            "Block-Right Squats"
    };

    private static final String[] repcounts = new String[] {
            "2 x 7",
            "3 x 5",
            "1 x 3",
            "1 x 3",
            "1 x 3",
            "1 x 3",
            "1 x 3",
            "1 x 3",
            "1 x 3"
    };

    private static final int[] ids = new int[] {
            R.id.normal_squats,
            R.id.timed_squats,
            R.id.quarter_squats,
            R.id.half_squats,
            R.id.full_squats,
            R.id.block_heel_squats,
            R.id.block_toe_squats,
            R.id.block_left_squats,
            R.id.block_right_squats
    };


    private Subject subject;
    private ViewGroup viewGroup;

    public SubjectDetailFragment() {
    }

    public static SubjectDetailFragment newInstance() {
        return new SubjectDetailFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        long id = bundle.getLong(DataLoggingActivity.SUBJECT_ID);
        subject = getDataSource().getSubjectById(id);

        View v = inflater.inflate(R.layout.fragment_subject_detail, container, false);
        viewGroup = (ViewGroup) v.findViewById(R.id.fragment_subject_detail_card);
        TextView title = (TextView) viewGroup.findViewById(R.id.subject_title);
        TextView age = (TextView) viewGroup.findViewById(R.id.subject_age);
        TextView height = (TextView) viewGroup.findViewById(R.id.subject_height);
        TextView weight = (TextView) viewGroup.findViewById(R.id.subject_weight);
        TextView category = (TextView) viewGroup.findViewById(R.id.subject_category);
        CircularTextView circularTextView = (CircularTextView) viewGroup.findViewById(R.id.circluar_text_view);

        title.setText("Subject " + subject.getNumber());
        age.setText("" +subject.getAge());
        height.setText(StringFormatter.getFormatedHeightFromInches(getResources(), subject.getHeight()));
        weight.setText(StringFormatter.getFormattedWeight(getResources(), subject.getWeight()));
        category.setText(subject.getCategory());
        circularTextView.setText(""+subject.getNumber());

        for(int i =0; i < ids.length; i++) {
            ViewGroup vg = (ViewGroup) v.findViewById(ids[i]);
            TextView name = (TextView) vg.findViewById(R.id.subject_test_info_title);
            TextView setReps = (TextView) vg.findViewById(R.id.subject_test_info_set_and_rep);
            name.setText(titles[i]);
            setReps.setText(repcounts[i]);
            vg.setFocusable(true);
            vg.setClickable(true);
            vg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClick(v.getId());
                }
            });
        }

        return v;
    }

    private void handleClick(int id) {
        showOptionDialog(id);
    }

    private void showOptionDialog(final int id) {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getActivity())
                .title("Options")
                .items(R.array.study_options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if(i == 0) {
                            //mark as done.
                            markAsDone(id);
                        } else if(i == 1) {
                            //start recording.
                            ((DataLoggingActivity) getActivity()).startRecording(getFileNameFromId(id));
                        } else if(i == 2) {
                            markAsUndone(id);
                        }
                    }
                });
        dialog.cancelable(false);
        dialog.positiveText("Cancel");
        dialog.show();
    }

    private String getFileNameFromId(int id) {
        String baseName = "Subject" + subject.getNumber()+"_";
        switch (id) {
            case R.id.normal_squats:
                baseName += "Normal_Squats";
                break;
            case R.id.timed_squats:
                baseName += "Timed_Squats";
                break;
            case R.id.quarter_squats:
                baseName+= "Quarter_Squats";
                break;
            case R.id.half_squats:
                baseName += "Half_Squats";
                break;
            case R.id.full_squats:
                baseName += "Full_Squats";
                break;
            case R.id.block_heel_squats:
                baseName += "Block_Heel_Squats";
                break;
            case R.id.block_toe_squats:
                baseName += "Block_Toe_Squats";
                break;
            case R.id.block_left_squats:
                baseName += "Block_Left_Squats";
                break;
            case R.id.block_right_squats:
                baseName += "Block_Right_Squats";
                break;
            default:
                break;
        }
        return baseName;
    }
    private void markAsDone(int id) {
        ViewGroup vg = (ViewGroup) getView().findViewById(id);
        ImageView imageView = (ImageView) vg.findViewById(R.id.subject_test_info_done_or_not);
        imageView.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.ic_action_action_done));
    }

    private void markAsUndone(int id) {
        ViewGroup vg = (ViewGroup) getView().findViewById(id);
        ImageView imageView = (ImageView) vg.findViewById(R.id.subject_test_info_done_or_not);
        imageView.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.ic_action_content_clear));
    }
}
