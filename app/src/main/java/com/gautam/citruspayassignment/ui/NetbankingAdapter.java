package com.gautam.citruspayassignment.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citrus.sdk.payment.NetbankingOption;

import java.util.ArrayList;

import static android.R.id.text1;

/**
 * Created by Gautam on 05/07/15.
 */
final class NetbankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NetbankingOption> mNetbankingOptionList = null;

    public NetbankingAdapter(ArrayList<NetbankingOption> netbankingOptionList) {
        this.mNetbankingOptionList = netbankingOptionList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View mItemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        return new NetbankingViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {

        NetbankingViewHolder mNetbankingViewHolder = (NetbankingViewHolder) viewHolder;
        NetbankingOption mNetbankingOption = getItem(index);

        if (mNetbankingOption != null) {
            mNetbankingViewHolder.mBankNameTxtView.setText(mNetbankingOption.getBankName());
        }
    }

    @Override
    public int getItemCount() {
        if (mNetbankingOptionList != null) {
            return mNetbankingOptionList.size();
        } else {
            return 0;
        }
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see #onAttachedToRecyclerView(RecyclerView)
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        if (mNetbankingOptionList != null) {
            mNetbankingOptionList.clear();
        }

        mNetbankingOptionList = null;
    }

    private NetbankingOption getItem(int position) {
        if (mNetbankingOptionList != null && position >= 0 && position < mNetbankingOptionList.size()) {
            return mNetbankingOptionList.get(position);
        }

        return null;
    }

    public static class NetbankingViewHolder extends RecyclerView.ViewHolder {
        final TextView mBankNameTxtView;

        public NetbankingViewHolder(View v) {
            super(v);
            mBankNameTxtView = (TextView) v.findViewById(text1);
        }
    }

    public void setNetbankingOptionList(ArrayList<NetbankingOption> netbankingOptionList) {
        this.mNetbankingOptionList = netbankingOptionList;
    }
}