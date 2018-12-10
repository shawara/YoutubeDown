package com.android.shawara.socialdownloader.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shawara.socialdownloader.BuildConfig;
import com.android.shawara.socialdownloader.R;
import com.android.shawara.socialdownloader.model.YoutubeItem;
import com.android.shawara.socialdownloader.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

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

        mProgressbar.setProgress(mItem.getProgress());
        mState.setText(YoutubeItem.states[item.getState()]);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_playStop_imageview) {
            if (mItem.getProgress() > 0) {
//                MimeTypeMap myMime = MimeTypeMap.getSingleton();
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//
//                File file = new File(mItem.getFilePath());
//                intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                mContext.startActivity(Intent.createChooser(intent, "Play Download"));
                File file = new File(mItem.getFilePath());
                Utils.showFile(mContext, file);
            } else
                Toast.makeText(mContext, "the item download progress is 0", Toast.LENGTH_SHORT).show();

        }

    }


}
