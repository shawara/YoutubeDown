package com.android.shawara.socialdownloader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.shawara.socialdownloader.R;
import com.android.shawara.socialdownloader.model.YoutubeItem;
import com.squareup.picasso.Picasso;

/**
 * Created by shawara on 7/25/2017.
 */

public class DownloadHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private YoutubeItem mItem;
    private Context mContext;

    private ImageView mThumbnail, mPlayPause;
    private TextView mTitle, mState;
    private ProgressBar mProgressbar;

    public DownloadHolder(View itemView) {
        super(itemView);

        mThumbnail = (ImageView) itemView.findViewById(R.id.item_thumbnail_imageview);
        mPlayPause = (ImageView) itemView.findViewById(R.id.item_playStop_imageview);
        mTitle = (TextView) itemView.findViewById(R.id.item_title_textview);
        mState = (TextView) itemView.findViewById(R.id.item_state_textview);
        mProgressbar = (ProgressBar) itemView.findViewById(R.id.item_download_progressbar);

        mPlayPause.setOnClickListener(this);


    }

    public void bindItem(YoutubeItem item, Context context) {
        mItem = item;
        mContext = context;

        Picasso.with(context).load(item.getThumbnail_url()).into(mThumbnail);
        mTitle.setText(mItem.getTitle());

        int progress =item.getTotalSize()>0?(int)( (100 * item.getCurrentSize()) / item.getTotalSize()):0;
        mProgressbar.setProgress(progress);
        mState.setText(item.getState());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_playStop_imageview) {

        }

    }


}
