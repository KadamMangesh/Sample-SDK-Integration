package com.gautam.citruspayassignment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.Constants;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.response.CitrusLogger;
import com.gautam.citruspayassignment.constants.AppConstants;
import com.gautam.citruspayassignment.constants.KeyConstants;
import com.gautam.citruspayassignment.listeners.PaymentListener;
import com.gautam.citruspayassignment.listeners.UserSetupListener;
import com.gautam.citruspayassignment.ui.DashboardFragment;
import com.gautam.citruspayassignment.ui.PaymentOptionsFragment;
import com.gautam.citruspayassignment.ui.UserSetupFragment;
import com.gautam.citruspayassignment.util.MySharedPreferences;
import com.gautam.citruspayassignment.util.Utils;

import static com.citrus.mobile.Config.setEnv;
import static com.citrus.mobile.Config.setSigninId;
import static com.citrus.mobile.Config.setSigninSecret;
import static com.citrus.mobile.Config.setupSignupId;
import static com.citrus.mobile.Config.setupSignupSecret;


public class MainActivity extends ActionBarActivity implements UserSetupListener, PaymentListener {

    private FragmentManager mFragmentManager = null;
    private MySharedPreferences mMySharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        mMySharedPreferences = new MySharedPreferences(this);

        initCitrusClient();

        initView();

    }

    private void initCitrusClient() {
        CitrusClient mCitrusClient = CitrusClient.getInstance(this);
        setEnv("sandbox");

        setupSignupId(AppConstants.SIGNUP_ID);
        setupSignupSecret(AppConstants.SIGNUP_SECRET);

        setSigninId(AppConstants.SIGNIN_ID);
        setSigninSecret(AppConstants.SIGNIN_SECRET);

        CitrusLogger.enableLogs();

        mCitrusClient.init(AppConstants.SIGNUP_ID, AppConstants.SIGNUP_SECRET, AppConstants.SIGNIN_ID, AppConstants.SIGNIN_SECRET, AppConstants.VANITY, AppConstants.ENVIRONMENT);
    }

    private void initView() {

        if (mMySharedPreferences.getPrefBool(KeyConstants.KEY_USER_LOGGED_IN_STATUS)) {
            // User is already logged in.
            loadDashboardFragment();
        } else {
            // User not logged in show login fragment.
            mMySharedPreferences.setPrefBool(KeyConstants.KEY_USER_LOGGED_IN_PREVIOUSLY, false);
            loadLoginFragment();
        }

    }

    private void loadLoginFragment() {
        setTitle(AppConstants.LOGIN_FRAGMENT_TITLE);
        UserSetupFragment mUserSetupFragment = UserSetupFragment.newInstance(this);

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mUserSetupFragment);
        mFragmentTransaction.commit();
    }

    private void loadDashboardFragment() {
        setTitle(AppConstants.DASHBOARD_FRAGMENT_TITLE);
        DashboardFragment mDashboardFragment = DashboardFragment.newInstance(this);

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mDashboardFragment);
        mFragmentTransaction.commit();
    }

    private void loadPaymentOptionsFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PaymentOptionsFragment.newInstance(this, new Amount(AppConstants.DEFAULT_PAY_AMOUNT)));

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitle(AppConstants.PAYMENT_OPTIONS_FRAGMENT_TITLE);
    }


    @Override
    public void userLoggedInEvent() {
        // Load Dashboard Fragment
        mMySharedPreferences.setPrefBool(KeyConstants.KEY_USER_LOGGED_IN_STATUS, true);
        loadDashboardFragment();
    }

    @Override
    public void userLoggedOutEvent() {
        // Load Login Fragment
        Utils.getToast(this, Constants.LOGOUT_SUCCESS_MESSAGE);
        mMySharedPreferences.setPrefBool(KeyConstants.KEY_USER_LOGGED_IN_STATUS, false);
        mMySharedPreferences.setPrefBool(KeyConstants.KEY_USER_LOGGED_IN_PREVIOUSLY, true);
        loadLoginFragment();
    }

    @Override
    public void paymentInitiated() {
        loadPaymentOptionsFragment();
    }

    @Override
    public void paymentCompleted(TransactionResponse transactionResponse) {
        if (transactionResponse != null) {
            Utils.getToast(this, transactionResponse.getMessage()).show();
        }
    }

}

