package com.iflytek.voicedemo;

/**
 * Created by Astrid_Ti on 2017/4/5.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.iflytek.voicedemo.MyAdapter.ViewHolder.id_adapter;
import static com.iflytek.voicedemo.MyAdapter.ViewHolder.sentiment_adapter;
import static com.iflytek.voicedemo.MyAdapter.ViewHolder.word_adapter;

/**
 * Created by ff_ti on 4/5/17.
 */


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<String> mDataset1;
    private ArrayList<String> mDataset2;
    private ArrayList<String> mDataset3;
    Context context;
    Cursor cursor;
    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public static final String sentiment_adapter = "sentiment_adapter";
        public static final String word_adapter = "word_adapter";
        public static final String time_adapter = "word_adapter";
        public static final String id_adapter = "id_adapter";

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        mDataset1.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset1.indexOf(item);
        mDataset1.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset1, ArrayList<String> myDataset2, ArrayList<String> myDataset3, Context con, Cursor cursor) {
        mDataset1 = myDataset1;
        mDataset2 = myDataset2;
        mDataset3 = myDataset3;
        //mDataset3 = myDataset3;
        context = con;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? cursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (cursor != null) {
            cursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = mDataset2.get(position);
        holder.txtHeader.setText(mDataset2.get(position));
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove(name);
                Intent intent = new Intent(context, Details.class);
                intent.putExtra(sentiment_adapter, mDataset2.get(position));
                intent.putExtra(word_adapter,mDataset1.get(position));
                intent.putExtra(id_adapter,mDataset3.get(position));
                //intent.putExtra(time_adapter,mDataset3.get(position).toString());
                context.startActivity(intent);
            }
        });

        holder.txtFooter.setText(mDataset1.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset1.size();
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
//        if (oldCursor != null && mDataSetObserver != null) {
//            oldCursor.unregisterDataSetObserver(mDataSetObserver);
//        }
        cursor = newCursor;
        if (cursor != null) {
//            if (mDataSetObserver != null) {
//                cursor.registerDataSetObserver(mDataSetObserver);
//            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}