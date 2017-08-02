package com.android.shawara.socialdownloader.helpers;

import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shawara on 7/28/2017.
 */

public class RadioGroupMerger {
    private List<RadioGroup> mGroups;
    private int CURRENT_UNCHECKED = -1;
    private int LAST_CHECKED_ID = -1;
    private RadioGroup LAST_CHECKED_GROUP;

    public RadioGroupMerger(RadioGroup... radioGroups) {
        mGroups = Arrays.asList(radioGroups);
        initListeners();
    }

    public RadioGroupMerger(List<RadioGroup> list) {
        mGroups = list;
        initListeners();
    }

    private void initListeners() {
        for (RadioGroup group : mGroups)
            group.clearCheck();

        for (RadioGroup group : mGroups) {
            group.setOnCheckedChangeListener((radioGroup, i) -> {
                        if (i != -1 && i != CURRENT_UNCHECKED) {
                            CURRENT_UNCHECKED = LAST_CHECKED_ID;
                            if (LAST_CHECKED_GROUP != null && LAST_CHECKED_GROUP != radioGroup)
                                LAST_CHECKED_GROUP.clearCheck();
                            LAST_CHECKED_ID = i;
                            LAST_CHECKED_GROUP = radioGroup;
                        }
                    }
            );
        }
    }


    public int getCheckedRadioButtonId() {
        return LAST_CHECKED_ID;
    }

    public RadioGroup getCheckedRadioGroup() {
        return LAST_CHECKED_GROUP;
    }


}
