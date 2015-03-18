package com.devpaul.datalogger.utils;

import android.widget.EditText;

/**
 * Created by Pauly D on 3/17/2015.
 */
public class EditTextChecker {

    public static boolean checkInput(EditText editText) {
        String s = editText.getText().toString();
        return !s.equals("") && !s.isEmpty() && (s != null);
    }
}
