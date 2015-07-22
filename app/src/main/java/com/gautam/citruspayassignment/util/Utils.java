package com.gautam.citruspayassignment.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gautam.citruspayassignment.constants.AppConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Gautam on 05/07/15.
 */
public class Utils {

    public static String getResourceString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static Toast getToast(Context context, String toastMessage) {
        return Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean validateEmail(final String str) {
        Pattern pattern = null;
        Matcher matcher;

        pattern = Pattern.compile(AppConstants.EMAIL_VALIDATE_PATTERN);
        matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
