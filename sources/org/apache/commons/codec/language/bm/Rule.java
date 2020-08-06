package org.apache.commons.codec.language.bm;

import com.bftv.fui.constantplugin.Constant;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.commons.codec.language.bm.Languages;

public class Rule {
    public static final String ALL = "ALL";
    public static final RPattern ALL_STRINGS_RMATCHER = new RPattern() {
        public boolean isMatch(CharSequence input) {
            return true;
        }
    };
    private static final String DOUBLE_QUOTE = "\"";
    private static final String HASH_INCLUDE = "#include";
    private static final Map<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>> RULES = new EnumMap(NameType.class);
    private final RPattern lContext;
    private final String pattern;
    private final PhonemeExpr phoneme;
    private final RPattern rContext;

    public interface PhonemeExpr {
        Iterable<Phoneme> getPhonemes();
    }

    public interface RPattern {
        boolean isMatch(CharSequence charSequence);
    }

    public static final class Phoneme implements PhonemeExpr {
        public static final Comparator<Phoneme> COMPARATOR = new Comparator<Phoneme>() {
            public int compare(Phoneme o1, Phoneme o2) {
                for (int i = 0; i < o1.phonemeText.length(); i++) {
                    if (i >= o2.phonemeText.length()) {
                        return 1;
                    }
                    int c = o1.phonemeText.charAt(i) - o2.phonemeText.charAt(i);
                    if (c != 0) {
                        return c;
                    }
                }
                if (o1.phonemeText.length() < o2.phonemeText.length()) {
                    return -1;
                }
                return 0;
            }
        };
        private final Languages.LanguageSet languages;
        /* access modifiers changed from: private */
        public final StringBuilder phonemeText;

        public Phoneme(CharSequence phonemeText2, Languages.LanguageSet languages2) {
            this.phonemeText = new StringBuilder(phonemeText2);
            this.languages = languages2;
        }

        public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight) {
            this((CharSequence) phonemeLeft.phonemeText, phonemeLeft.languages);
            this.phonemeText.append(phonemeRight.phonemeText);
        }

        public Phoneme(Phoneme phonemeLeft, Phoneme phonemeRight, Languages.LanguageSet languages2) {
            this((CharSequence) phonemeLeft.phonemeText, languages2);
            this.phonemeText.append(phonemeRight.phonemeText);
        }

        public Phoneme append(CharSequence str) {
            this.phonemeText.append(str);
            return this;
        }

        public Languages.LanguageSet getLanguages() {
            return this.languages;
        }

        public Iterable<Phoneme> getPhonemes() {
            return Collections.singleton(this);
        }

        public CharSequence getPhonemeText() {
            return this.phonemeText;
        }

        @Deprecated
        public Phoneme join(Phoneme right) {
            return new Phoneme((CharSequence) this.phonemeText.toString() + right.phonemeText.toString(), this.languages.restrictTo(right.languages));
        }

        public Phoneme mergeWithLanguage(Languages.LanguageSet lang) {
            return new Phoneme((CharSequence) this.phonemeText.toString(), this.languages.merge(lang));
        }

        public String toString() {
            return this.phonemeText.toString() + "[" + this.languages + "]";
        }
    }

    public static final class PhonemeList implements PhonemeExpr {
        private final List<Phoneme> phonemes;

        public PhonemeList(List<Phoneme> phonemes2) {
            this.phonemes = phonemes2;
        }

        public List<Phoneme> getPhonemes() {
            return this.phonemes;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0095, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0096, code lost:
        r19 = r11;
        r11 = r10;
        r10 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00f4, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00f5, code lost:
        r19 = r11;
        r11 = r10;
        r10 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x011a, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x011c, code lost:
        r10 = th;
     */
    static {
        /*
            org.apache.commons.codec.language.bm.Rule$1 r10 = new org.apache.commons.codec.language.bm.Rule$1
            r10.<init>()
            ALL_STRINGS_RMATCHER = r10
            java.util.EnumMap r10 = new java.util.EnumMap
            java.lang.Class<org.apache.commons.codec.language.bm.NameType> r11 = org.apache.commons.codec.language.bm.NameType.class
            r10.<init>(r11)
            RULES = r10
            org.apache.commons.codec.language.bm.NameType[] r13 = org.apache.commons.codec.language.bm.NameType.values()
            int r14 = r13.length
            r10 = 0
            r12 = r10
        L_0x0017:
            if (r12 >= r14) goto L_0x0119
            r8 = r13[r12]
            java.util.EnumMap r7 = new java.util.EnumMap
            java.lang.Class<org.apache.commons.codec.language.bm.RuleType> r10 = org.apache.commons.codec.language.bm.RuleType.class
            r7.<init>(r10)
            org.apache.commons.codec.language.bm.RuleType[] r15 = org.apache.commons.codec.language.bm.RuleType.values()
            int r0 = r15.length
            r16 = r0
            r10 = 0
        L_0x002a:
            r0 = r16
            if (r10 >= r0) goto L_0x010b
            r6 = r15[r10]
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            org.apache.commons.codec.language.bm.Languages r4 = org.apache.commons.codec.language.bm.Languages.getInstance((org.apache.commons.codec.language.bm.NameType) r8)
            java.util.Set r11 = r4.getLanguages()
            java.util.Iterator r17 = r11.iterator()
        L_0x0041:
            boolean r11 = r17.hasNext()
            if (r11 == 0) goto L_0x00ac
            java.lang.Object r3 = r17.next()
            java.lang.String r3 = (java.lang.String) r3
            java.util.Scanner r9 = createScanner(r8, r6, r3)     // Catch:{ IllegalStateException -> 0x0070 }
            r11 = 0
            java.lang.String r18 = createResourceName(r8, r6, r3)     // Catch:{ Throwable -> 0x0093, all -> 0x011c }
            r0 = r18
            java.util.Map r18 = parseRules(r9, r0)     // Catch:{ Throwable -> 0x0093, all -> 0x011c }
            r0 = r18
            r5.put(r3, r0)     // Catch:{ Throwable -> 0x0093, all -> 0x011c }
            if (r9 == 0) goto L_0x0041
            if (r11 == 0) goto L_0x008f
            r9.close()     // Catch:{ Throwable -> 0x0069 }
            goto L_0x0041
        L_0x0069:
            r18 = move-exception
            r0 = r18
            r11.addSuppressed(r0)     // Catch:{ IllegalStateException -> 0x0070 }
            goto L_0x0041
        L_0x0070:
            r2 = move-exception
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "Problem processing "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = createResourceName(r8, r6, r3)
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11, r2)
            throw r10
        L_0x008f:
            r9.close()     // Catch:{ IllegalStateException -> 0x0070 }
            goto L_0x0041
        L_0x0093:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0095 }
        L_0x0095:
            r11 = move-exception
            r19 = r11
            r11 = r10
            r10 = r19
        L_0x009b:
            if (r9 == 0) goto L_0x00a2
            if (r11 == 0) goto L_0x00a8
            r9.close()     // Catch:{ Throwable -> 0x00a3 }
        L_0x00a2:
            throw r10     // Catch:{ IllegalStateException -> 0x0070 }
        L_0x00a3:
            r12 = move-exception
            r11.addSuppressed(r12)     // Catch:{ IllegalStateException -> 0x0070 }
            goto L_0x00a2
        L_0x00a8:
            r9.close()     // Catch:{ IllegalStateException -> 0x0070 }
            goto L_0x00a2
        L_0x00ac:
            org.apache.commons.codec.language.bm.RuleType r11 = org.apache.commons.codec.language.bm.RuleType.RULES
            boolean r11 = r6.equals(r11)
            if (r11 != 0) goto L_0x00dc
            java.lang.String r11 = "common"
            java.util.Scanner r9 = createScanner(r8, r6, r11)
            r11 = 0
            java.lang.String r17 = "common"
            java.lang.String r18 = "common"
            r0 = r18
            java.lang.String r18 = createResourceName(r8, r6, r0)     // Catch:{ Throwable -> 0x00f2, all -> 0x011a }
            r0 = r18
            java.util.Map r18 = parseRules(r9, r0)     // Catch:{ Throwable -> 0x00f2, all -> 0x011a }
            r0 = r17
            r1 = r18
            r5.put(r0, r1)     // Catch:{ Throwable -> 0x00f2, all -> 0x011a }
            if (r9 == 0) goto L_0x00dc
            if (r11 == 0) goto L_0x00ee
            r9.close()     // Catch:{ Throwable -> 0x00e7 }
        L_0x00dc:
            java.util.Map r11 = java.util.Collections.unmodifiableMap(r5)
            r7.put(r6, r11)
            int r10 = r10 + 1
            goto L_0x002a
        L_0x00e7:
            r17 = move-exception
            r0 = r17
            r11.addSuppressed(r0)
            goto L_0x00dc
        L_0x00ee:
            r9.close()
            goto L_0x00dc
        L_0x00f2:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x00f4 }
        L_0x00f4:
            r11 = move-exception
            r19 = r11
            r11 = r10
            r10 = r19
        L_0x00fa:
            if (r9 == 0) goto L_0x0101
            if (r11 == 0) goto L_0x0107
            r9.close()     // Catch:{ Throwable -> 0x0102 }
        L_0x0101:
            throw r10
        L_0x0102:
            r12 = move-exception
            r11.addSuppressed(r12)
            goto L_0x0101
        L_0x0107:
            r9.close()
            goto L_0x0101
        L_0x010b:
            java.util.Map<org.apache.commons.codec.language.bm.NameType, java.util.Map<org.apache.commons.codec.language.bm.RuleType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.commons.codec.language.bm.Rule>>>>> r10 = RULES
            java.util.Map r11 = java.util.Collections.unmodifiableMap(r7)
            r10.put(r8, r11)
            int r10 = r12 + 1
            r12 = r10
            goto L_0x0017
        L_0x0119:
            return
        L_0x011a:
            r10 = move-exception
            goto L_0x00fa
        L_0x011c:
            r10 = move-exception
            goto L_0x009b
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.bm.Rule.<clinit>():void");
    }

    /* access modifiers changed from: private */
    public static boolean contains(CharSequence chars, char input) {
        for (int i = 0; i < chars.length(); i++) {
            if (chars.charAt(i) == input) {
                return true;
            }
        }
        return false;
    }

    private static String createResourceName(NameType nameType, RuleType rt, String lang) {
        return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", new Object[]{nameType.getName(), rt.getName(), lang});
    }

    private static Scanner createScanner(NameType nameType, RuleType rt, String lang) {
        String resName = createResourceName(nameType, rt, lang);
        InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
        if (rulesIS != null) {
            return new Scanner(rulesIS, "UTF-8");
        }
        throw new IllegalArgumentException("Unable to load resource: " + resName);
    }

    private static Scanner createScanner(String lang) {
        String resName = String.format("org/apache/commons/codec/language/bm/%s.txt", new Object[]{lang});
        InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
        if (rulesIS != null) {
            return new Scanner(rulesIS, "UTF-8");
        }
        throw new IllegalArgumentException("Unable to load resource: " + resName);
    }

    /* access modifiers changed from: private */
    public static boolean endsWith(CharSequence input, CharSequence suffix) {
        if (suffix.length() > input.length()) {
            return false;
        }
        int i = input.length() - 1;
        for (int j = suffix.length() - 1; j >= 0; j--) {
            if (input.charAt(i) != suffix.charAt(j)) {
                return false;
            }
            i--;
        }
        return true;
    }

    public static List<Rule> getInstance(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
        Map<String, List<Rule>> ruleMap = getInstanceMap(nameType, rt, langs);
        List<Rule> allRules = new ArrayList<>();
        for (List<Rule> rules : ruleMap.values()) {
            allRules.addAll(rules);
        }
        return allRules;
    }

    public static List<Rule> getInstance(NameType nameType, RuleType rt, String lang) {
        return getInstance(nameType, rt, Languages.LanguageSet.from(new HashSet(Arrays.asList(new String[]{lang}))));
    }

    public static Map<String, List<Rule>> getInstanceMap(NameType nameType, RuleType rt, Languages.LanguageSet langs) {
        if (langs.isSingleton()) {
            return getInstanceMap(nameType, rt, langs.getAny());
        }
        return getInstanceMap(nameType, rt, Languages.ANY);
    }

    public static Map<String, List<Rule>> getInstanceMap(NameType nameType, RuleType rt, String lang) {
        Map<String, List<Rule>> rules = (Map) ((Map) RULES.get(nameType).get(rt)).get(lang);
        if (rules != null) {
            return rules;
        }
        throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", new Object[]{nameType.getName(), rt.getName(), lang}));
    }

    private static Phoneme parsePhoneme(String ph) {
        int open = ph.indexOf("[");
        if (open < 0) {
            return new Phoneme((CharSequence) ph, Languages.ANY_LANGUAGE);
        }
        if (ph.endsWith("]")) {
            return new Phoneme((CharSequence) ph.substring(0, open), Languages.LanguageSet.from(new HashSet<>(Arrays.asList(ph.substring(open + 1, ph.length() - 1).split("[+]")))));
        }
        throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
    }

    private static PhonemeExpr parsePhonemeExpr(String ph) {
        if (!ph.startsWith("(")) {
            return parsePhoneme(ph);
        }
        if (!ph.endsWith(")")) {
            throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
        }
        List<Phoneme> phs = new ArrayList<>();
        String body = ph.substring(1, ph.length() - 1);
        for (String part : body.split("[|]")) {
            phs.add(parsePhoneme(part));
        }
        if (body.startsWith("|") || body.endsWith("|")) {
            phs.add(new Phoneme((CharSequence) "", Languages.ANY_LANGUAGE));
        }
        return new PhonemeList(phs);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e2, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e3, code lost:
        r25 = r10;
        r10 = r9;
        r9 = r25;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x01bc, code lost:
        r9 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Map<java.lang.String, java.util.List<org.apache.commons.codec.language.bm.Rule>> parseRules(java.util.Scanner r26, java.lang.String r27) {
        /*
            java.util.HashMap r20 = new java.util.HashMap
            r20.<init>()
            r14 = 0
            r17 = 0
        L_0x0008:
            boolean r9 = r26.hasNextLine()
            if (r9 == 0) goto L_0x01bb
            int r14 = r14 + 1
            java.lang.String r23 = r26.nextLine()
            r19 = r23
            if (r17 == 0) goto L_0x0026
            java.lang.String r9 = "*/"
            r0 = r19
            boolean r9 = r0.endsWith(r9)
            if (r9 == 0) goto L_0x0008
            r17 = 0
            goto L_0x0008
        L_0x0026:
            java.lang.String r9 = "/*"
            r0 = r19
            boolean r9 = r0.startsWith(r9)
            if (r9 == 0) goto L_0x0034
            r17 = 1
            goto L_0x0008
        L_0x0034:
            java.lang.String r9 = "//"
            r0 = r19
            int r13 = r0.indexOf(r9)
            if (r13 < 0) goto L_0x0046
            r9 = 0
            r0 = r19
            java.lang.String r19 = r0.substring(r9, r13)
        L_0x0046:
            java.lang.String r19 = r19.trim()
            int r9 = r19.length()
            if (r9 == 0) goto L_0x0008
            java.lang.String r9 = "#include"
            r0 = r19
            boolean r9 = r0.startsWith(r9)
            if (r9 == 0) goto L_0x00f9
            java.lang.String r9 = "#include"
            int r9 = r9.length()
            r0 = r19
            java.lang.String r9 = r0.substring(r9)
            java.lang.String r18 = r9.trim()
            java.lang.String r9 = " "
            r0 = r18
            boolean r9 = r0.contains(r9)
            if (r9 == 0) goto L_0x00a0
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Malformed import statement '"
            java.lang.StringBuilder r10 = r10.append(r11)
            r0 = r23
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r11 = "' in "
            java.lang.StringBuilder r10 = r10.append(r11)
            r0 = r27
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x00a0:
            java.util.Scanner r16 = createScanner(r18)
            r10 = 0
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            r9.<init>()     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            r0 = r27
            java.lang.StringBuilder r9 = r9.append(r0)     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            java.lang.String r11 = "->"
            java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            r0 = r18
            java.lang.StringBuilder r9 = r9.append(r0)     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            r0 = r16
            java.util.Map r9 = parseRules(r0, r9)     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            r0 = r20
            r0.putAll(r9)     // Catch:{ Throwable -> 0x00e0, all -> 0x01bc }
            if (r16 == 0) goto L_0x0008
            if (r10 == 0) goto L_0x00db
            r16.close()     // Catch:{ Throwable -> 0x00d5 }
            goto L_0x0008
        L_0x00d5:
            r9 = move-exception
            r10.addSuppressed(r9)
            goto L_0x0008
        L_0x00db:
            r16.close()
            goto L_0x0008
        L_0x00e0:
            r9 = move-exception
            throw r9     // Catch:{ all -> 0x00e2 }
        L_0x00e2:
            r10 = move-exception
            r25 = r10
            r10 = r9
            r9 = r25
        L_0x00e8:
            if (r16 == 0) goto L_0x00ef
            if (r10 == 0) goto L_0x00f5
            r16.close()     // Catch:{ Throwable -> 0x00f0 }
        L_0x00ef:
            throw r9
        L_0x00f0:
            r11 = move-exception
            r10.addSuppressed(r11)
            goto L_0x00ef
        L_0x00f5:
            r16.close()
            goto L_0x00ef
        L_0x00f9:
            java.lang.String r9 = "\\s+"
            r0 = r19
            java.lang.String[] r21 = r0.split(r9)
            r0 = r21
            int r9 = r0.length
            r10 = 4
            if (r9 == r10) goto L_0x013f
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Malformed rule statement split into "
            java.lang.StringBuilder r10 = r10.append(r11)
            r0 = r21
            int r11 = r0.length
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = " parts: "
            java.lang.StringBuilder r10 = r10.append(r11)
            r0 = r23
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r11 = " in "
            java.lang.StringBuilder r10 = r10.append(r11)
            r0 = r27
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x013f:
            r9 = 0
            r9 = r21[r9]     // Catch:{ IllegalArgumentException -> 0x0193 }
            java.lang.String r4 = stripQuotes(r9)     // Catch:{ IllegalArgumentException -> 0x0193 }
            r9 = 1
            r9 = r21[r9]     // Catch:{ IllegalArgumentException -> 0x0193 }
            java.lang.String r5 = stripQuotes(r9)     // Catch:{ IllegalArgumentException -> 0x0193 }
            r9 = 2
            r9 = r21[r9]     // Catch:{ IllegalArgumentException -> 0x0193 }
            java.lang.String r6 = stripQuotes(r9)     // Catch:{ IllegalArgumentException -> 0x0193 }
            r9 = 3
            r9 = r21[r9]     // Catch:{ IllegalArgumentException -> 0x0193 }
            java.lang.String r9 = stripQuotes(r9)     // Catch:{ IllegalArgumentException -> 0x0193 }
            org.apache.commons.codec.language.bm.Rule$PhonemeExpr r7 = parsePhonemeExpr(r9)     // Catch:{ IllegalArgumentException -> 0x0193 }
            r8 = r14
            org.apache.commons.codec.language.bm.Rule$2 r3 = new org.apache.commons.codec.language.bm.Rule$2     // Catch:{ IllegalArgumentException -> 0x0193 }
            r9 = r27
            r10 = r4
            r11 = r5
            r12 = r6
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ IllegalArgumentException -> 0x0193 }
            java.lang.String r9 = r3.pattern     // Catch:{ IllegalArgumentException -> 0x0193 }
            r10 = 0
            r11 = 1
            java.lang.String r22 = r9.substring(r10, r11)     // Catch:{ IllegalArgumentException -> 0x0193 }
            r0 = r20
            r1 = r22
            java.lang.Object r24 = r0.get(r1)     // Catch:{ IllegalArgumentException -> 0x0193 }
            java.util.List r24 = (java.util.List) r24     // Catch:{ IllegalArgumentException -> 0x0193 }
            if (r24 != 0) goto L_0x018c
            java.util.ArrayList r24 = new java.util.ArrayList     // Catch:{ IllegalArgumentException -> 0x0193 }
            r24.<init>()     // Catch:{ IllegalArgumentException -> 0x0193 }
            r0 = r20
            r1 = r22
            r2 = r24
            r0.put(r1, r2)     // Catch:{ IllegalArgumentException -> 0x0193 }
        L_0x018c:
            r0 = r24
            r0.add(r3)     // Catch:{ IllegalArgumentException -> 0x0193 }
            goto L_0x0008
        L_0x0193:
            r15 = move-exception
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Problem parsing line '"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r14)
            java.lang.String r11 = "' in "
            java.lang.StringBuilder r10 = r10.append(r11)
            r0 = r27
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10, r15)
            throw r9
        L_0x01bb:
            return r20
        L_0x01bc:
            r9 = move-exception
            goto L_0x00e8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.bm.Rule.parseRules(java.util.Scanner, java.lang.String):java.util.Map");
    }

    private static RPattern pattern(final String regex) {
        int i;
        int length;
        final boolean shouldMatch = true;
        boolean startsWith = regex.startsWith(Constant.BETTER_ASR);
        boolean endsWith = regex.endsWith(SymbolExpUtil.SYMBOL_DOLLAR);
        if (startsWith) {
            i = 1;
        } else {
            i = 0;
        }
        if (endsWith) {
            length = regex.length() - 1;
        } else {
            length = regex.length();
        }
        final String content = regex.substring(i, length);
        if (content.contains("[")) {
            boolean startsWithBox = content.startsWith("[");
            boolean endsWithBox = content.endsWith("]");
            if (startsWithBox && endsWithBox) {
                String boxContent = content.substring(1, content.length() - 1);
                if (!boxContent.contains("[")) {
                    boolean negate = boxContent.startsWith(Constant.BETTER_ASR);
                    if (negate) {
                        boxContent = boxContent.substring(1);
                    }
                    final String bContent = boxContent;
                    if (negate) {
                        shouldMatch = false;
                    }
                    if (startsWith && endsWith) {
                        return new RPattern() {
                            public boolean isMatch(CharSequence input) {
                                return input.length() == 1 && Rule.contains(bContent, input.charAt(0)) == shouldMatch;
                            }
                        };
                    }
                    if (startsWith) {
                        return new RPattern() {
                            public boolean isMatch(CharSequence input) {
                                return input.length() > 0 && Rule.contains(bContent, input.charAt(0)) == shouldMatch;
                            }
                        };
                    }
                    if (endsWith) {
                        return new RPattern() {
                            public boolean isMatch(CharSequence input) {
                                return input.length() > 0 && Rule.contains(bContent, input.charAt(input.length() + -1)) == shouldMatch;
                            }
                        };
                    }
                }
            }
        } else if (!startsWith || !endsWith) {
            if ((startsWith || endsWith) && content.length() == 0) {
                return ALL_STRINGS_RMATCHER;
            }
            if (startsWith) {
                return new RPattern() {
                    public boolean isMatch(CharSequence input) {
                        return Rule.startsWith(input, content);
                    }
                };
            }
            if (endsWith) {
                return new RPattern() {
                    public boolean isMatch(CharSequence input) {
                        return Rule.endsWith(input, content);
                    }
                };
            }
        } else if (content.length() == 0) {
            return new RPattern() {
                public boolean isMatch(CharSequence input) {
                    return input.length() == 0;
                }
            };
        } else {
            return new RPattern() {
                public boolean isMatch(CharSequence input) {
                    return input.equals(content);
                }
            };
        }
        return new RPattern() {
            Pattern pattern = Pattern.compile(regex);

            public boolean isMatch(CharSequence input) {
                return this.pattern.matcher(input).find();
            }
        };
    }

    /* access modifiers changed from: private */
    public static boolean startsWith(CharSequence input, CharSequence prefix) {
        if (prefix.length() > input.length()) {
            return false;
        }
        for (int i = 0; i < prefix.length(); i++) {
            if (input.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static String stripQuotes(String str) {
        if (str.startsWith(DOUBLE_QUOTE)) {
            str = str.substring(1);
        }
        if (str.endsWith(DOUBLE_QUOTE)) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    public Rule(String pattern2, String lContext2, String rContext2, PhonemeExpr phoneme2) {
        this.pattern = pattern2;
        this.lContext = pattern(lContext2 + SymbolExpUtil.SYMBOL_DOLLAR);
        this.rContext = pattern(Constant.BETTER_ASR + rContext2);
        this.phoneme = phoneme2;
    }

    public RPattern getLContext() {
        return this.lContext;
    }

    public String getPattern() {
        return this.pattern;
    }

    public PhonemeExpr getPhoneme() {
        return this.phoneme;
    }

    public RPattern getRContext() {
        return this.rContext;
    }

    public boolean patternAndContextMatches(CharSequence input, int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
        }
        int ipl = i + this.pattern.length();
        if (ipl <= input.length() && input.subSequence(i, ipl).equals(this.pattern) && this.rContext.isMatch(input.subSequence(ipl, input.length()))) {
            return this.lContext.isMatch(input.subSequence(0, i));
        }
        return false;
    }
}
