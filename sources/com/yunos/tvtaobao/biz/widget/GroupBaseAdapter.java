package com.yunos.tvtaobao.biz.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;

public abstract class GroupBaseAdapter extends BaseAdapter {
    private final String TAG = "GroupBaseAdapter";
    private List<Long> mGroupPositionList;
    private int mTotalItemCount;

    public abstract int getGroupCount();

    public abstract Rect getGroupHintRect();

    public abstract View getGroupHintView(int i, int i2, View view);

    public abstract Rect getGroupItemRect();

    public abstract View getGroupItemView(int i, int i2, int i3, View view);

    public abstract int getItemCount(int i);

    public int getTotalItemCount() {
        return this.mTotalItemCount;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public final int getCount() {
        if (this.mGroupPositionList != null) {
            return this.mGroupPositionList.size();
        }
        return 0;
    }

    public int getItemViewType(int position) {
        if (isGroupHint(position)) {
            return 0;
        }
        return 1;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        Long groupPosition;
        boolean hint = false;
        int itemId = -1;
        int groupId = -1;
        if (!(this.mGroupPositionList == null || (groupPosition = this.mGroupPositionList.get(position)) == null)) {
            itemId = getItemFromGroupPos(groupPosition.longValue());
            groupId = getGroupFromGroupPos(groupPosition.longValue());
            if (itemId == Integer.MAX_VALUE) {
                hint = true;
            }
        }
        if (hint) {
            return getGroupHintView(position, groupId, convertView);
        }
        return getGroupItemView(position, groupId, itemId, convertView);
    }

    public boolean isGroupHint(int position) {
        Long groupPosition;
        if (!(this.mGroupPositionList == null || (groupPosition = this.mGroupPositionList.get(position)) == null)) {
            int item = getItemFromGroupPos(groupPosition.longValue());
            ZpLogger.i("GroupBaseAdapter", "isGroupHint position=" + position + " groupPosition=" + groupPosition + " item=" + item);
            if (item == Integer.MAX_VALUE) {
                return true;
            }
        }
        return false;
    }

    public int getGroupPos(int position) {
        Long groupPos;
        if (this.mGroupPositionList == null || position < 0 || position >= this.mGroupPositionList.size() || (groupPos = this.mGroupPositionList.get(position)) == null) {
            return -1;
        }
        return getGroupFromGroupPos(groupPos.longValue());
    }

    public int getGroupItemPos(int position) {
        Long groupPos;
        if (this.mGroupPositionList == null || position < 0 || position >= this.mGroupPositionList.size() || (groupPos = this.mGroupPositionList.get(position)) == null) {
            return -1;
        }
        return getItemFromGroupPos(groupPos.longValue());
    }

    public void buildGroup() {
        List<Long> positionList = new ArrayList<>();
        int groupCount = getGroupCount();
        if (groupCount <= 1) {
            groupCount = 1;
        }
        this.mTotalItemCount = 0;
        for (int group = 0; group < groupCount; group++) {
            int itemCount = getItemCount(group);
            if (itemCount > 0) {
                this.mTotalItemCount += itemCount;
                positionList.add(Long.valueOf(buildGroupPosition(group, Integer.MAX_VALUE)));
                for (int item = 0; item < itemCount; item++) {
                    positionList.add(Long.valueOf(buildGroupPosition(group, item)));
                }
            }
        }
        this.mGroupPositionList = positionList;
    }

    private long buildGroupPosition(int group, int item) {
        return (((long) group) << 32) | ((long) item);
    }

    private int getGroupFromGroupPos(long groupPos) {
        return (int) (groupPos >> 32);
    }

    private int getItemFromGroupPos(long groupPos) {
        return (int) (2147483647L & groupPos);
    }
}
