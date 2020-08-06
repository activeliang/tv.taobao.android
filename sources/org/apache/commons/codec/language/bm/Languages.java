package org.apache.commons.codec.language.bm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class Languages {
    public static final String ANY = "any";
    public static final LanguageSet ANY_LANGUAGE = new LanguageSet() {
        public boolean contains(String language) {
            return true;
        }

        public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the any language set.");
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean isSingleton() {
            return false;
        }

        public LanguageSet restrictTo(LanguageSet other) {
            return other;
        }

        public LanguageSet merge(LanguageSet other) {
            return other;
        }

        public String toString() {
            return "ANY_LANGUAGE";
        }
    };
    private static final Map<NameType, Languages> LANGUAGES = new EnumMap(NameType.class);
    public static final LanguageSet NO_LANGUAGES = new LanguageSet() {
        public boolean contains(String language) {
            return false;
        }

        public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the empty language set.");
        }

        public boolean isEmpty() {
            return true;
        }

        public boolean isSingleton() {
            return false;
        }

        public LanguageSet restrictTo(LanguageSet other) {
            return this;
        }

        public LanguageSet merge(LanguageSet other) {
            return other;
        }

        public String toString() {
            return "NO_LANGUAGES";
        }
    };
    private final Set<String> languages;

    public static abstract class LanguageSet {
        public abstract boolean contains(String str);

        public abstract String getAny();

        public abstract boolean isEmpty();

        public abstract boolean isSingleton();

        /* access modifiers changed from: package-private */
        public abstract LanguageSet merge(LanguageSet languageSet);

        public abstract LanguageSet restrictTo(LanguageSet languageSet);

        public static LanguageSet from(Set<String> langs) {
            return langs.isEmpty() ? Languages.NO_LANGUAGES : new SomeLanguages(langs);
        }
    }

    public static final class SomeLanguages extends LanguageSet {
        private final Set<String> languages;

        private SomeLanguages(Set<String> languages2) {
            this.languages = Collections.unmodifiableSet(languages2);
        }

        public boolean contains(String language) {
            return this.languages.contains(language);
        }

        public String getAny() {
            return this.languages.iterator().next();
        }

        public Set<String> getLanguages() {
            return this.languages;
        }

        public boolean isEmpty() {
            return this.languages.isEmpty();
        }

        public boolean isSingleton() {
            return this.languages.size() == 1;
        }

        public LanguageSet restrictTo(LanguageSet other) {
            if (other == Languages.NO_LANGUAGES) {
                return other;
            }
            if (other == Languages.ANY_LANGUAGE) {
                return this;
            }
            SomeLanguages sl = (SomeLanguages) other;
            Set<String> ls = new HashSet<>(Math.min(this.languages.size(), sl.languages.size()));
            for (String lang : this.languages) {
                if (sl.languages.contains(lang)) {
                    ls.add(lang);
                }
            }
            return from(ls);
        }

        /* Debug info: failed to restart local var, previous not found, register: 5 */
        public LanguageSet merge(LanguageSet other) {
            if (other == Languages.NO_LANGUAGES) {
                return this;
            }
            if (other == Languages.ANY_LANGUAGE) {
                return other;
            }
            Set<String> ls = new HashSet<>(this.languages);
            for (String lang : ((SomeLanguages) other).languages) {
                ls.add(lang);
            }
            return from(ls);
        }

        public String toString() {
            return "Languages(" + this.languages.toString() + ")";
        }
    }

    static {
        for (NameType s : NameType.values()) {
            LANGUAGES.put(s, getInstance(langResourceName(s)));
        }
    }

    public static Languages getInstance(NameType nameType) {
        return LANGUAGES.get(nameType);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0067, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        r8 = r6;
        r6 = r5;
        r5 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0096, code lost:
        r5 = th;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.commons.codec.language.bm.Languages getInstance(java.lang.String r9) {
        /*
            java.util.HashSet r3 = new java.util.HashSet
            r3.<init>()
            java.lang.Class<org.apache.commons.codec.language.bm.Languages> r5 = org.apache.commons.codec.language.bm.Languages.class
            java.lang.ClassLoader r5 = r5.getClassLoader()
            java.io.InputStream r1 = r5.getResourceAsStream(r9)
            if (r1 != 0) goto L_0x002b
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Unable to resolve required resource: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L_0x002b:
            java.util.Scanner r4 = new java.util.Scanner
            java.lang.String r5 = "UTF-8"
            r4.<init>(r1, r5)
            r6 = 0
            r0 = 0
        L_0x0035:
            boolean r5 = r4.hasNextLine()     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            if (r5 == 0) goto L_0x0073
            java.lang.String r5 = r4.nextLine()     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            java.lang.String r2 = r5.trim()     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            if (r0 == 0) goto L_0x0050
            java.lang.String r5 = "*/"
            boolean r5 = r2.endsWith(r5)     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            if (r5 == 0) goto L_0x0035
            r0 = 0
            goto L_0x0035
        L_0x0050:
            java.lang.String r5 = "/*"
            boolean r5 = r2.startsWith(r5)     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            if (r5 == 0) goto L_0x005b
            r0 = 1
            goto L_0x0035
        L_0x005b:
            int r5 = r2.length()     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            if (r5 <= 0) goto L_0x0035
            r3.add(r2)     // Catch:{ Throwable -> 0x0065, all -> 0x0096 }
            goto L_0x0035
        L_0x0065:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0067 }
        L_0x0067:
            r6 = move-exception
            r8 = r6
            r6 = r5
            r5 = r8
        L_0x006b:
            if (r4 == 0) goto L_0x0072
            if (r6 == 0) goto L_0x0092
            r4.close()     // Catch:{ Throwable -> 0x008d }
        L_0x0072:
            throw r5
        L_0x0073:
            if (r4 == 0) goto L_0x007a
            if (r6 == 0) goto L_0x0089
            r4.close()     // Catch:{ Throwable -> 0x0084 }
        L_0x007a:
            org.apache.commons.codec.language.bm.Languages r5 = new org.apache.commons.codec.language.bm.Languages
            java.util.Set r6 = java.util.Collections.unmodifiableSet(r3)
            r5.<init>(r6)
            return r5
        L_0x0084:
            r5 = move-exception
            r6.addSuppressed(r5)
            goto L_0x007a
        L_0x0089:
            r4.close()
            goto L_0x007a
        L_0x008d:
            r7 = move-exception
            r6.addSuppressed(r7)
            goto L_0x0072
        L_0x0092:
            r4.close()
            goto L_0x0072
        L_0x0096:
            r5 = move-exception
            goto L_0x006b
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.bm.Languages.getInstance(java.lang.String):org.apache.commons.codec.language.bm.Languages");
    }

    private static String langResourceName(NameType nameType) {
        return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", new Object[]{nameType.getName()});
    }

    private Languages(Set<String> languages2) {
        this.languages = languages2;
    }

    public Set<String> getLanguages() {
        return this.languages;
    }
}
