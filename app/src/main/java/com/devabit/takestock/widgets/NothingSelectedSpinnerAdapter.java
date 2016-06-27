package com.devabit.takestock.widgets;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Collection;

/**
 * Decorator Adapter to allow a Spinner to show a 'Nothing Selected...' initially
 * displayed instead of the first choice in the Adapter.
 */
public class NothingSelectedSpinnerAdapter<T> implements SpinnerAdapter, ListAdapter {

    protected static final int EXTRA = 1;
    protected BaseSpinnerAdapter<T> mAdapter;
    protected Context mContext;
    @LayoutRes protected int mNothingSelectedLayout;
    @LayoutRes protected int mNothingSelectedDropdownLayout;
    @StringRes protected int mHintString;
    protected LayoutInflater mLayoutInflater;

    /**
     * Use this constructor to have NO 'Select One...' item, instead use
     * the standard prompt or nothing at all.
     *
     * @param spinnerAdapter        wrapped Adapter.
     * @param nothingSelectedLayout layout for nothing selected, perhaps
     *                              you want text grayed out like a prompt...
     * @param context
     */
    public NothingSelectedSpinnerAdapter(
            BaseSpinnerAdapter<T> spinnerAdapter,
            @LayoutRes int nothingSelectedLayout,
            @StringRes int hintStringRes,
            Context context) {

        this(spinnerAdapter, nothingSelectedLayout, -1, hintStringRes, context);
    }

    /**
     * Use this constructor to Define your 'Select One...' layout as the first
     * row in the returned choices.
     * If you do this, you probably don't want a prompt on your spinner or it'll
     * have two 'Select' rows.
     *
     * @param spinnerAdapter                wrapped Adapter. Should probably return false for isEnabled(0)
     * @param nothingSelectedLayout         layout for nothing selected, perhaps you want
     *                                      text grayed out like a prompt...
     * @param nothingSelectedDropdownLayout layout for your 'Select an Item...' in
     *                                      the dropdown.
     * @param context
     */
    public NothingSelectedSpinnerAdapter(BaseSpinnerAdapter<T> spinnerAdapter,
                                         @LayoutRes int nothingSelectedLayout,
                                         @LayoutRes int nothingSelectedDropdownLayout,
                                         @StringRes int hintStringRes,
                                         Context context) {
        mAdapter = spinnerAdapter;
        mContext = context.getApplicationContext();
        mNothingSelectedLayout = nothingSelectedLayout;
        mNothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
        mHintString = hintStringRes;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        // This provides the View for the Selected Item in the Spinner, not
        // the dropdown (unless dropdownView is not set).
        if (position == 0) {
            return getNothingSelectedTextView(parent);
        }
        return mAdapter.getView(position - EXTRA, null, parent); // Could re-use
        // the convertView if possible.
    }

    /**
     * View to show in Spinner with Nothing Selected
     * Override this to do something dynamic... e.g. "37 Options Found"
     *
     * @param parent
     * @return
     */
    protected View getNothingSelectedTextView(ViewGroup parent) {
        TextView textView = (TextView) mLayoutInflater.inflate(mNothingSelectedLayout, parent, false);
        textView.setHint(mHintString);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Android BUG! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        if (position == 0) {
            return mNothingSelectedDropdownLayout == -1 ?
                    new View(mContext) :
                    getNothingSelectedDropdownView(parent);
        }

        // Could re-use the convertView if possible, use setTag...
        return mAdapter.getDropDownView(position - EXTRA, null, parent);
    }

    /**
     * Override this to do something dynamic... For example, "Pick your favorite
     * of these 37".
     *
     * @param parent
     * @return
     */
    protected View getNothingSelectedDropdownView(ViewGroup parent) {
        return mLayoutInflater.inflate(mNothingSelectedDropdownLayout, parent, false);
    }

    @Override
    public int getCount() {
        int count = mAdapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public T getItem(int position) {
        return position == 0 ? null : mAdapter.getItem(position - EXTRA);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position >= EXTRA ? mAdapter.getItemId(position - EXTRA) : position - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0; // Don't allow the 'nothing selected'
        // item to be picked.
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mAdapter.getPosition(item) + EXTRA;
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        mAdapter.add(object);
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        mAdapter.addAll(collection);
    }
}
