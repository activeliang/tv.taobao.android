package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.BackStackRecord;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
    private static final int[] INVERSE_OPS = {0, 3, 0, 1, 5, 4, 7, 6};

    FragmentTransition() {
    }

    static void startTransitions(FragmentManagerImpl fragmentManager, ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, boolean isOptimized) {
        if (fragmentManager.mCurState >= 1 && Build.VERSION.SDK_INT >= 21) {
            SparseArray<FragmentContainerTransition> transitioningFragments = new SparseArray<>();
            for (int i = startIndex; i < endIndex; i++) {
                BackStackRecord record = records.get(i);
                if (isRecordPop.get(i).booleanValue()) {
                    calculatePopFragments(record, transitioningFragments, isOptimized);
                } else {
                    calculateFragments(record, transitioningFragments, isOptimized);
                }
            }
            if (transitioningFragments.size() != 0) {
                View nonExistentView = new View(fragmentManager.mHost.getContext());
                int numContainers = transitioningFragments.size();
                for (int i2 = 0; i2 < numContainers; i2++) {
                    int containerId = transitioningFragments.keyAt(i2);
                    ArrayMap<String, String> nameOverrides = calculateNameOverrides(containerId, records, isRecordPop, startIndex, endIndex);
                    FragmentContainerTransition containerTransition = transitioningFragments.valueAt(i2);
                    if (isOptimized) {
                        configureTransitionsOptimized(fragmentManager, containerId, containerTransition, nonExistentView, nameOverrides);
                    } else {
                        configureTransitionsUnoptimized(fragmentManager, containerId, containerTransition, nonExistentView, nameOverrides);
                    }
                }
            }
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int containerId, ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        ArrayList<String> sources;
        ArrayList<String> targets;
        ArrayMap<String, String> nameOverrides = new ArrayMap<>();
        for (int recordNum = endIndex - 1; recordNum >= startIndex; recordNum--) {
            BackStackRecord record = records.get(recordNum);
            if (record.interactsWith(containerId)) {
                boolean isPop = isRecordPop.get(recordNum).booleanValue();
                if (record.mSharedElementSourceNames != null) {
                    int numSharedElements = record.mSharedElementSourceNames.size();
                    if (isPop) {
                        targets = record.mSharedElementSourceNames;
                        sources = record.mSharedElementTargetNames;
                    } else {
                        sources = record.mSharedElementSourceNames;
                        targets = record.mSharedElementTargetNames;
                    }
                    for (int i = 0; i < numSharedElements; i++) {
                        String sourceName = sources.get(i);
                        String targetName = targets.get(i);
                        String previousTarget = nameOverrides.remove(targetName);
                        if (previousTarget != null) {
                            nameOverrides.put(sourceName, previousTarget);
                        } else {
                            nameOverrides.put(sourceName, targetName);
                        }
                    }
                }
            }
        }
        return nameOverrides;
    }

    private static void configureTransitionsOptimized(FragmentManagerImpl fragmentManager, int containerId, FragmentContainerTransition fragments, View nonExistentView, ArrayMap<String, String> nameOverrides) {
        ViewGroup sceneRoot = null;
        if (fragmentManager.mContainer.onHasView()) {
            sceneRoot = (ViewGroup) fragmentManager.mContainer.onFindViewById(containerId);
        }
        if (sceneRoot != null) {
            Fragment inFragment = fragments.lastIn;
            Fragment outFragment = fragments.firstOut;
            boolean inIsPop = fragments.lastInIsPop;
            ArrayList<View> sharedElementsIn = new ArrayList<>();
            ArrayList<View> sharedElementsOut = new ArrayList<>();
            Object enterTransition = getEnterTransition(inFragment, inIsPop);
            Object exitTransition = getExitTransition(outFragment, fragments.firstOutIsPop);
            Object sharedElementTransition = configureSharedElementsOptimized(sceneRoot, nonExistentView, nameOverrides, fragments, sharedElementsOut, sharedElementsIn, enterTransition, exitTransition);
            if (enterTransition != null || sharedElementTransition != null || exitTransition != null) {
                ArrayList<View> exitingViews = configureEnteringExitingViews(exitTransition, outFragment, sharedElementsOut, nonExistentView);
                ArrayList<View> enteringViews = configureEnteringExitingViews(enterTransition, inFragment, sharedElementsIn, nonExistentView);
                setViewVisibility(enteringViews, 4);
                Object transition = mergeTransitions(enterTransition, exitTransition, sharedElementTransition, inFragment, inIsPop);
                if (transition != null) {
                    replaceHide(exitTransition, outFragment, exitingViews);
                    ArrayList<String> inNames = FragmentTransitionCompat21.prepareSetNameOverridesOptimized(sharedElementsIn);
                    FragmentTransitionCompat21.scheduleRemoveTargets(transition, enterTransition, enteringViews, exitTransition, exitingViews, sharedElementTransition, sharedElementsIn);
                    FragmentTransitionCompat21.beginDelayedTransition(sceneRoot, transition);
                    FragmentTransitionCompat21.setNameOverridesOptimized(sceneRoot, sharedElementsOut, sharedElementsIn, inNames, nameOverrides);
                    setViewVisibility(enteringViews, 0);
                    FragmentTransitionCompat21.swapSharedElementTargets(sharedElementTransition, sharedElementsOut, sharedElementsIn);
                }
            }
        }
    }

    private static void replaceHide(Object exitTransition, Fragment exitingFragment, final ArrayList<View> exitingViews) {
        if (exitingFragment != null && exitTransition != null && exitingFragment.mAdded && exitingFragment.mHidden && exitingFragment.mHiddenChanged) {
            exitingFragment.setHideReplaced(true);
            FragmentTransitionCompat21.scheduleHideFragmentView(exitTransition, exitingFragment.getView(), exitingViews);
            OneShotPreDrawListener.add(exitingFragment.mContainer, new Runnable() {
                public void run() {
                    FragmentTransition.setViewVisibility(exitingViews, 4);
                }
            });
        }
    }

    private static void configureTransitionsUnoptimized(FragmentManagerImpl fragmentManager, int containerId, FragmentContainerTransition fragments, View nonExistentView, ArrayMap<String, String> nameOverrides) {
        ViewGroup sceneRoot = null;
        if (fragmentManager.mContainer.onHasView()) {
            sceneRoot = (ViewGroup) fragmentManager.mContainer.onFindViewById(containerId);
        }
        if (sceneRoot != null) {
            Fragment inFragment = fragments.lastIn;
            Fragment outFragment = fragments.firstOut;
            boolean inIsPop = fragments.lastInIsPop;
            boolean outIsPop = fragments.firstOutIsPop;
            Object enterTransition = getEnterTransition(inFragment, inIsPop);
            Object exitTransition = getExitTransition(outFragment, outIsPop);
            ArrayList<View> sharedElementsOut = new ArrayList<>();
            ArrayList<View> sharedElementsIn = new ArrayList<>();
            Object sharedElementTransition = configureSharedElementsUnoptimized(sceneRoot, nonExistentView, nameOverrides, fragments, sharedElementsOut, sharedElementsIn, enterTransition, exitTransition);
            if (enterTransition != null || sharedElementTransition != null || exitTransition != null) {
                ArrayList<View> exitingViews = configureEnteringExitingViews(exitTransition, outFragment, sharedElementsOut, nonExistentView);
                if (exitingViews == null || exitingViews.isEmpty()) {
                    exitTransition = null;
                }
                FragmentTransitionCompat21.addTarget(enterTransition, nonExistentView);
                Object transition = mergeTransitions(enterTransition, exitTransition, sharedElementTransition, inFragment, fragments.lastInIsPop);
                if (transition != null) {
                    ArrayList<View> enteringViews = new ArrayList<>();
                    FragmentTransitionCompat21.scheduleRemoveTargets(transition, enterTransition, enteringViews, exitTransition, exitingViews, sharedElementTransition, sharedElementsIn);
                    scheduleTargetChange(sceneRoot, inFragment, nonExistentView, sharedElementsIn, enterTransition, enteringViews, exitTransition, exitingViews);
                    FragmentTransitionCompat21.setNameOverridesUnoptimized(sceneRoot, sharedElementsIn, nameOverrides);
                    FragmentTransitionCompat21.beginDelayedTransition(sceneRoot, transition);
                    FragmentTransitionCompat21.scheduleNameReset(sceneRoot, sharedElementsIn, nameOverrides);
                }
            }
        }
    }

    private static void scheduleTargetChange(ViewGroup sceneRoot, Fragment inFragment, View nonExistentView, ArrayList<View> sharedElementsIn, Object enterTransition, ArrayList<View> enteringViews, Object exitTransition, ArrayList<View> exitingViews) {
        final Object obj = enterTransition;
        final View view = nonExistentView;
        final Fragment fragment = inFragment;
        final ArrayList<View> arrayList = sharedElementsIn;
        final ArrayList<View> arrayList2 = enteringViews;
        final ArrayList<View> arrayList3 = exitingViews;
        final Object obj2 = exitTransition;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                if (obj != null) {
                    FragmentTransitionCompat21.removeTarget(obj, view);
                    arrayList2.addAll(FragmentTransition.configureEnteringExitingViews(obj, fragment, arrayList, view));
                }
                if (arrayList3 != null) {
                    if (obj2 != null) {
                        ArrayList<View> tempExiting = new ArrayList<>();
                        tempExiting.add(view);
                        FragmentTransitionCompat21.replaceTargets(obj2, arrayList3, tempExiting);
                    }
                    arrayList3.clear();
                    arrayList3.add(view);
                }
            }
        });
    }

    private static Object getSharedElementTransition(Fragment inFragment, Fragment outFragment, boolean isPop) {
        Object sharedElementEnterTransition;
        if (inFragment == null || outFragment == null) {
            return null;
        }
        if (isPop) {
            sharedElementEnterTransition = outFragment.getSharedElementReturnTransition();
        } else {
            sharedElementEnterTransition = inFragment.getSharedElementEnterTransition();
        }
        return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(sharedElementEnterTransition));
    }

    private static Object getEnterTransition(Fragment inFragment, boolean isPop) {
        Object enterTransition;
        if (inFragment == null) {
            return null;
        }
        if (isPop) {
            enterTransition = inFragment.getReenterTransition();
        } else {
            enterTransition = inFragment.getEnterTransition();
        }
        return FragmentTransitionCompat21.cloneTransition(enterTransition);
    }

    private static Object getExitTransition(Fragment outFragment, boolean isPop) {
        Object exitTransition;
        if (outFragment == null) {
            return null;
        }
        if (isPop) {
            exitTransition = outFragment.getReturnTransition();
        } else {
            exitTransition = outFragment.getExitTransition();
        }
        return FragmentTransitionCompat21.cloneTransition(exitTransition);
    }

    private static Object configureSharedElementsOptimized(ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> nameOverrides, FragmentContainerTransition fragments, ArrayList<View> sharedElementsOut, ArrayList<View> sharedElementsIn, Object enterTransition, Object exitTransition) {
        Object sharedElementTransition;
        final Rect epicenter;
        final View epicenterView;
        final Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        if (inFragment != null) {
            inFragment.getView().setVisibility(0);
        }
        if (inFragment == null || outFragment == null) {
            return null;
        }
        final boolean inIsPop = fragments.lastInIsPop;
        if (nameOverrides.isEmpty()) {
            sharedElementTransition = null;
        } else {
            sharedElementTransition = getSharedElementTransition(inFragment, outFragment, inIsPop);
        }
        ArrayMap<String, View> outSharedElements = captureOutSharedElements(nameOverrides, sharedElementTransition, fragments);
        final ArrayMap<String, View> inSharedElements = captureInSharedElements(nameOverrides, sharedElementTransition, fragments);
        if (nameOverrides.isEmpty()) {
            sharedElementTransition = null;
            if (outSharedElements != null) {
                outSharedElements.clear();
            }
            if (inSharedElements != null) {
                inSharedElements.clear();
            }
        } else {
            addSharedElementsWithMatchingNames(sharedElementsOut, outSharedElements, nameOverrides.keySet());
            addSharedElementsWithMatchingNames(sharedElementsIn, inSharedElements, nameOverrides.values());
        }
        if (enterTransition == null && exitTransition == null && sharedElementTransition == null) {
            return null;
        }
        callSharedElementStartEnd(inFragment, outFragment, inIsPop, outSharedElements, true);
        if (sharedElementTransition != null) {
            sharedElementsIn.add(nonExistentView);
            FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, nonExistentView, sharedElementsOut);
            setOutEpicenter(sharedElementTransition, exitTransition, outSharedElements, fragments.firstOutIsPop, fragments.firstOutTransaction);
            epicenter = new Rect();
            epicenterView = getInEpicenterView(inSharedElements, fragments, enterTransition, inIsPop);
            if (epicenterView != null) {
                FragmentTransitionCompat21.setEpicenter(enterTransition, epicenter);
            }
        } else {
            epicenter = null;
            epicenterView = null;
        }
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
                if (epicenterView != null) {
                    FragmentTransitionCompat21.getBoundsOnScreen(epicenterView, epicenter);
                }
            }
        });
        return sharedElementTransition;
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> views, ArrayMap<String, View> sharedElements, Collection<String> nameOverridesSet) {
        for (int i = sharedElements.size() - 1; i >= 0; i--) {
            View view = sharedElements.valueAt(i);
            if (nameOverridesSet.contains(ViewCompat.getTransitionName(view))) {
                views.add(view);
            }
        }
    }

    private static Object configureSharedElementsUnoptimized(ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> nameOverrides, FragmentContainerTransition fragments, ArrayList<View> sharedElementsOut, ArrayList<View> sharedElementsIn, Object enterTransition, Object exitTransition) {
        Object sharedElementTransition;
        final Object sharedElementTransition2;
        final Rect inEpicenter;
        final Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        if (inFragment == null || outFragment == null) {
            return null;
        }
        final boolean inIsPop = fragments.lastInIsPop;
        if (nameOverrides.isEmpty()) {
            sharedElementTransition = null;
        } else {
            sharedElementTransition = getSharedElementTransition(inFragment, outFragment, inIsPop);
        }
        ArrayMap<String, View> outSharedElements = captureOutSharedElements(nameOverrides, sharedElementTransition, fragments);
        if (nameOverrides.isEmpty()) {
            sharedElementTransition2 = null;
        } else {
            sharedElementsOut.addAll(outSharedElements.values());
            sharedElementTransition2 = sharedElementTransition;
        }
        if (enterTransition == null && exitTransition == null && sharedElementTransition2 == null) {
            return null;
        }
        callSharedElementStartEnd(inFragment, outFragment, inIsPop, outSharedElements, true);
        if (sharedElementTransition2 != null) {
            inEpicenter = new Rect();
            FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition2, nonExistentView, sharedElementsOut);
            setOutEpicenter(sharedElementTransition2, exitTransition, outSharedElements, fragments.firstOutIsPop, fragments.firstOutTransaction);
            if (enterTransition != null) {
                FragmentTransitionCompat21.setEpicenter(enterTransition, inEpicenter);
            }
        } else {
            inEpicenter = null;
        }
        final ArrayMap<String, String> arrayMap = nameOverrides;
        final FragmentContainerTransition fragmentContainerTransition = fragments;
        final ArrayList<View> arrayList = sharedElementsIn;
        final View view = nonExistentView;
        final ArrayList<View> arrayList2 = sharedElementsOut;
        final Object obj = enterTransition;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                ArrayMap<String, View> inSharedElements = FragmentTransition.captureInSharedElements(arrayMap, sharedElementTransition2, fragmentContainerTransition);
                if (inSharedElements != null) {
                    arrayList.addAll(inSharedElements.values());
                    arrayList.add(view);
                }
                FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
                if (sharedElementTransition2 != null) {
                    FragmentTransitionCompat21.swapSharedElementTargets(sharedElementTransition2, arrayList2, arrayList);
                    View inEpicenterView = FragmentTransition.getInEpicenterView(inSharedElements, fragmentContainerTransition, obj, inIsPop);
                    if (inEpicenterView != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen(inEpicenterView, inEpicenter);
                    }
                }
            }
        });
        return sharedElementTransition2;
    }

    private static ArrayMap<String, View> captureOutSharedElements(ArrayMap<String, String> nameOverrides, Object sharedElementTransition, FragmentContainerTransition fragments) {
        SharedElementCallback sharedElementCallback;
        ArrayList<String> names;
        if (nameOverrides.isEmpty() || sharedElementTransition == null) {
            nameOverrides.clear();
            return null;
        }
        Fragment outFragment = fragments.firstOut;
        ArrayMap<String, View> outSharedElements = new ArrayMap<>();
        FragmentTransitionCompat21.findNamedViews(outSharedElements, outFragment.getView());
        BackStackRecord outTransaction = fragments.firstOutTransaction;
        if (fragments.firstOutIsPop) {
            sharedElementCallback = outFragment.getEnterTransitionCallback();
            names = outTransaction.mSharedElementTargetNames;
        } else {
            sharedElementCallback = outFragment.getExitTransitionCallback();
            names = outTransaction.mSharedElementSourceNames;
        }
        outSharedElements.retainAll(names);
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(names, outSharedElements);
            for (int i = names.size() - 1; i >= 0; i--) {
                String name = names.get(i);
                View view = outSharedElements.get(name);
                if (view == null) {
                    nameOverrides.remove(name);
                } else if (!name.equals(ViewCompat.getTransitionName(view))) {
                    nameOverrides.put(ViewCompat.getTransitionName(view), nameOverrides.remove(name));
                }
            }
            return outSharedElements;
        }
        nameOverrides.retainAll(outSharedElements.keySet());
        return outSharedElements;
    }

    /* access modifiers changed from: private */
    public static ArrayMap<String, View> captureInSharedElements(ArrayMap<String, String> nameOverrides, Object sharedElementTransition, FragmentContainerTransition fragments) {
        SharedElementCallback sharedElementCallback;
        ArrayList<String> names;
        String key;
        Fragment inFragment = fragments.lastIn;
        View fragmentView = inFragment.getView();
        if (nameOverrides.isEmpty() || sharedElementTransition == null || fragmentView == null) {
            nameOverrides.clear();
            return null;
        }
        ArrayMap<String, View> inSharedElements = new ArrayMap<>();
        FragmentTransitionCompat21.findNamedViews(inSharedElements, fragmentView);
        BackStackRecord inTransaction = fragments.lastInTransaction;
        if (fragments.lastInIsPop) {
            sharedElementCallback = inFragment.getExitTransitionCallback();
            names = inTransaction.mSharedElementSourceNames;
        } else {
            sharedElementCallback = inFragment.getEnterTransitionCallback();
            names = inTransaction.mSharedElementTargetNames;
        }
        if (names != null) {
            inSharedElements.retainAll(names);
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(names, inSharedElements);
            for (int i = names.size() - 1; i >= 0; i--) {
                String name = names.get(i);
                View view = inSharedElements.get(name);
                if (view == null) {
                    String key2 = findKeyForValue(nameOverrides, name);
                    if (key2 != null) {
                        nameOverrides.remove(key2);
                    }
                } else if (!name.equals(ViewCompat.getTransitionName(view)) && (key = findKeyForValue(nameOverrides, name)) != null) {
                    nameOverrides.put(key, ViewCompat.getTransitionName(view));
                }
            }
            return inSharedElements;
        }
        retainValues(nameOverrides, inSharedElements);
        return inSharedElements;
    }

    private static String findKeyForValue(ArrayMap<String, String> map, String value) {
        int numElements = map.size();
        for (int i = 0; i < numElements; i++) {
            if (value.equals(map.valueAt(i))) {
                return map.keyAt(i);
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static View getInEpicenterView(ArrayMap<String, View> inSharedElements, FragmentContainerTransition fragments, Object enterTransition, boolean inIsPop) {
        String targetName;
        BackStackRecord inTransaction = fragments.lastInTransaction;
        if (enterTransition == null || inSharedElements == null || inTransaction.mSharedElementSourceNames == null || inTransaction.mSharedElementSourceNames.isEmpty()) {
            return null;
        }
        if (inIsPop) {
            targetName = inTransaction.mSharedElementSourceNames.get(0);
        } else {
            targetName = inTransaction.mSharedElementTargetNames.get(0);
        }
        return inSharedElements.get(targetName);
    }

    private static void setOutEpicenter(Object sharedElementTransition, Object exitTransition, ArrayMap<String, View> outSharedElements, boolean outIsPop, BackStackRecord outTransaction) {
        String sourceName;
        if (outTransaction.mSharedElementSourceNames != null && !outTransaction.mSharedElementSourceNames.isEmpty()) {
            if (outIsPop) {
                sourceName = outTransaction.mSharedElementTargetNames.get(0);
            } else {
                sourceName = outTransaction.mSharedElementSourceNames.get(0);
            }
            View outEpicenterView = outSharedElements.get(sourceName);
            FragmentTransitionCompat21.setEpicenter(sharedElementTransition, outEpicenterView);
            if (exitTransition != null) {
                FragmentTransitionCompat21.setEpicenter(exitTransition, outEpicenterView);
            }
        }
    }

    private static void retainValues(ArrayMap<String, String> nameOverrides, ArrayMap<String, View> namedViews) {
        for (int i = nameOverrides.size() - 1; i >= 0; i--) {
            if (!namedViews.containsKey(nameOverrides.valueAt(i))) {
                nameOverrides.removeAt(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void callSharedElementStartEnd(Fragment inFragment, Fragment outFragment, boolean isPop, ArrayMap<String, View> sharedElements, boolean isStart) {
        SharedElementCallback sharedElementCallback;
        if (isPop) {
            sharedElementCallback = outFragment.getEnterTransitionCallback();
        } else {
            sharedElementCallback = inFragment.getEnterTransitionCallback();
        }
        if (sharedElementCallback != null) {
            ArrayList<View> views = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            int count = sharedElements == null ? 0 : sharedElements.size();
            for (int i = 0; i < count; i++) {
                names.add(sharedElements.keyAt(i));
                views.add(sharedElements.valueAt(i));
            }
            if (isStart) {
                sharedElementCallback.onSharedElementStart(names, views, (List<View>) null);
            } else {
                sharedElementCallback.onSharedElementEnd(names, views, (List<View>) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public static ArrayList<View> configureEnteringExitingViews(Object transition, Fragment fragment, ArrayList<View> sharedElements, View nonExistentView) {
        ArrayList<View> viewList = null;
        if (transition != null) {
            viewList = new ArrayList<>();
            View root = fragment.getView();
            if (root != null) {
                FragmentTransitionCompat21.captureTransitioningViews(viewList, root);
            }
            if (sharedElements != null) {
                viewList.removeAll(sharedElements);
            }
            if (!viewList.isEmpty()) {
                viewList.add(nonExistentView);
                FragmentTransitionCompat21.addTargets(transition, viewList);
            }
        }
        return viewList;
    }

    /* access modifiers changed from: private */
    public static void setViewVisibility(ArrayList<View> views, int visibility) {
        if (views != null) {
            for (int i = views.size() - 1; i >= 0; i--) {
                views.get(i).setVisibility(visibility);
            }
        }
    }

    private static Object mergeTransitions(Object enterTransition, Object exitTransition, Object sharedElementTransition, Fragment inFragment, boolean isPop) {
        boolean overlap = true;
        if (!(enterTransition == null || exitTransition == null || inFragment == null)) {
            overlap = isPop ? inFragment.getAllowReturnTransitionOverlap() : inFragment.getAllowEnterTransitionOverlap();
        }
        if (overlap) {
            return FragmentTransitionCompat21.mergeTransitionsTogether(exitTransition, enterTransition, sharedElementTransition);
        }
        return FragmentTransitionCompat21.mergeTransitionsInSequence(exitTransition, enterTransition, sharedElementTransition);
    }

    public static void calculateFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isOptimized) {
        int numOps = transaction.mOps.size();
        for (int opNum = 0; opNum < numOps; opNum++) {
            addToFirstInLastOut(transaction, transaction.mOps.get(opNum), transitioningFragments, false, isOptimized);
        }
    }

    public static void calculatePopFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isOptimized) {
        if (transaction.mManager.mContainer.onHasView()) {
            for (int opNum = transaction.mOps.size() - 1; opNum >= 0; opNum--) {
                addToFirstInLastOut(transaction, transaction.mOps.get(opNum), transitioningFragments, true, isOptimized);
            }
        }
    }

    private static void addToFirstInLastOut(BackStackRecord transaction, BackStackRecord.Op op, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isPop, boolean isOptimizedTransaction) {
        boolean setFirstOut;
        boolean setLastIn;
        Fragment fragment = op.fragment;
        int containerId = fragment.mContainerId;
        if (containerId != 0) {
            boolean setLastIn2 = false;
            boolean wasRemoved = false;
            boolean setFirstOut2 = false;
            boolean wasAdded = false;
            switch (isPop ? INVERSE_OPS[op.cmd] : op.cmd) {
                case 1:
                case 7:
                    if (isOptimizedTransaction) {
                        setLastIn = fragment.mIsNewlyAdded;
                    } else {
                        setLastIn = !fragment.mAdded && !fragment.mHidden;
                    }
                    wasAdded = true;
                    break;
                case 3:
                case 6:
                    if (isOptimizedTransaction) {
                        setFirstOut = !fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f;
                    } else {
                        setFirstOut = fragment.mAdded && !fragment.mHidden;
                    }
                    wasRemoved = true;
                    break;
                case 4:
                    if (isOptimizedTransaction) {
                        setFirstOut2 = fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden;
                    } else {
                        setFirstOut2 = fragment.mAdded && !fragment.mHidden;
                    }
                    wasRemoved = true;
                    break;
                case 5:
                    if (isOptimizedTransaction) {
                        setLastIn2 = fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded;
                    } else {
                        setLastIn2 = fragment.mHidden;
                    }
                    wasAdded = true;
                    break;
            }
            FragmentContainerTransition containerTransition = transitioningFragments.get(containerId);
            if (setLastIn2) {
                containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                containerTransition.lastIn = fragment;
                containerTransition.lastInIsPop = isPop;
                containerTransition.lastInTransaction = transaction;
            }
            if (!isOptimizedTransaction && wasAdded) {
                if (containerTransition != null && containerTransition.firstOut == fragment) {
                    containerTransition.firstOut = null;
                }
                FragmentManagerImpl manager = transaction.mManager;
                if (fragment.mState < 1 && manager.mCurState >= 1 && !transaction.mAllowOptimization) {
                    manager.makeActive(fragment);
                    manager.moveToState(fragment, 1, 0, 0, false);
                }
            }
            if (setFirstOut2 && (containerTransition == null || containerTransition.firstOut == null)) {
                containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                containerTransition.firstOut = fragment;
                containerTransition.firstOutIsPop = isPop;
                containerTransition.firstOutTransaction = transaction;
            }
            if (!isOptimizedTransaction && wasRemoved && containerTransition != null && containerTransition.lastIn == fragment) {
                containerTransition.lastIn = null;
            }
        }
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition containerTransition, SparseArray<FragmentContainerTransition> transitioningFragments, int containerId) {
        if (containerTransition != null) {
            return containerTransition;
        }
        FragmentContainerTransition containerTransition2 = new FragmentContainerTransition();
        transitioningFragments.put(containerId, containerTransition2);
        return containerTransition2;
    }

    static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }
}
