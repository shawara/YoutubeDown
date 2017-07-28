package com.android.shawara.socialdownloader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shawara.socialdownloader.R;
import com.android.shawara.socialdownloader.helpers.ItemDismissListener;
import com.android.shawara.socialdownloader.helpers.ItemTouchHelperAdapter;
import com.android.shawara.socialdownloader.model.YoutubeItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shawara on 7/25/2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private List<YoutubeItem> mList;
    private ItemDismissListener itemDismissListener;

    public DownloadAdapter(Context c) {
        mContext = c;
        mList = new ArrayList<>();
    }

    public void setItemDismissListener(ItemDismissListener itemDismissListener) {
        this.itemDismissListener = itemDismissListener;
    }

    public void setList(List<YoutubeItem> list) {
        mList = list;
    }

    @Override
    public DownloadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_download, parent, false);
        return new DownloadHolder(v);
    }

    @Override
    public void onBindViewHolder(DownloadHolder holder, int position) {
        holder.bindItem(mList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        if (itemDismissListener != null)
            itemDismissListener.onDismiss();
    }
}
