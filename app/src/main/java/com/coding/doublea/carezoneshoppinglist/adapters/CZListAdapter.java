package com.coding.doublea.carezoneshoppinglist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coding.doublea.carezoneshoppinglist.R;
import com.coding.doublea.carezoneshoppinglist.models.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 4/4/2015.
 */
public class CZListAdapter extends RecyclerView.Adapter<CZListAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private final LayoutInflater mInflater;
    private ArrayList<ShoppingItem> mData = new ArrayList<>();

    public static interface OnItemClickListener {
        public void onItemClick(ShoppingItem item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCategoryView;
        public TextView mNameView;
        private ShoppingItem mShoppingItem;

        public ViewHolder(View v) {
            super(v);
            mCategoryView = (TextView) v.findViewById(R.id.item_category);
            mNameView = (TextView) v.findViewById(R.id.item_name);
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mShoppingItem);
                    }
                }
            });
        }

        public void bind(ShoppingItem item) {
            mShoppingItem = item;
            mCategoryView.setText(item.getCategory());
            mNameView.setText(item.getName());
        }
    }

    public CZListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CZListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.shopping_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CZListAdapter.ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setData(List<ShoppingItem> data) {
        mData.clear();
        mData.addAll(data);
    }

    public void addData(ShoppingItem item) {
        mData.add(item);
    }

    public void removeData(ShoppingItem item) {
        if(mData.contains(item)) {
            mData.remove(item);
        }
    }

    public ArrayList<ShoppingItem> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
