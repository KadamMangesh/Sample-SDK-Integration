package com.gautam.citruspayassignment.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.citrus.card.Card;
import com.citrus.mobile.CType;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.classes.Month;
import com.citrus.sdk.classes.Year;
import com.citrus.sdk.payment.CardOption;
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.DebitCardOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.widgets.CardNumberEditText;
import com.citrus.widgets.ExpiryDate;
import com.gautam.citruspayassignment.R;
import com.gautam.citruspayassignment.constants.AppConstants;
import com.gautam.citruspayassignment.constants.KeyConstants;
import com.gautam.citruspayassignment.listeners.PaymentListener;
import com.gautam.citruspayassignment.util.Utils;

/**
 * Created by Gautam on 05/07/15.
 */
public class CardPaymentFragment extends Fragment implements View.OnClickListener {

    private CardNumberEditText mCardNumberEdTxt = null;
    private ExpiryDate mExpiryDate = null;
    private EditText mCVVEdTxt = null, mCardHolderNameEdTxt = null, mCardHolderNickNameEdTxt = null;
    private TextView mPayBtn = null;
    private Card mCard;
    private CType cType = CType.DEBIT;
    private Amount mAmount = null;
    private static Context mContext = null;
    private PaymentListener mPaymentListener = null;


    public static CardPaymentFragment newInstance(Context context, CType cType, Amount amount) {

        mContext = context;
        CardPaymentFragment mCardPaymentFragment = new CardPaymentFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(KeyConstants.KEY_CTYPE, cType);
        mBundle.putParcelable(KeyConstants.KEY_AMOUNT, amount);
        mCardPaymentFragment.setArguments(mBundle);

        return mCardPaymentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle mBundle = getArguments();
            cType = (CType) mBundle.getSerializable(KeyConstants.KEY_CTYPE);
            mAmount = mBundle.getParcelable(KeyConstants.KEY_AMOUNT);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mPaymentListener = (PaymentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPaymentListener = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_card_payment_option, container,
                false);

        mCardNumberEdTxt = (CardNumberEditText) mRootView
                .findViewById(R.id.cardHolderNumberEdTxtId);
        mExpiryDate = (ExpiryDate) mRootView.findViewById(R.id.cardExpiryDateId);
        mCardHolderNameEdTxt = (EditText) mRootView.findViewById(R.id.cardHolderNameEdTxtId);
        mCardHolderNickNameEdTxt = (EditText) mRootView.findViewById(R.id.cardHolderNickNameEdTxtId);
        mCVVEdTxt = (EditText) mRootView.findViewById(R.id.cardCvv);
        mPayBtn = (TextView) mRootView.findViewById(R.id.payBtnId);
        mPayBtn.setOnClickListener(this);

        mPayBtn.setText(Utils.getResourceString(mContext, R.string.main_activity_login_btn_pay_text));

        return mRootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.payBtnId:
                Utils.hideKeyboard(mContext, mPayBtn);
                handlePayment();
                break;
        }
    }

    private void handlePayment() {

        String mCardHolderNameStr = mCardHolderNameEdTxt.getText().toString();
        String mCardNumberStr = mCardNumberEdTxt.getText().toString();
        String mCardCVVStr = mCVVEdTxt.getText().toString();
        Month mMonth = mExpiryDate.getMonth();
        Year mYear = mExpiryDate.getYear();

        CardOption mCardOption;
        if (cType == CType.DEBIT) {
            mCardOption = new DebitCardOption(mCardHolderNameStr, mCardNumberStr, mCardCVVStr, mMonth, mYear);
        } else {
            mCardOption = new CreditCardOption(mCardHolderNameStr, mCardNumberStr, mCardCVVStr, mMonth, mYear);
        }

        PaymentType paymentType;
        CitrusClient mCitrusClient = CitrusClient.getInstance(getActivity());

        Callback<TransactionResponse> callback = new Callback<TransactionResponse>() {
            @Override
            public void success(TransactionResponse transactionResponse) {
                mPaymentListener.paymentCompleted(transactionResponse);
            }

            @Override
            public void error(CitrusError error) {
                Utils.getToast(getActivity(), error.getMessage()).show();
            }
        };

        try {

            paymentType = new PaymentType.PGPayment(mAmount, AppConstants.BILL_URL, mCardOption, new CitrusUser(mCitrusClient.getUserEmailId(), mCitrusClient.getUserMobileNumber()));
            mCitrusClient.pgPayment((PaymentType.PGPayment) paymentType, callback);
        } catch (CitrusException e) {
            e.printStackTrace();

            Utils.getToast(getActivity(), e.getMessage()).show();
        }
    }
}
