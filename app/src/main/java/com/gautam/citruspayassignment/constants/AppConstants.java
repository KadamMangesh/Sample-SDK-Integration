package com.gautam.citruspayassignment.constants;

import com.citrus.sdk.Environment;

/**
 * Created by Gautam on 05/07/15.
 */
public class AppConstants {
    public static final String EMAIL_VALIDATE_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String BILL_URL = "https://salty-plateau-1529.herokuapp.com/billGenerator.sandbox.php";
    public static final String SIGNUP_ID = "test-signup";
    public static final String SIGNUP_SECRET = "c78ec84e389814a05d3ae46546d16d2e";
    public static final String SIGNIN_ID = "test-signin";
    public static final String SIGNIN_SECRET = "52f7e15efd4208cf5345dd554443fd99";
    public static final String VANITY = "prepaid";
    public static final Environment ENVIRONMENT = Environment.SANDBOX;

    public static final String PREF_FILENAME = "AppPrefs";

    public static final String LOGIN_FRAGMENT_TITLE ="Welcome";
    public static final String DASHBOARD_FRAGMENT_TITLE ="Dashboard";
    public static final String PAYMENT_OPTIONS_FRAGMENT_TITLE ="Payment Options";

    public static final String DEFAULT_PAY_AMOUNT = "10";
}
