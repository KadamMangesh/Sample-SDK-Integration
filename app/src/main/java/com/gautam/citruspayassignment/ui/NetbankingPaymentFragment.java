package com.gautam.citruspayassignment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.NetbankingOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;
import com.gautam.citruspayassignment.R;
import com.gautam.citruspayassignment.constants.AppConstants;
import com.gautam.citruspayassignment.constants.KeyConstants;
import com.gautam.citruspayassignment.listeners.PaymentListener;
import com.gautam.citruspayassignment.util.Utils;

import java.util.ArrayList;

/**
 * Created by Gautam on 05/07/15.
 */
public class NetbankingPaymentFragment extends Fragment {

    private ArrayList<NetbankingOption> mNetbankingOptionsList;
    private Amount mAmount = null;
    private MerchantPaymentOption mMerchantPaymentOption = null;
    private NetbankingAdapter mNetbankingAdapter = null;
    private PaymentListener mPaymentListener = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetbankingPaymentFragment.
     */
    public static NetbankingPaymentFragment newInstance(Amount amount) {
        NetbankingPaymentFragment mNetbankingPaymentFragment = new NetbankingPaymentFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(KeyConstants.KEY_AMOUNT, amount);
        mNetbankingPaymentFragment.setArguments(mBundle);
        return mNetbankingPaymentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAmount = getArguments().getParcelable(KeyConstants.KEY_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootview = inflater.inflate(R.layout.fragment_net_banking, container, false);

        mNetbankingAdapter = new NetbankingAdapter(mNetbankingOptionsList);

        RecyclerView mRecylerViewNetbanking = (RecyclerView) mRootview.findViewById(R.id.recyclerViewNetBankingId);
        mRecylerViewNetbanking.setAdapter(mNetbankingAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecylerViewNetbanking.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecylerViewNetbanking.setLayoutManager(mLayoutManager);

        mRecylerViewNetbanking.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));

        CitrusClient.getInstance(getActivity()).getMerchantPaymentOptions(new Callback<MerchantPaymentOption>() {
            @Override
            public void success(MerchantPaymentOption merchantPaymentOption) {
                mMerchantPaymentOption = merchantPaymentOption;
                mNetbankingAdapter.setNetbankingOptionList(mMerchantPaymentOption.getNetbankingOptionList());
                mNetbankingAdapter.notifyDataSetChanged();

                mNetbankingOptionsList = mMerchantPaymentOption.getNetbankingOptionList();
            }

            @Override
            public void error(CitrusError error) {
                Utils.getToast(getActivity(), error.getMessage()).show();
            }
        });

        return mRootview;
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
    }

    private NetbankingOption getItem(int position) {
        NetbankingOption mNetbankingOption = null;

        if (mNetbankingOptionsList != null && mNetbankingOptionsList.size() > position && position >= -1) {
            mNetbankingOption = mNetbankingOptionsList.get(position);
        }

        return mNetbankingOption;
    }

    private class OnItemClickListener implements RecyclerItemClickListener.OnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            NetbankingOption mNetbankingOption = getItem(position);

            PaymentType mPaymentType;
            CitrusClient mCitrusClient = CitrusClient.getInstance(getActivity());

            Callback<TransactionResponse> mCallback = new Callback<TransactionResponse>() {
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
                mPaymentType = new PaymentType.PGPayment(mAmount, AppConstants.BILL_URL, mNetbankingOption, new CitrusUser(mCitrusClient.getUserEmailId(), mCitrusClient.getUserMobileNumber()));
                mCitrusClient.pgPayment((PaymentType.PGPayment) mPaymentType, mCallback);
            } catch (CitrusException e) {
                e.printStackTrace();

                Utils.getToast(getActivity(), e.getMessage()).show();
            }
        }
    }
}
