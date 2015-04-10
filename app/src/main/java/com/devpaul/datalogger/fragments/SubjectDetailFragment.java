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

    /**
     * The titles of the different sets.
     */
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

    /**
     * Array of set and rep counts.
     */
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

    /**
     * The view ids of the different squat sessions.
     */
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

    /**
     * The current subject.
     */
    private Subject subject;

    /**
     * The view group parent.
     */
    private ViewGroup viewGroup;

    private boolean isRecording;

    /**
     * Default empty constructor.
     */
    public SubjectDetailFragment() {
    }

    /**
     * Create a new instance of the fragment.
     * @return a new {@code SubjectDetailFragment}
     */
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

        //set the title of the subject view.
        title.setText("Subject " + subject.getNumber());
        age.setText("" +subject.getAge());
        height.setText(StringFormatter.getFormatedHeightFromInches(getResources(), subject.getHeight()));
        weight.setText(StringFormatter.getFormattedWeight(getResources(), subject.getWeight()));
        category.setText(subject.getCategory());
        circularTextView.setText(""+subject.getNumber());

        String studies = subject.getDoneStudies();

        //set the titles and rep counts for each set squats.
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
            if(studies.contains(getTestName(ids[i]))){
                ImageView imageView = (ImageView) vg.findViewById(R.id.subject_test_info_done_or_not);
                imageView.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.ic_action_action_done));
            }
        }
        isRecording = false;
        return v;
    }

    public void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    /**
     * Handle the click of the view.
     * @param id, the id of the view.
     */
    private void handleClick(int id) {
        showOptionDialog(id);
    }

    /**
     * Show an option dialog based on the id.
     * @param id, the view id.
     */
    private void showOptionDialog(final int id) {
        if(!isRecording) {
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
    }

    /**
     * Creates a file name based on the id.
     * @param id, the id of the view.
     * @return, a filename.
     */
    private String getFileNameFromId(int id) {
        String baseName = "Subject" + subject.getNumber()+"_";
        baseName += getTestName(id);
        return baseName;
    }

    public String getTestName(int id) {
        switch (id) {
            case R.id.normal_squats:
                return "Normal_Squats";
            case R.id.timed_squats:
                return "Timed_Squats";
            case R.id.quarter_squats:
                return "Quarter_Squats";
            case R.id.half_squats:
                return "Half_Squats";
            case R.id.full_squats:
                return "Full_Squats";
            case R.id.block_heel_squats:
                return "Block_Heel_Squats";
            case R.id.block_toe_squats:
                return "Block_Toe_Squats";
            case R.id.block_left_squats:
                return "Block_Left_Squats";
            case R.id.block_right_squats:
                return "Block_Right_Squats";
            default:
                return "";
        }
    }

    /**
     * Marks the image view as done.
     * @param id the id of the view.
     */
    private void markAsDone(int id) {
        ViewGroup vg = (ViewGroup) getView().findViewById(id);
        ImageView imageView = (ImageView) vg.findViewById(R.id.subject_test_info_done_or_not);
        imageView.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.ic_action_action_done));
        String test = getTestName(id);
        String doneStudies = subject.getDoneStudies();
        String[] studies = doneStudies.split(";");
        if(studies.length > 0) {
            boolean foundmatch = false;
            for(int i = 0; i < studies.length; i++) {
                if(studies[i].equals(test)) {
                    foundmatch = true;
                }
            }
            if(!foundmatch) {
                doneStudies+=";";
                doneStudies+=test;
                subject.setDoneStudies(doneStudies);
                long subjectId = subject.getId();
                getDataSource().updateSubject(subject);
                subject = getDataSource().getSubjectById(subjectId);
            }
        }
    }

    /**
     * Marks the image view as not done.
     * @param id the id of the view.
     */
    private void markAsUndone(int id) {
        ViewGroup vg = (ViewGroup) getView().findViewById(id);
        ImageView imageView = (ImageView) vg.findViewById(R.id.subject_test_info_done_or_not);
        imageView.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.ic_action_content_clear));

        String test = getTestName(id);
        String doneStudies = subject.getDoneStudies();
        String[] studies = doneStudies.split(";");
        if(studies.length > 0) {
            String newStudies = "";
            for(int i = 0; i < studies.length; i++) {
                if(!studies[i].equals(test)) {
                    newStudies += studies;
                    if(i <studies.length-1) {
                        newStudies += ";";
                    }
                }
            }
            subject.setDoneStudies(newStudies);
            long subjectId = subject.getId();
            getDataSource().updateSubject(subject);
            subject = getDataSource().getSubjectById(subjectId);
        }
    }
}
