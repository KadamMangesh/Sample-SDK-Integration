package com.gautam.citruspayassignment.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.gautam.citruspayassignment.R;
import com.gautam.citruspayassignment.constants.KeyConstants;
import com.gautam.citruspayassignment.listeners.UserSetupListener;
import com.gautam.citruspayassignment.util.MySharedPreferences;
import com.gautam.citruspayassignment.util.Utils;

/**
 * Created by Gautam on 05/07/15.
 */
public class UserSetupFragment extends Fragment implements View.OnClickListener {

    private Button mLoginBtn = null;
    private EditText mEmailEdTxt, mPasswordEdTxt = null;
    private LinearLayout mLoginLayout = null;
    private StringBuffer mStringBuffer = null;

    private CitrusClient mCitrusClient = null;
    private UserSetupListener mUserSetupListener = null;
    private static Context mContext = null;
    private MySharedPreferences mMySharedPreferences;
    private ProgressDialog mProgressDialog = null;


   /* private final String emailID = "developercitrus@mailinator.com";
    private final String mobileNo = "9769507476";
    private final String password = "Citrus@123";*/

    public static UserSetupFragment newInstance(Context context) {
        mContext = context;
        return new UserSetupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCitrusClient = CitrusClient.getInstance(mContext);
        mMySharedPreferences = new MySharedPreferences(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.fragment_user_setup, container, false);

        mLoginBtn = (Button) mRootView.findViewById(R.id.loginBtnId);
        mEmailEdTxt = (EditText) mRootView.findViewById(R.id.emailEdTxtId);
        mPasswordEdTxt = (EditText) mRootView.findViewById(R.id.passwordEdtxtId);
        mLoginLayout = (LinearLayout) mRootView.findViewById(R.id.loginLayoutId);

        if (mMySharedPreferences.getPrefBool(KeyConstants.KEY_USER_LOGGED_IN_PREVIOUSLY)) {
            mLoginLayout.setVisibility(View.VISIBLE);
            mLoginBtn.setText(Utils.getResourceString(mContext, R.string.main_activity_login_btn_text));
        }

        mLoginBtn.setOnClickListener(this);


        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mUserSetupListener = (UserSetupListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement UserSetupListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUserSetupListener = null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtnId:
                handleLogin();
                break;
        }

    }

    private void handleLogin() {

        Utils.hideKeyboard(mContext, mLoginBtn);

        if (mLoginLayout.getVisibility() == View.GONE) {
            mLoginLayout.setVisibility(View.VISIBLE);
            mLoginBtn.setText(Utils.getResourceString(mContext, R.string.main_activity_login_btn_text));
        } else {
            if (validateLoginCredentials()) {
                // Credentials are valid start login
                loginInUser();
            } else {
                // Credentials are invalid prompt user
                Utils.getToast(mContext, mStringBuffer.toString()).show();
            }

        }

    }

    private void loginInUser() {

        mProgressDialog = Utils.getProgressDialog(mContext, Utils.getResourceString(mContext, R.string.please_wait_message_text));
        if (mProgressDialog != null)
            mProgressDialog.show();

        String emailId = mEmailEdTxt.getText().toString().trim();
        String password = mPasswordEdTxt.getText().toString();

        mCitrusClient.signIn(emailId, password, new Callback<CitrusResponse>() {
            @Override
            public void success(CitrusResponse citrusResponse) {
//                Utils.getToast(mContext, citrusResponse.getMessage()).show();
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                mUserSetupListener.userLoggedInEvent();
            }

            @Override
            public void error(CitrusError error) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                Utils.getToast(mContext, error.getMessage()).show();
            }
        });
    }

    private boolean validateLoginCredentials() {
        boolean mIsValid = false;
        mStringBuffer = new StringBuffer();

        String mEmailTextStr = mEmailEdTxt.getText().toString().trim();
        if (TextUtils.isEmpty(mEmailTextStr)) {
            mStringBuffer.append(Utils.getResourceString(mContext, R.string.email_id_empty_text));
        } else {
            if (Utils.validateEmail(mEmailTextStr)) {
                mIsValid = true;
            } else {
                mStringBuffer.append(Utils.getResourceString(mContext, R.string.email_id_invalid_text));
            }
        }
        return mIsValid;
    }
}
