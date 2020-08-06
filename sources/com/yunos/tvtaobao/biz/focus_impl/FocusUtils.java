package com.yunos.tvtaobao.biz.focus_impl;

import android.graphics.Rect;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import com.yunos.tvtaobao.biz.focus_impl.FocusFinder;
import com.yunos.tvtaobao.biz.focus_impl.FocusNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FocusUtils {
    public static FocusNode findInBrothers(final FocusNode child, final int keyCode, KeyEvent event) {
        List<FocusNode> possibleList = new ArrayList<>();
        if (child.getParentNode() != null) {
            for (Pair<FocusNode, Rect> iterator : child.getParentNode().getFocusChildren()) {
                if (!(iterator.first == child || ((FocusNode) iterator.first).getRectInParentNode() == null || child.getRectInParentNode() == null)) {
                    if (keyCode == 19) {
                        if (((FocusNode) iterator.first).getRectInParentNode().top < child.getRectInParentNode().top && ((FocusNode) iterator.first).getRectInParentNode().bottom < child.getRectInParentNode().bottom && ((FocusNode) iterator.first).getRectInParentNode().right > child.getRectInParentNode().left && ((FocusNode) iterator.first).getRectInParentNode().left < child.getRectInParentNode().right) {
                            possibleList.add(iterator.first);
                        }
                    } else if (keyCode == 20) {
                        if (((FocusNode) iterator.first).getRectInParentNode().bottom > child.getRectInParentNode().bottom && ((FocusNode) iterator.first).getRectInParentNode().top > child.getRectInParentNode().top && ((FocusNode) iterator.first).getRectInParentNode().right > child.getRectInParentNode().left && ((FocusNode) iterator.first).getRectInParentNode().left < child.getRectInParentNode().right) {
                            possibleList.add(iterator.first);
                        }
                    } else if (keyCode == 21) {
                        if (((FocusNode) iterator.first).getRectInParentNode().left < child.getRectInParentNode().left && ((FocusNode) iterator.first).getRectInParentNode().right < child.getRectInParentNode().right && ((FocusNode) iterator.first).getRectInParentNode().bottom > child.getRectInParentNode().top && ((FocusNode) iterator.first).getRectInParentNode().top < child.getRectInParentNode().bottom) {
                            possibleList.add(iterator.first);
                        }
                    } else if (keyCode == 22 && ((FocusNode) iterator.first).getRectInParentNode().right > child.getRectInParentNode().right && ((FocusNode) iterator.first).getRectInParentNode().left > child.getRectInParentNode().left && ((FocusNode) iterator.first).getRectInParentNode().bottom > child.getRectInParentNode().top && ((FocusNode) iterator.first).getRectInParentNode().top < child.getRectInParentNode().bottom) {
                        possibleList.add(iterator.first);
                    }
                }
            }
        }
        Collections.sort(possibleList, new Comparator<FocusNode>() {
            public int compare(FocusNode o1, FocusNode o2) {
                int distanceO1 = 0;
                int distanceO2 = 0;
                if (keyCode == 19) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().centerX() - child.getRectInParentNode().centerX()), 2.0d) + Math.pow((double) (o1.getRectInParentNode().bottom - child.getRectInParentNode().top), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().centerX() - child.getRectInParentNode().centerX()), 2.0d) + Math.pow((double) (o2.getRectInParentNode().bottom - child.getRectInParentNode().top), 2.0d));
                } else if (keyCode == 20) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().centerX() - child.getRectInParentNode().centerX()), 2.0d) + Math.pow((double) (o1.getRectInParentNode().top - child.getRectInParentNode().bottom), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().centerX() - child.getRectInParentNode().centerX()), 2.0d) + Math.pow((double) (o2.getRectInParentNode().top - child.getRectInParentNode().bottom), 2.0d));
                } else if (keyCode == 21) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().right - child.getRectInParentNode().left), 2.0d) + Math.pow((double) (o1.getRectInParentNode().centerY() - child.getRectInParentNode().centerY()), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().right - child.getRectInParentNode().left), 2.0d) + Math.pow((double) (o2.getRectInParentNode().centerY() - child.getRectInParentNode().centerY()), 2.0d));
                } else if (keyCode == 22) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().left - child.getRectInParentNode().right), 2.0d) + Math.pow((double) (o1.getRectInParentNode().centerY() - child.getRectInParentNode().centerY()), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().left - child.getRectInParentNode().right), 2.0d) + Math.pow((double) (o2.getRectInParentNode().centerY() - child.getRectInParentNode().centerY()), 2.0d));
                }
                return distanceO1 - distanceO2;
            }
        });
        Collections.sort(possibleList, new Comparator<FocusNode>() {
            public int compare(FocusNode o1, FocusNode o2) {
                long rlt = o2.getPriority() - o1.getPriority();
                if (rlt > 0) {
                    return 1;
                }
                return rlt < 0 ? -1 : 0;
            }
        });
        if (!possibleList.isEmpty()) {
            return possibleList.get(0);
        }
        return null;
    }

    public static FocusNode findInChildren(final FocusNode from, final int keyCode, KeyEvent event) {
        if (isLeafFocusNode(from)) {
            return null;
        }
        List<FocusNode> possibleList = new ArrayList<>();
        if (!(from == null || from.getBinder() == null || from.getBinder().getView() == null)) {
            for (Pair<FocusNode, Rect> iterator : from.getFocusChildren()) {
                if (((FocusNode) iterator.first).getRectInParentNode() != null) {
                    if (keyCode == 19) {
                        if (((FocusNode) iterator.first).getRectInParentNode().top < from.getBinder().getView().getHeight()) {
                            possibleList.add(iterator.first);
                        }
                    } else if (keyCode == 20) {
                        if (((FocusNode) iterator.first).getRectInParentNode().bottom > 0) {
                            possibleList.add(iterator.first);
                        }
                    } else if (keyCode == 21) {
                        if (((FocusNode) iterator.first).getRectInParentNode().left < from.getBinder().getView().getWidth()) {
                            possibleList.add(iterator.first);
                        }
                    } else if (keyCode == 22 && ((FocusNode) iterator.first).getRectInParentNode().right > 0) {
                        possibleList.add(iterator.first);
                    }
                }
            }
        }
        Collections.sort(possibleList, new Comparator<FocusNode>() {
            public int compare(FocusNode o1, FocusNode o2) {
                int distanceO1 = 0;
                int distanceO2 = 0;
                if (keyCode == 19) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().centerX() - (from.getBinder().getView().getWidth() / 2)), 2.0d) + Math.pow((double) (o1.getRectInParentNode().bottom - from.getBinder().getView().getHeight()), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().centerX() - (from.getBinder().getView().getWidth() / 2)), 2.0d) + Math.pow((double) (o2.getRectInParentNode().bottom - from.getBinder().getView().getHeight()), 2.0d));
                } else if (keyCode == 20) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().centerX() - (from.getBinder().getView().getWidth() / 2)), 2.0d) + Math.pow((double) (o1.getRectInParentNode().top + 0), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().centerX() - (from.getBinder().getView().getWidth() / 2)), 2.0d) + Math.pow((double) (o2.getRectInParentNode().top + 0), 2.0d));
                } else if (keyCode == 21) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().right - from.getBinder().getView().getWidth()), 2.0d) + Math.pow((double) (o1.getRectInParentNode().centerY() - (from.getBinder().getView().getHeight() / 2)), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().right - from.getBinder().getView().getWidth()), 2.0d) + Math.pow((double) (o2.getRectInParentNode().centerY() - (from.getBinder().getView().getHeight() / 2)), 2.0d));
                } else if (keyCode == 22) {
                    distanceO1 = (int) (Math.pow((double) (o1.getRectInParentNode().left + 0), 2.0d) + Math.pow((double) (o1.getRectInParentNode().centerY() - (from.getBinder().getView().getHeight() / 2)), 2.0d));
                    distanceO2 = (int) (Math.pow((double) (o2.getRectInParentNode().left + 0), 2.0d) + Math.pow((double) (o2.getRectInParentNode().centerY() - (from.getBinder().getView().getHeight() / 2)), 2.0d));
                }
                return distanceO1 - distanceO2;
            }
        });
        Collections.sort(possibleList, new Comparator<FocusNode>() {
            public int compare(FocusNode o1, FocusNode o2) {
                long rlt = o2.getPriority() - o1.getPriority();
                if (rlt > 0) {
                    return 1;
                }
                return rlt < 0 ? -1 : 0;
            }
        });
        if (!possibleList.isEmpty()) {
            return possibleList.get(0);
        }
        return null;
    }

    public static boolean isLeafFocusNode(FocusNode focusNode) {
        if (focusNode == null || focusNode.getInnerNode() != null || focusNode.getBinder() == null || focusNode.getBinder().getView() == null || !focusNode.getFocusChildren().isEmpty()) {
            return false;
        }
        return true;
    }

    public static List<FocusNode> calculateFocusPath(FocusNode focusNode) {
        if (focusNode == null) {
            return null;
        }
        List<FocusNode> rtn = new ArrayList<>();
        while (focusNode != null) {
            rtn.add(focusNode);
            focusNode = focusNode.getParentNode();
        }
        return rtn;
    }

    public static boolean buildFocusPath(FocusNode startNode, FocusNode stopNode) {
        return buildFocusPath(startNode, stopNode, false);
    }

    public static boolean buildFocusPath(FocusNode startNode, FocusNode stopNode, boolean excludeStopNode) {
        List<FocusNode> nowList;
        if (startNode == null) {
            return false;
        }
        if ((stopNode != null && !isNodeInParent(startNode, stopNode)) || (nowList = calculateFocusPath(startNode)) == null) {
            return false;
        }
        for (int i = 0; i < nowList.size(); i++) {
            if (i + 1 < nowList.size()) {
                if (!excludeStopNode) {
                    nowList.get(i + 1).setInnerNode(nowList.get(i));
                    if (nowList.get(i + 1) == stopNode) {
                        break;
                    }
                } else if (nowList.get(i + 1) == stopNode) {
                    break;
                } else {
                    nowList.get(i + 1).setInnerNode(nowList.get(i));
                }
            }
        }
        return true;
    }

    public static void syncFocusState(FocusNode newLeafNode, FocusNode oldLeafNode, FocusNode stopNode) {
        if (newLeafNode != null) {
            List<FocusNode> nowList = calculateFocusPath(newLeafNode);
            List<FocusNode> oldList = calculateFocusPath(oldLeafNode);
            List<FocusNode> listSame = new ArrayList<>();
            if (!(nowList == null || oldList == null)) {
                for (int i = 0; i < oldList.size(); i++) {
                    for (int j = 0; j < nowList.size(); j++) {
                        if (nowList.get(j) == oldList.get(i)) {
                            listSame.add(oldList.get(i));
                        }
                    }
                }
            }
            buildFocusPath(newLeafNode, stopNode);
            for (int i2 = 0; i2 < listSame.size(); i2++) {
                if (nowList != null) {
                    nowList.remove(listSame.get(i2));
                }
                if (oldList != null) {
                    oldList.remove(listSame.get(i2));
                }
            }
            int i3 = 0;
            while (oldList != null && i3 < oldList.size()) {
                if (oldList.get(i3) != null) {
                    if (oldList.get(i3) == stopNode) {
                        break;
                    }
                    oldList.get(i3).onFocusLeave();
                }
                i3++;
            }
            int i4 = 0;
            while (nowList != null && i4 < nowList.size()) {
                if (nowList.get(i4) != null) {
                    if (nowList.get(i4) == stopNode) {
                        break;
                    } else if (!nowList.get(i4).isNodeHasFocus()) {
                        nowList.get(i4).onFocusEnter();
                    }
                }
                i4++;
            }
            if (newLeafNode == oldLeafNode) {
                newLeafNode.onFocusEnter();
            }
        }
    }

    public static FocusNode findNext(FocusFinder.Routine routine, FocusNode currFocusOnNode, int keyCode, KeyEvent event) {
        if (currFocusOnNode == null) {
            return null;
        }
        FocusNode next = currFocusOnNode.findNext(routine, keyCode, event);
        FocusNode iterator = currFocusOnNode.getParentNode();
        int loopCount = 20;
        while (next == null) {
            loopCount--;
            if (iterator == null) {
                return next;
            }
            next = iterator.findNext(routine, keyCode, event);
            iterator = iterator.getParentNode();
            if (loopCount < 0) {
                return next;
            }
        }
        return next;
    }

    public static boolean isNodeInParent(FocusNode node, FocusNode parent) {
        boolean rtn = false;
        if (parent != null && node != null) {
            if (parent == node) {
                return true;
            }
            Iterator<Pair<FocusNode, Rect>> it = parent.getFocusChildren().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair<FocusNode, Rect> iterator = it.next();
                if (!(iterator == null || iterator.first == null)) {
                    if (iterator.first == node) {
                        rtn = true;
                        break;
                    }
                    rtn = isNodeInParent(node, (FocusNode) iterator.first);
                    if (rtn) {
                        break;
                    }
                }
            }
        }
        return rtn;
    }

    public static FocusNode findPriorityLeaf(FocusNode node) {
        if (node != null) {
            if (isLeafFocusNode(node)) {
                return node;
            }
            FocusNode leaf = null;
            long priority = Long.MIN_VALUE;
            for (Pair<FocusNode, Rect> iterator : node.getFocusChildren()) {
                if (!(iterator == null || iterator.first == null || iterator.second == null || priority >= ((FocusNode) iterator.first).getPriority() || ((FocusNode) iterator.first).getPriority() == 0)) {
                    priority = ((FocusNode) iterator.first).getPriority();
                    leaf = (FocusNode) iterator.first;
                }
            }
            if (leaf != null) {
                return findPriorityLeaf(leaf);
            }
        }
        return null;
    }

    public static FocusNode findClosestLeaf2LT(FocusNode node) {
        int tmpDis;
        if (node == null) {
            return null;
        }
        if (isLeafFocusNode(node)) {
            return node;
        }
        FocusNode leaf = null;
        int distance = Integer.MAX_VALUE;
        for (Pair<FocusNode, Rect> iterator : node.getFocusChildren()) {
            if (!(iterator == null || iterator.first == null || iterator.second == null || (tmpDis = (int) (Math.pow((double) ((Rect) iterator.second).left, 2.0d) + Math.pow((double) ((Rect) iterator.second).left, 2.0d))) >= distance)) {
                distance = tmpDis;
                leaf = (FocusNode) iterator.first;
            }
        }
        if (leaf != null) {
            return findClosestLeaf2LT(leaf);
        }
        return node;
    }

    public static boolean focusLeaveToLeaf(FocusNode focusNode) {
        boolean rtn = false;
        for (FocusNode iterator = focusNode; iterator != null; iterator = iterator.getInnerNode()) {
            if (iterator.isNodeHasFocus()) {
                rtn |= iterator.onFocusLeave();
            }
        }
        return rtn;
    }

    public static boolean focusEnterToLeaf(FocusNode focusNode) {
        FocusNode iterator = focusNode;
        boolean rtn = false;
        while (iterator != null && iterator.isNodeFocusable()) {
            if (!iterator.isNodeHasFocus()) {
                rtn |= iterator.onFocusEnter();
            }
            for (Pair<FocusNode, Rect> tmp : iterator.getFocusChildren()) {
                if (tmp.first != iterator.getInnerNode()) {
                    focusLeaveToLeaf((FocusNode) tmp.first);
                }
            }
            iterator = iterator.getInnerNode();
        }
        return rtn;
    }

    public static boolean focusClickToLeaf(FocusNode focusNode) {
        if (focusNode == null || !focusNode.isNodeHasFocus()) {
            return false;
        }
        boolean rtn = false;
        List<FocusNode> list = new ArrayList<>();
        for (FocusNode iterator = focusNode; iterator != null; iterator = iterator.getInnerNode()) {
            list.add(iterator);
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            rtn = list.get(i).onFocusClick();
            if (rtn) {
                return rtn;
            }
        }
        return rtn;
    }

    private static List<Pair<FocusNode, Rect>> buildBranch2(FocusNode startNode, View notNodeView) {
        List<Pair<FocusNode, Rect>> rtn = new ArrayList<>();
        if (!(notNodeView == null || startNode == null)) {
            try {
                if (!(startNode.getBinder() == null || startNode.getBinder().getView() == null || !(notNodeView instanceof ViewGroup))) {
                    for (int i = 0; i < ((ViewGroup) notNodeView).getChildCount(); i++) {
                        View child = ((ViewGroup) notNodeView).getChildAt(i);
                        if (!(child instanceof FocusNode.Binder)) {
                            rtn.addAll(buildBranch2(startNode, child));
                        } else if (child.getVisibility() == 0 && ((FocusNode.Binder) child).getNode().isNodeFocusable()) {
                            Rect tmpRect = new Rect();
                            child.getDrawingRect(tmpRect);
                            ((ViewGroup) startNode.getBinder().getView()).offsetDescendantRectToMyCoords(child, tmpRect);
                            ((FocusNode.Binder) child).getNode().setRectInParentNode(tmpRect);
                            ((FocusNode.Binder) child).getNode().setParentNode(startNode);
                            rtn.add(new Pair(((FocusNode.Binder) child).getNode(), tmpRect));
                        }
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return rtn;
    }

    public static List<Pair<FocusNode, Rect>> buildBranch(FocusNode branchNode) {
        List<Pair<FocusNode, Rect>> rtn = new ArrayList<>();
        if (branchNode != null) {
            try {
                if (!(branchNode.getBinder() == null || branchNode.getBinder().getView() == null)) {
                    View attachedView = branchNode.getBinder().getView();
                    if (attachedView instanceof ViewGroup) {
                        for (int i = 0; i < ((ViewGroup) attachedView).getChildCount(); i++) {
                            View child = ((ViewGroup) attachedView).getChildAt(i);
                            if (!(child instanceof FocusNode.Binder)) {
                                rtn.addAll(buildBranch2(branchNode, child));
                            } else if (child.getVisibility() == 0 && ((FocusNode.Binder) child).getNode().isNodeFocusable()) {
                                Rect tmpRect = new Rect();
                                child.getDrawingRect(tmpRect);
                                ((ViewGroup) attachedView).offsetDescendantRectToMyCoords(child, tmpRect);
                                ((FocusNode.Binder) child).getNode().setRectInParentNode(tmpRect);
                                ((FocusNode.Binder) child).getNode().setParentNode(((FocusNode.Binder) attachedView).getNode());
                                rtn.add(new Pair(((FocusNode.Binder) child).getNode(), tmpRect));
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return rtn;
    }

    public static void rebuildTotalPath(FocusNode branchNode) {
        if (branchNode != null) {
            branchNode.rebuildChildren();
            for (Pair<FocusNode, Rect> iterator : branchNode.getFocusChildren()) {
                if (!(iterator == null || iterator.first == null)) {
                    rebuildTotalPath((FocusNode) iterator.first);
                }
            }
        }
    }
}
