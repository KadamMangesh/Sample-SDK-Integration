
package com.gautam.citruspayassignment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gautam.citruspayassignment.constants.AppConstants;

/**
 * Created by Gautam on 05/07/15.
 */
public class MySharedPreferences {

    private Context mContext = null;
    private Editor mEditor = null;
    private SharedPreferences mPrefs = null;

    public MySharedPreferences(final Context aContext) {
        mContext = aContext;
        /******* Create SharedPreferences *******/
        mPrefs = mContext.getSharedPreferences(AppConstants.PREF_FILENAME,
                Context.MODE_PRIVATE);
    }

    /**
     * Gets the pref bool.
     *
     * @param key the key
     * @return the pref bool
     */
    public boolean getPrefBool(final String key) {
        return mPrefs.getBoolean(key, false);
    }

    /**
     * Gets the pref float.
     * 
     * @param key the key
     * @return the pref float
     */
    public float getPrefFloat(final String key) {
        return mPrefs.getFloat(key, 0F);
    }

    /**
     * Gets the pref int.
     * 
     * @param key the key
     * @return the pref int
     */
    public int getPrefInt(final String key) {
        return mPrefs.getInt(key, 0);
    }

    /**
     * Gets the pref long.
     * 
     * @param key the key
     * @return the pref long
     */
    public long getPrefLong(final String key) {
        return mPrefs.getLong(key, 0L);
    }

    /**
     * Gets the pref string.
     * 
     * @param key the key
     * @return the pref string
     */
    public String getPrefString(final String key) {
        return mPrefs.getString(key, null);
    }

    /**
     * ************** Storing data as KEY/VALUE pair ******************.
     * 
     * @param key the key
     * @param value the value
     */

    public void setPrefBool(final String key, final boolean value) {
        mEditor = mPrefs.edit();
        mEditor.putBoolean(key, value); // Saving boolean - true/false
        mEditor.commit();
    }

    /**
     * Sets the pref float.
     * 
     * @param key the key
     * @param value the value
     */
    public void setPrefFloat(final String key, final float value) {
        mEditor = mPrefs.edit();
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    /**
     * Sets the pref int.
     * 
     * @param key the key
     * @param value the value
     */
    public void setPrefInt(final String key, final int value) {
        mEditor = mPrefs.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * Sets the pref long.
     * 
     * @param key the key
     * @param value the value
     */
    public void setPrefLong(final String key, final long value) {
        mEditor = mPrefs.edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * Sets the pref string.
     * 
     * @param key the key
     * @param value the value
     */
    public void setPrefString(final String key, final String value) {
        mEditor = mPrefs.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

}
