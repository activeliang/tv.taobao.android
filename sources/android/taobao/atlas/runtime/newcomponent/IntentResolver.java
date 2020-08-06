package android.taobao.atlas.runtime.newcomponent;

import android.content.Intent;
import android.content.IntentFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class IntentResolver<F extends IntentFilter, R> {
    private static final boolean DEBUG = false;
    private static final String TAG = "IntentResolver";
    private static final boolean localLOGV = false;
    private static final Comparator mResolvePrioritySorter = new Comparator() {
        public int compare(Object o1, Object o2) {
            int q1 = ((IntentFilter) o1).getPriority();
            int q2 = ((IntentFilter) o2).getPriority();
            if (q1 > q2) {
                return -1;
            }
            return q1 < q2 ? 1 : 0;
        }
    };
    private final HashMap<String, F[]> mActionToFilter = new HashMap<>();
    private final HashMap<String, F[]> mBaseTypeToFilter = new HashMap<>();
    private final HashSet<F> mFilters = new HashSet<>();
    private final HashMap<String, F[]> mSchemeToFilter = new HashMap<>();
    private final HashMap<String, F[]> mTypeToFilter = new HashMap<>();
    private final HashMap<String, F[]> mTypedActionToFilter = new HashMap<>();
    private final HashMap<String, F[]> mWildTypeToFilter = new HashMap<>();

    /* access modifiers changed from: protected */
    public abstract boolean isPackageForFilter(String str, F f);

    /* access modifiers changed from: protected */
    public abstract F[] newArray(int i);

    public void addFilter(F f) {
        this.mFilters.add(f);
        int numS = register_intent_filter(f, f.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = register_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            register_intent_filter(f, f.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            register_intent_filter(f, f.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    public void removeFilter(F f) {
        removeFilterInternal(f);
        this.mFilters.remove(f);
    }

    /* access modifiers changed from: package-private */
    public void removeFilterInternal(F f) {
        int numS = unregister_intent_filter(f, f.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = unregister_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            unregister_intent_filter(f, f.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            unregister_intent_filter(f, f.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    private class IteratorWrapper implements Iterator<F> {
        private F mCur;
        private final Iterator<F> mI;

        IteratorWrapper(Iterator<F> it) {
            this.mI = it;
        }

        public boolean hasNext() {
            return this.mI.hasNext();
        }

        public F next() {
            F f = (IntentFilter) this.mI.next();
            this.mCur = f;
            return f;
        }

        public void remove() {
            if (this.mCur != null) {
                IntentResolver.this.removeFilterInternal(this.mCur);
            }
            this.mI.remove();
        }
    }

    public Iterator<F> filterIterator() {
        return new IteratorWrapper(this.mFilters.iterator());
    }

    public Set<F> filterSet() {
        return Collections.unmodifiableSet(this.mFilters);
    }

    public List<R> queryIntentFromList(Intent intent, String resolvedType, boolean defaultOnly, ArrayList<F[]> listCut) {
        ArrayList<R> resultList = new ArrayList<>();
        boolean debug = (intent.getFlags() & 8) != 0;
        FastImmutableArraySet<String> categories = getFastIntentCategories(intent);
        String scheme = intent.getScheme();
        int N = listCut.size();
        for (int i = 0; i < N; i++) {
            buildResolveList(intent, categories, debug, defaultOnly, resolvedType, scheme, (IntentFilter[]) listCut.get(i), resultList);
        }
        sortResults(resultList);
        return resultList;
    }

    public List<R> queryIntent(Intent intent, String resolvedType, boolean defaultOnly) {
        int slashpos;
        String scheme = intent.getScheme();
        ArrayList<R> finalList = new ArrayList<>();
        boolean debug = (intent.getFlags() & 8) != 0;
        F[] firstTypeCut = null;
        F[] secondTypeCut = null;
        F[] thirdTypeCut = null;
        F[] schemeCut = null;
        if (resolvedType != null && (slashpos = resolvedType.indexOf(47)) > 0) {
            String baseType = resolvedType.substring(0, slashpos);
            if (!baseType.equals("*")) {
                if (resolvedType.length() == slashpos + 2) {
                    if (resolvedType.charAt(slashpos + 1) == '*') {
                        firstTypeCut = (IntentFilter[]) this.mBaseTypeToFilter.get(baseType);
                        secondTypeCut = (IntentFilter[]) this.mWildTypeToFilter.get(baseType);
                        thirdTypeCut = (IntentFilter[]) this.mWildTypeToFilter.get("*");
                    }
                }
                firstTypeCut = (IntentFilter[]) this.mTypeToFilter.get(resolvedType);
                secondTypeCut = (IntentFilter[]) this.mWildTypeToFilter.get(baseType);
                thirdTypeCut = (IntentFilter[]) this.mWildTypeToFilter.get("*");
            } else if (intent.getAction() != null) {
                firstTypeCut = (IntentFilter[]) this.mTypedActionToFilter.get(intent.getAction());
            }
        }
        if (scheme != null) {
            schemeCut = (IntentFilter[]) this.mSchemeToFilter.get(scheme);
        }
        if (resolvedType == null && scheme == null && intent.getAction() != null) {
            firstTypeCut = (IntentFilter[]) this.mActionToFilter.get(intent.getAction());
        }
        FastImmutableArraySet<String> categories = getFastIntentCategories(intent);
        if (firstTypeCut != null) {
            buildResolveList(intent, categories, debug, defaultOnly, resolvedType, scheme, firstTypeCut, finalList);
        }
        if (secondTypeCut != null) {
            buildResolveList(intent, categories, debug, defaultOnly, resolvedType, scheme, secondTypeCut, finalList);
        }
        if (thirdTypeCut != null) {
            buildResolveList(intent, categories, debug, defaultOnly, resolvedType, scheme, thirdTypeCut, finalList);
        }
        if (schemeCut != null) {
            buildResolveList(intent, categories, debug, defaultOnly, resolvedType, scheme, schemeCut, finalList);
        }
        sortResults(finalList);
        return finalList;
    }

    /* access modifiers changed from: protected */
    public boolean allowFilterResult(F f, List<R> list) {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isFilterStopped(F f) {
        return false;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [R, F] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public R newResult(F r1, int r2) {
        /*
            r0 = this;
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.newcomponent.IntentResolver.newResult(android.content.IntentFilter, int):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public void sortResults(List<R> results) {
        Collections.sort(results, mResolvePrioritySorter);
    }

    /* access modifiers changed from: protected */
    public void dumpFilter(PrintWriter out, String prefix, F filter) {
        out.print(prefix);
        out.println(filter);
    }

    private final void addFilter(HashMap<String, F[]> map, String name, F filter) {
        F[] array = (IntentFilter[]) map.get(name);
        if (array == null) {
            F[] array2 = newArray(2);
            map.put(name, array2);
            array2[0] = filter;
            return;
        }
        int N = array.length;
        int i = N;
        while (i > 0 && array[i - 1] == null) {
            i--;
        }
        if (i < N) {
            array[i] = filter;
            return;
        }
        F[] newa = newArray((N * 3) / 2);
        System.arraycopy(array, 0, newa, 0, N);
        newa[N] = filter;
        map.put(name, newa);
    }

    private final int register_mime_types(F filter, String prefix) {
        Iterator<String> i = filter.typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = i.next();
            num++;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                name = name + "/*";
            }
            addFilter(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                addFilter(this.mBaseTypeToFilter, baseName, filter);
            } else {
                addFilter(this.mWildTypeToFilter, baseName, filter);
            }
        }
        return num;
    }

    private final int unregister_mime_types(F filter, String prefix) {
        Iterator<String> i = filter.typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = i.next();
            num++;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                name = name + "/*";
            }
            remove_all_objects(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                remove_all_objects(this.mBaseTypeToFilter, baseName, filter);
            } else {
                remove_all_objects(this.mWildTypeToFilter, baseName, filter);
            }
        }
        return num;
    }

    private final int register_intent_filter(F filter, Iterator<String> i, HashMap<String, F[]> dest, String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            num++;
            addFilter(dest, i.next(), filter);
        }
        return num;
    }

    private final int unregister_intent_filter(F filter, Iterator<String> i, HashMap<String, F[]> dest, String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            num++;
            remove_all_objects(dest, i.next(), filter);
        }
        return num;
    }

    private final void remove_all_objects(HashMap<String, F[]> map, String name, Object object) {
        F[] array = (IntentFilter[]) map.get(name);
        if (array != null) {
            int LAST = array.length - 1;
            while (LAST >= 0 && array[LAST] == null) {
                LAST--;
            }
            for (int idx = LAST; idx >= 0; idx--) {
                if (array[idx] == object) {
                    int remain = LAST - idx;
                    if (remain > 0) {
                        System.arraycopy(array, idx + 1, array, idx, remain);
                    }
                    array[LAST] = null;
                    LAST--;
                }
            }
            if (LAST < 0) {
                map.remove(name);
            } else if (LAST < array.length / 2) {
                F[] newa = newArray(LAST + 2);
                System.arraycopy(array, 0, newa, 0, LAST + 1);
                map.put(name, newa);
            }
        }
    }

    private static FastImmutableArraySet<String> getFastIntentCategories(Intent intent) {
        Set<String> categories = intent.getCategories();
        if (categories == null) {
            return null;
        }
        return new FastImmutableArraySet<>(categories.toArray(new String[categories.size()]));
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void buildResolveList(android.content.Intent r20, android.taobao.atlas.runtime.newcomponent.FastImmutableArraySet<java.lang.String> r21, boolean r22, boolean r23, java.lang.String r24, java.lang.String r25, F[] r26, java.util.List<R> r27) {
        /*
            r19 = this;
            java.lang.String r3 = r20.getAction()
            android.net.Uri r6 = r20.getData()
            java.lang.String r17 = r20.getPackage()
            int r4 = r20.getFlags()
            r4 = r4 & 48
            r5 = 16
            if (r4 != r5) goto L_0x005a
            r10 = 1
        L_0x0017:
            r14 = 0
            r13 = 0
            if (r26 == 0) goto L_0x005c
            r0 = r26
            int r9 = r0.length
        L_0x001e:
            r11 = 0
            r12 = 0
        L_0x0020:
            if (r12 >= r9) goto L_0x013c
            r2 = r26[r12]
            if (r2 == 0) goto L_0x013c
            if (r22 == 0) goto L_0x0042
            java.lang.String r4 = "IntentResolver"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "Matching against filter "
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.StringBuilder r5 = r5.append(r2)
            java.lang.String r5 = r5.toString()
            android.util.Log.v(r4, r5)
        L_0x0042:
            if (r10 == 0) goto L_0x005e
            r0 = r19
            boolean r4 = r0.isFilterStopped(r2)
            if (r4 == 0) goto L_0x005e
            if (r22 == 0) goto L_0x0057
            java.lang.String r4 = "IntentResolver"
            java.lang.String r5 = "  Filter's target is stopped; skipping"
            android.util.Log.v(r4, r5)
        L_0x0057:
            int r12 = r12 + 1
            goto L_0x0020
        L_0x005a:
            r10 = 0
            goto L_0x0017
        L_0x005c:
            r9 = 0
            goto L_0x001e
        L_0x005e:
            if (r17 == 0) goto L_0x0090
            r0 = r19
            r1 = r17
            boolean r4 = r0.isPackageForFilter(r1, r2)
            if (r4 != 0) goto L_0x0090
            if (r22 == 0) goto L_0x0057
            java.lang.String r4 = "IntentResolver"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "  Filter is not from package "
            java.lang.StringBuilder r5 = r5.append(r7)
            r0 = r17
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r7 = "; skipping"
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r5 = r5.toString()
            android.util.Log.v(r4, r5)
            goto L_0x0057
        L_0x0090:
            r0 = r19
            r1 = r27
            boolean r4 = r0.allowFilterResult(r2, r1)
            if (r4 != 0) goto L_0x00a6
            if (r22 == 0) goto L_0x0057
            java.lang.String r4 = "IntentResolver"
            java.lang.String r5 = "  Filter's target already added"
            android.util.Log.v(r4, r5)
            goto L_0x0057
        L_0x00a6:
            java.lang.String r8 = "IntentResolver"
            r4 = r24
            r5 = r25
            r7 = r21
            int r15 = r2.match(r3, r4, r5, r6, r7, r8)
            if (r15 < 0) goto L_0x0106
            if (r22 == 0) goto L_0x00e7
            java.lang.String r4 = "IntentResolver"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "  Filter matched!  match=0x"
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r7 = java.lang.Integer.toHexString(r15)
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r7 = " hasDefault="
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r7 = "android.intent.category.DEFAULT"
            boolean r7 = r2.hasCategory(r7)
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r5 = r5.toString()
            android.util.Log.v(r4, r5)
        L_0x00e7:
            if (r23 == 0) goto L_0x00f2
            java.lang.String r4 = "android.intent.category.DEFAULT"
            boolean r4 = r2.hasCategory(r4)
            if (r4 == 0) goto L_0x0103
        L_0x00f2:
            r0 = r19
            java.lang.Object r16 = r0.newResult(r2, r15)
            if (r16 == 0) goto L_0x0057
            r0 = r27
            r1 = r16
            r0.add(r1)
            goto L_0x0057
        L_0x0103:
            r11 = 1
            goto L_0x0057
        L_0x0106:
            if (r22 == 0) goto L_0x0057
            switch(r15) {
                case -4: goto L_0x0130;
                case -3: goto L_0x012c;
                case -2: goto L_0x0134;
                case -1: goto L_0x0138;
                default: goto L_0x010b;
            }
        L_0x010b:
            java.lang.String r18 = "unknown reason"
        L_0x010e:
            java.lang.String r4 = "IntentResolver"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "  Filter did not match: "
            java.lang.StringBuilder r5 = r5.append(r7)
            r0 = r18
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r5 = r5.toString()
            android.util.Log.v(r4, r5)
            goto L_0x0057
        L_0x012c:
            java.lang.String r18 = "action"
            goto L_0x010e
        L_0x0130:
            java.lang.String r18 = "category"
            goto L_0x010e
        L_0x0134:
            java.lang.String r18 = "data"
            goto L_0x010e
        L_0x0138:
            java.lang.String r18 = "type"
            goto L_0x010e
        L_0x013c:
            if (r11 == 0) goto L_0x014d
            int r4 = r27.size()
            if (r4 != 0) goto L_0x014e
            java.lang.String r4 = "IntentResolver"
            java.lang.String r5 = "resolveIntent failed: found match, but none with CATEGORY_DEFAULT"
            android.util.Log.w(r4, r5)
        L_0x014d:
            return
        L_0x014e:
            int r4 = r27.size()
            r5 = 1
            if (r4 <= r5) goto L_0x014d
            java.lang.String r4 = "IntentResolver"
            java.lang.String r5 = "resolveIntent: multiple matches, only some with CATEGORY_DEFAULT"
            android.util.Log.w(r4, r5)
            goto L_0x014d
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.newcomponent.IntentResolver.buildResolveList(android.content.Intent, android.taobao.atlas.runtime.newcomponent.FastImmutableArraySet, boolean, boolean, java.lang.String, java.lang.String, android.content.IntentFilter[], java.util.List):void");
    }
}
