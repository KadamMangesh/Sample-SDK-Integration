package com.gautam.citruspayassignment.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.gautam.citruspayassignment.R;
import com.gautam.citruspayassignment.listeners.PaymentListener;
import com.gautam.citruspayassignment.listeners.UserSetupListener;
import com.gautam.citruspayassignment.util.Utils;
import com.orhanobut.logger.Logger;

/**
 * Created by Gautam on 05/07/15.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private TextView mUserBalanceAmountTxtView = null;
    private static Context mContext = null;
    private CitrusClient mCitrusClient = null;
    private UserSetupListener mUserSetupListener = null;
    private PaymentListener mPaymentListener = null;
    private Amount mUserBalanceAmount = null;
    private ProgressDialog mProgressDialog = null;

    public static DashboardFragment newInstance(Context context) {
        mContext = context;
        return new DashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCitrusClient = CitrusClient.getInstance(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getUserBalance() {

        mProgressDialog = Utils.getProgressDialog(mContext, Utils.getResourceString(mContext, R.string.please_wait_message_text));
        if (mProgressDialog != null)
            mProgressDialog.show();

        mCitrusClient.getBalance(new com.citrus.sdk.Callback<Amount>() {
            @Override
            public void success(Amount amount) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                Logger.d("Get Balance Success Response =" + amount.toString());
                mUserBalanceAmount = amount;
                String mBalanceAmountStr = Utils.getResourceString(mContext, R.string.dashboard_user_balance_text) + " " + mUserBalanceAmount.getValue() + " " + mUserBalanceAmount.getCurrency();
                mUserBalanceAmountTxtView.setText(mBalanceAmountStr);
            }

            @Override
            public void error(CitrusError error) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                Logger.d("Get Balance Failed Response =" + error.getMessage());
                Utils.getToast(mContext, error.getMessage());
            }
        });
    }

    private void logOutUser() {
        mCitrusClient.signOut(new Callback<CitrusResponse>() {

            @Override
            public void success(CitrusResponse citrusResponse) {
                mUserSetupListener.userLoggedOutEvent();
            }

            @Override
            public void error(CitrusError error) {
                Utils.getToast(mContext, error.getMessage()).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.fragment_user_dashboard, container, false);

        Button mPayBtn = (Button) mRootView.findViewById(R.id.payBtnId);
        Button mLogoutBtn = (Button) mRootView.findViewById(R.id.logoutBtnId);
        Button mMyBalanceBtn = (Button) mRootView.findViewById(R.id.myBalanceBtnId);
        mUserBalanceAmountTxtView = (TextView) mRootView.findViewById(R.id.userBalanceTxtViewId);

        mPayBtn.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);
        mMyBalanceBtn.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mPaymentListener = (PaymentListener) activity;
            mUserSetupListener = (UserSetupListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
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
            case R.id.payBtnId:
                mPaymentListener.paymentInitiated();
                break;
            case R.id.logoutBtnId:
                logOutUser();

//                if (User.logoutUser(mContext)) {
//                    mUserSetupListener.userLoggedOutEvent();
//                } else {
//                    Utils.getToast(mContext, getResources().getString(R.string.logout_failed_msg)).show();
//                }
                break;
            case R.id.myBalanceBtnId:
                getUserBalance();
                break;
        }
    }

}
