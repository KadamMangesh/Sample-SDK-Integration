package com.gautam.citruspayassignment.listeners;

import com.citrus.sdk.TransactionResponse;

/**
 * Created by Gautam on 05/07/15.
 */
public interface PaymentListener {

    void paymentInitiated();

    void paymentCompleted(TransactionResponse transactionResponse);
}
