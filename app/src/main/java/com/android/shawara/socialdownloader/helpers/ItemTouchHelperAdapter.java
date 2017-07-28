package com.android.shawara.socialdownloader.helpers;

/**
 * Created by shawara on 7/28/2017.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
