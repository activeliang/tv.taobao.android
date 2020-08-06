package org.apache.commons.codec.language.bm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.codec.language.bm.Languages;

public class Lang {
    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/%s_lang.txt";
    private static final Map<NameType, Lang> Langs = new EnumMap(NameType.class);
    private final Languages languages;
    private final List<LangRule> rules;

    private static final class LangRule {
        /* access modifiers changed from: private */
        public final boolean acceptOnMatch;
        /* access modifiers changed from: private */
        public final Set<String> languages;
        private final Pattern pattern;

        private LangRule(Pattern pattern2, Set<String> languages2, boolean acceptOnMatch2) {
            this.pattern = pattern2;
            this.languages = languages2;
            this.acceptOnMatch = acceptOnMatch2;
        }

        public boolean matches(String txt) {
            return this.pattern.matcher(txt).find();
        }
    }

    static {
        for (NameType s : NameType.values()) {
            Langs.put(s, loadFromResource(String.format(LANGUAGE_RULES_RN, new Object[]{s.getName()}), Languages.getInstance(s)));
        }
    }

    public static Lang instance(NameType nameType) {
        return Langs.get(nameType);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009c, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x009d, code lost:
        r16 = r13;
        r13 = r12;
        r12 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00d9, code lost:
        r12 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.commons.codec.language.bm.Lang loadFromResource(java.lang.String r17, org.apache.commons.codec.language.bm.Languages r18) {
        /*
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.lang.Class<org.apache.commons.codec.language.bm.Lang> r12 = org.apache.commons.codec.language.bm.Lang.class
            java.lang.ClassLoader r12 = r12.getClassLoader()
            r0 = r17
            java.io.InputStream r4 = r12.getResourceAsStream(r0)
            if (r4 != 0) goto L_0x001c
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.String r13 = "Unable to resolve required resource:org/apache/commons/codec/language/bm/%s_lang.txt"
            r12.<init>(r13)
            throw r12
        L_0x001c:
            java.util.Scanner r11 = new java.util.Scanner
            java.lang.String r12 = "UTF-8"
            r11.<init>(r4, r12)
            r13 = 0
            r3 = 0
        L_0x0026:
            boolean r12 = r11.hasNextLine()     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            if (r12 == 0) goto L_0x00db
            java.lang.String r9 = r11.nextLine()     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r6 = r9
            if (r3 == 0) goto L_0x003e
            java.lang.String r12 = "*/"
            boolean r12 = r6.endsWith(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            if (r12 == 0) goto L_0x0026
            r3 = 0
            goto L_0x0026
        L_0x003e:
            java.lang.String r12 = "/*"
            boolean r12 = r6.startsWith(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            if (r12 == 0) goto L_0x0049
            r3 = 1
            goto L_0x0026
        L_0x0049:
            java.lang.String r12 = "//"
            int r2 = r6.indexOf(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            if (r2 < 0) goto L_0x0057
            r12 = 0
            java.lang.String r6 = r6.substring(r12, r2)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
        L_0x0057:
            java.lang.String r6 = r6.trim()     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            int r12 = r6.length()     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            if (r12 == 0) goto L_0x0026
            java.lang.String r12 = "\\s+"
            java.lang.String[] r7 = r6.split(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            int r12 = r7.length     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r14 = 3
            if (r12 == r14) goto L_0x00aa
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r14.<init>()     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.String r15 = "Malformed line '"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.StringBuilder r14 = r14.append(r9)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.String r15 = "' in language resource '"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r0 = r17
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.String r15 = "'"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.String r14 = r14.toString()     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r12.<init>(r14)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            throw r12     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
        L_0x009a:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x009c }
        L_0x009c:
            r13 = move-exception
            r16 = r13
            r13 = r12
            r12 = r16
        L_0x00a2:
            if (r11 == 0) goto L_0x00a9
            if (r13 == 0) goto L_0x00f8
            r11.close()     // Catch:{ Throwable -> 0x00f3 }
        L_0x00a9:
            throw r12
        L_0x00aa:
            r12 = 0
            r12 = r7[r12]     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.util.regex.Pattern r8 = java.util.regex.Pattern.compile(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r12 = 1
            r12 = r7[r12]     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.String r14 = "\\+"
            java.lang.String[] r5 = r12.split(r14)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r12 = 2
            r12 = r7[r12]     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.lang.String r14 = "true"
            boolean r1 = r12.equals(r14)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            org.apache.commons.codec.language.bm.Lang$LangRule r12 = new org.apache.commons.codec.language.bm.Lang$LangRule     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.util.HashSet r14 = new java.util.HashSet     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            java.util.List r15 = java.util.Arrays.asList(r5)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r14.<init>(r15)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r15 = 0
            r12.<init>(r8, r14, r1)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            r10.add(r12)     // Catch:{ Throwable -> 0x009a, all -> 0x00d9 }
            goto L_0x0026
        L_0x00d9:
            r12 = move-exception
            goto L_0x00a2
        L_0x00db:
            if (r11 == 0) goto L_0x00e2
            if (r13 == 0) goto L_0x00ef
            r11.close()     // Catch:{ Throwable -> 0x00ea }
        L_0x00e2:
            org.apache.commons.codec.language.bm.Lang r12 = new org.apache.commons.codec.language.bm.Lang
            r0 = r18
            r12.<init>(r10, r0)
            return r12
        L_0x00ea:
            r12 = move-exception
            r13.addSuppressed(r12)
            goto L_0x00e2
        L_0x00ef:
            r11.close()
            goto L_0x00e2
        L_0x00f3:
            r14 = move-exception
            r13.addSuppressed(r14)
            goto L_0x00a9
        L_0x00f8:
            r11.close()
            goto L_0x00a9
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.bm.Lang.loadFromResource(java.lang.String, org.apache.commons.codec.language.bm.Languages):org.apache.commons.codec.language.bm.Lang");
    }

    private Lang(List<LangRule> rules2, Languages languages2) {
        this.rules = Collections.unmodifiableList(rules2);
        this.languages = languages2;
    }

    public String guessLanguage(String text) {
        Languages.LanguageSet ls = guessLanguages(text);
        return ls.isSingleton() ? ls.getAny() : Languages.ANY;
    }

    public Languages.LanguageSet guessLanguages(String input) {
        String text = input.toLowerCase(Locale.ENGLISH);
        Set<String> langs = new HashSet<>(this.languages.getLanguages());
        for (LangRule rule : this.rules) {
            if (rule.matches(text)) {
                if (rule.acceptOnMatch) {
                    langs.retainAll(rule.languages);
                } else {
                    langs.removeAll(rule.languages);
                }
            }
        }
        Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
        return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
    }
}
