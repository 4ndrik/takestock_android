package com.devabit.takestock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Victor Artemyev on 11/04/2016.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<String> mStrings;

    public TestAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

        mStrings = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            mStrings.add(UUID.randomUUID().toString());
        }
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_test, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindString(mStrings.get(position));
    }

    @Override public int getItemCount() {
        return mStrings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mTestTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTestTextView = (TextView) itemView.findViewById(R.id.test_text_view);
        }

        void bindString(String string) {
            mTestTextView.setText(string);
        }
    }
}
