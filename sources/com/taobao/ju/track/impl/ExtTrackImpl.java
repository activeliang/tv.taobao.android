package com.taobao.ju.track.impl;

import android.content.Context;
import com.taobao.ju.track.csv.CsvFileUtil;
import com.taobao.ju.track.impl.interfaces.IExtTrack;

public class ExtTrackImpl extends TrackImpl implements IExtTrack {
    private static final String DEFAULT_FILE_NAME = "ut_ext.csv";

    public ExtTrackImpl(Context context) {
        super(context, DEFAULT_FILE_NAME);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ExtTrackImpl(Context context, String csvFileName) {
        super(context, !CsvFileUtil.isCSV(csvFileName) ? DEFAULT_FILE_NAME : csvFileName);
    }
}
