package com.gautam.citruspayassignment.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.citrus.mobile.CType;
import com.citrus.sdk.classes.Amount;
import com.gautam.citruspayassignment.R;
import com.gautam.citruspayassignment.constants.AppConstants;
import com.gautam.citruspayassignment.constants.KeyConstants;

/**
 * Created by Gautam on 05/07/15.
 */
public class PaymentOptionsFragment extends Fragment {

    private static final String[] PAYMENT_OPTIONS = new String[]{"CREDIT CARD", "DEBIT CARD", "NET BANKING"};
    private static Amount mAmount = null;
    private static Context mContext = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PaymentOptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentOptionsFragment newInstance(Context context, Amount amount) {
        mContext = context;
        PaymentOptionsFragment mPaymentOptionsFragment = new PaymentOptionsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KeyConstants.KEY_AMOUNT, amount);
        mPaymentOptionsFragment.setArguments(bundle);
        return mPaymentOptionsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle mBundle = getArguments();
            mAmount = mBundle.getParcelable(KeyConstants.KEY_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final Context mContextThemeWrapper = new ContextThemeWrapper(getActivity(), -1);

        LayoutInflater mLayoutInflater = inflater.cloneInContext(mContextThemeWrapper);

        View mRootView = mLayoutInflater.inflate(R.layout.fragment_card_payment, container, false);
        FragmentStatePagerAdapter mFragmentStatePagerAdapter = new PaymentOptionsAdapter(getChildFragmentManager());
        ViewPager mViewPager = (ViewPager) mRootView.findViewById(R.id.paymentOptionsViewPagerId);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mFragmentStatePagerAdapter);

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) mRootView.findViewById(R.id.paymentOptionsTabsId);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(mViewPager);

        return mRootView;
    }

    public class PaymentOptionsAdapter extends FragmentStatePagerAdapter {
        public PaymentOptionsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return CardPaymentFragment.newInstance(mContext,CType.CREDIT, mAmount);
            }
            if (position == 1) {
                return CardPaymentFragment.newInstance(mContext,CType.DEBIT, mAmount);
            } else {
                return NetbankingPaymentFragment.newInstance(mAmount);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAYMENT_OPTIONS[position];
        }

        @Override
        public int getCount() {
            return PAYMENT_OPTIONS.length;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().setTitle(AppConstants.DASHBOARD_FRAGMENT_TITLE);
    }


}
