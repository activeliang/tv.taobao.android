package org.apache.commons.codec.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DaitchMokotoffSoundex implements StringEncoder {
    private static final String COMMENT = "//";
    private static final String DOUBLE_QUOTE = "\"";
    private static final Map<Character, Character> FOLDINGS = new HashMap();
    private static final int MAX_LENGTH = 6;
    private static final String MULTILINE_COMMENT_END = "*/";
    private static final String MULTILINE_COMMENT_START = "/*";
    private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
    private static final Map<Character, List<Rule>> RULES = new HashMap();
    private final boolean folding;

    private static final class Branch {
        private final StringBuilder builder;
        private String cachedString;
        private String lastReplacement;

        private Branch() {
            this.builder = new StringBuilder();
            this.lastReplacement = null;
            this.cachedString = null;
        }

        public Branch createBranch() {
            Branch branch = new Branch();
            branch.builder.append(toString());
            branch.lastReplacement = this.lastReplacement;
            return branch;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Branch)) {
                return false;
            }
            return toString().equals(((Branch) other).toString());
        }

        public void finish() {
            while (this.builder.length() < 6) {
                this.builder.append('0');
                this.cachedString = null;
            }
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public void processNextReplacement(String replacement, boolean forceAppend) {
            if ((this.lastReplacement == null || !this.lastReplacement.endsWith(replacement) || forceAppend) && this.builder.length() < 6) {
                this.builder.append(replacement);
                if (this.builder.length() > 6) {
                    this.builder.delete(6, this.builder.length());
                }
                this.cachedString = null;
            }
            this.lastReplacement = replacement;
        }

        public String toString() {
            if (this.cachedString == null) {
                this.cachedString = this.builder.toString();
            }
            return this.cachedString;
        }
    }

    private static final class Rule {
        /* access modifiers changed from: private */
        public final String pattern;
        private final String[] replacementAtStart;
        private final String[] replacementBeforeVowel;
        private final String[] replacementDefault;

        protected Rule(String pattern2, String replacementAtStart2, String replacementBeforeVowel2, String replacementDefault2) {
            this.pattern = pattern2;
            this.replacementAtStart = replacementAtStart2.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
            this.replacementBeforeVowel = replacementBeforeVowel2.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
            this.replacementDefault = replacementDefault2.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
        }

        public int getPatternLength() {
            return this.pattern.length();
        }

        public String[] getReplacements(String context, boolean atStart) {
            if (atStart) {
                return this.replacementAtStart;
            }
            int nextIndex = getPatternLength();
            if (nextIndex < context.length() ? isVowel(context.charAt(nextIndex)) : false) {
                return this.replacementBeforeVowel;
            }
            return this.replacementDefault;
        }

        private boolean isVowel(char ch) {
            return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
        }

        public boolean matches(String context) {
            return context.startsWith(this.pattern);
        }

        public String toString() {
            return String.format("%s=(%s,%s,%s)", new Object[]{this.pattern, Arrays.asList(this.replacementAtStart), Arrays.asList(this.replacementBeforeVowel), Arrays.asList(this.replacementDefault)});
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0070, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0071, code lost:
        r8 = r5;
        r5 = r4;
        r4 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0086, code lost:
        r4 = th;
     */
    static {
        /*
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>()
            RULES = r4
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>()
            FOLDINGS = r4
            java.lang.Class<org.apache.commons.codec.language.DaitchMokotoffSoundex> r4 = org.apache.commons.codec.language.DaitchMokotoffSoundex.class
            java.lang.ClassLoader r4 = r4.getClassLoader()
            java.lang.String r5 = "org/apache/commons/codec/language/dmrules.txt"
            java.io.InputStream r2 = r4.getResourceAsStream(r5)
            if (r2 != 0) goto L_0x0026
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Unable to load resource: org/apache/commons/codec/language/dmrules.txt"
            r4.<init>(r5)
            throw r4
        L_0x0026:
            java.util.Scanner r3 = new java.util.Scanner
            java.lang.String r4 = "UTF-8"
            r3.<init>(r2, r4)
            r5 = 0
            java.lang.String r4 = "org/apache/commons/codec/language/dmrules.txt"
            java.util.Map<java.lang.Character, java.util.List<org.apache.commons.codec.language.DaitchMokotoffSoundex$Rule>> r6 = RULES     // Catch:{ Throwable -> 0x006e, all -> 0x0086 }
            java.util.Map<java.lang.Character, java.lang.Character> r7 = FOLDINGS     // Catch:{ Throwable -> 0x006e, all -> 0x0086 }
            parseRules(r3, r4, r6, r7)     // Catch:{ Throwable -> 0x006e, all -> 0x0086 }
            if (r3 == 0) goto L_0x0040
            if (r5 == 0) goto L_0x006a
            r3.close()     // Catch:{ Throwable -> 0x0065 }
        L_0x0040:
            java.util.Map<java.lang.Character, java.util.List<org.apache.commons.codec.language.DaitchMokotoffSoundex$Rule>> r4 = RULES
            java.util.Set r4 = r4.entrySet()
            java.util.Iterator r4 = r4.iterator()
        L_0x004a:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0085
            java.lang.Object r0 = r4.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r1 = r0.getValue()
            java.util.List r1 = (java.util.List) r1
            org.apache.commons.codec.language.DaitchMokotoffSoundex$1 r5 = new org.apache.commons.codec.language.DaitchMokotoffSoundex$1
            r5.<init>()
            java.util.Collections.sort(r1, r5)
            goto L_0x004a
        L_0x0065:
            r4 = move-exception
            r5.addSuppressed(r4)
            goto L_0x0040
        L_0x006a:
            r3.close()
            goto L_0x0040
        L_0x006e:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0070 }
        L_0x0070:
            r5 = move-exception
            r8 = r5
            r5 = r4
            r4 = r8
        L_0x0074:
            if (r3 == 0) goto L_0x007b
            if (r5 == 0) goto L_0x0081
            r3.close()     // Catch:{ Throwable -> 0x007c }
        L_0x007b:
            throw r4
        L_0x007c:
            r6 = move-exception
            r5.addSuppressed(r6)
            goto L_0x007b
        L_0x0081:
            r3.close()
            goto L_0x007b
        L_0x0085:
            return
        L_0x0086:
            r4 = move-exception
            goto L_0x0074
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DaitchMokotoffSoundex.<clinit>():void");
    }

    private static void parseRules(Scanner scanner, String location, Map<Character, List<Rule>> ruleMapping, Map<Character, Character> asciiFoldings) {
        int currentLine = 0;
        boolean inMultilineComment = false;
        while (scanner.hasNextLine()) {
            currentLine++;
            String rawLine = scanner.nextLine();
            String line = rawLine;
            if (inMultilineComment) {
                if (line.endsWith(MULTILINE_COMMENT_END)) {
                    inMultilineComment = false;
                }
            } else if (line.startsWith(MULTILINE_COMMENT_START)) {
                inMultilineComment = true;
            } else {
                int cmtI = line.indexOf("//");
                if (cmtI >= 0) {
                    line = line.substring(0, cmtI);
                }
                String line2 = line.trim();
                if (line2.length() == 0) {
                    continue;
                } else if (line2.contains("=")) {
                    String[] parts = line2.split("=");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Malformed folding statement split into " + parts.length + " parts: " + rawLine + " in " + location);
                    }
                    String leftCharacter = parts[0];
                    String rightCharacter = parts[1];
                    if (leftCharacter.length() == 1 && rightCharacter.length() == 1) {
                        asciiFoldings.put(Character.valueOf(leftCharacter.charAt(0)), Character.valueOf(rightCharacter.charAt(0)));
                    } else {
                        throw new IllegalArgumentException("Malformed folding statement - patterns are not single characters: " + rawLine + " in " + location);
                    }
                } else {
                    String[] parts2 = line2.split("\\s+");
                    if (parts2.length != 4) {
                        throw new IllegalArgumentException("Malformed rule statement split into " + parts2.length + " parts: " + rawLine + " in " + location);
                    }
                    try {
                        Rule r = new Rule(stripQuotes(parts2[0]), stripQuotes(parts2[1]), stripQuotes(parts2[2]), stripQuotes(parts2[3]));
                        char patternKey = r.pattern.charAt(0);
                        List<Rule> rules = ruleMapping.get(Character.valueOf(patternKey));
                        if (rules == null) {
                            rules = new ArrayList<>();
                            ruleMapping.put(Character.valueOf(patternKey), rules);
                        }
                        rules.add(r);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, e);
                    }
                }
            }
        }
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

    public DaitchMokotoffSoundex() {
        this(true);
    }

    public DaitchMokotoffSoundex(boolean folding2) {
        this.folding = folding2;
    }

    private String cleanup(String input) {
        StringBuilder sb = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (!Character.isWhitespace(ch)) {
                char ch2 = Character.toLowerCase(ch);
                if (this.folding && FOLDINGS.containsKey(Character.valueOf(ch2))) {
                    ch2 = FOLDINGS.get(Character.valueOf(ch2)).charValue();
                }
                sb.append(ch2);
            }
        }
        return sb.toString();
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
    }

    public String encode(String source) {
        if (source == null) {
            return null;
        }
        return soundex(source, false)[0];
    }

    public String soundex(String source) {
        String[] branches = soundex(source, true);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String branch : branches) {
            sb.append(branch);
            index++;
            if (index < branches.length) {
                sb.append('|');
            }
        }
        return sb.toString();
    }

    private String[] soundex(String source, boolean branching) {
        Branch nextBranch;
        if (source == null) {
            return null;
        }
        String input = cleanup(source);
        Set<Branch> currentBranches = new LinkedHashSet<>();
        currentBranches.add(new Branch());
        char lastChar = 0;
        int index = 0;
        while (index < input.length()) {
            char ch = input.charAt(index);
            if (!Character.isWhitespace(ch)) {
                String inputContext = input.substring(index);
                List<Rule> rules = RULES.get(Character.valueOf(ch));
                if (rules != null) {
                    List<Branch> nextBranches = branching ? new ArrayList<>() : Collections.EMPTY_LIST;
                    Iterator<Rule> it = rules.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Rule rule = it.next();
                        if (rule.matches(inputContext)) {
                            if (branching) {
                                nextBranches.clear();
                            }
                            String[] replacements = rule.getReplacements(inputContext, lastChar == 0);
                            boolean branchingRequired = replacements.length > 1 && branching;
                            for (Branch branch : currentBranches) {
                                int length = replacements.length;
                                for (int i = 0; i < length; i++) {
                                    String nextReplacement = replacements[i];
                                    if (branchingRequired) {
                                        nextBranch = branch.createBranch();
                                    } else {
                                        nextBranch = branch;
                                    }
                                    nextBranch.processNextReplacement(nextReplacement, (lastChar == 'm' && ch == 'n') || (lastChar == 'n' && ch == 'm'));
                                    if (!branching) {
                                        break;
                                    }
                                    nextBranches.add(nextBranch);
                                }
                            }
                            if (branching) {
                                currentBranches.clear();
                                currentBranches.addAll(nextBranches);
                            }
                            index += rule.getPatternLength() - 1;
                        }
                    }
                    lastChar = ch;
                }
            }
            index++;
        }
        String[] result = new String[currentBranches.size()];
        int index2 = 0;
        for (Branch branch2 : currentBranches) {
            branch2.finish();
            result[index2] = branch2.toString();
            index2++;
        }
        return result;
    }
}
