package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Rule;

public class PhoneticEngine {
    private static final int DEFAULT_MAX_PHONEMES = 20;
    private static final Map<NameType, Set<String>> NAME_PREFIXES = new EnumMap(NameType.class);
    private final boolean concat;
    private final Lang lang;
    private final int maxPhonemes;
    private final NameType nameType;
    private final RuleType ruleType;

    static final class PhonemeBuilder {
        private final Set<Rule.Phoneme> phonemes;

        public static PhonemeBuilder empty(Languages.LanguageSet languages) {
            return new PhonemeBuilder(new Rule.Phoneme((CharSequence) "", languages));
        }

        private PhonemeBuilder(Rule.Phoneme phoneme) {
            this.phonemes = new LinkedHashSet();
            this.phonemes.add(phoneme);
        }

        private PhonemeBuilder(Set<Rule.Phoneme> phonemes2) {
            this.phonemes = phonemes2;
        }

        public void append(CharSequence str) {
            for (Rule.Phoneme ph : this.phonemes) {
                ph.append(str);
            }
        }

        public void apply(Rule.PhonemeExpr phonemeExpr, int maxPhonemes) {
            Set<Rule.Phoneme> newPhonemes = new LinkedHashSet<>(maxPhonemes);
            loop0:
            for (Rule.Phoneme left : this.phonemes) {
                Iterator<Rule.Phoneme> it = phonemeExpr.getPhonemes().iterator();
                while (true) {
                    if (it.hasNext()) {
                        Rule.Phoneme right = it.next();
                        Languages.LanguageSet languages = left.getLanguages().restrictTo(right.getLanguages());
                        if (!languages.isEmpty()) {
                            Rule.Phoneme join = new Rule.Phoneme(left, right, languages);
                            if (newPhonemes.size() < maxPhonemes) {
                                newPhonemes.add(join);
                                if (newPhonemes.size() >= maxPhonemes) {
                                    break loop0;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
            this.phonemes.clear();
            this.phonemes.addAll(newPhonemes);
        }

        public Set<Rule.Phoneme> getPhonemes() {
            return this.phonemes;
        }

        public String makeString() {
            StringBuilder sb = new StringBuilder();
            for (Rule.Phoneme ph : this.phonemes) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append(ph.getPhonemeText());
            }
            return sb.toString();
        }
    }

    private static final class RulesApplication {
        private final Map<String, List<Rule>> finalRules;
        private boolean found;
        private int i;
        private final CharSequence input;
        private final int maxPhonemes;
        private final PhonemeBuilder phonemeBuilder;

        public RulesApplication(Map<String, List<Rule>> finalRules2, CharSequence input2, PhonemeBuilder phonemeBuilder2, int i2, int maxPhonemes2) {
            if (finalRules2 == null) {
                throw new NullPointerException("The finalRules argument must not be null");
            }
            this.finalRules = finalRules2;
            this.phonemeBuilder = phonemeBuilder2;
            this.input = input2;
            this.i = i2;
            this.maxPhonemes = maxPhonemes2;
        }

        public int getI() {
            return this.i;
        }

        public PhonemeBuilder getPhonemeBuilder() {
            return this.phonemeBuilder;
        }

        public RulesApplication invoke() {
            this.found = false;
            int patternLength = 1;
            List<Rule> rules = this.finalRules.get(this.input.subSequence(this.i, this.i + 1));
            if (rules != null) {
                Iterator<Rule> it = rules.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Rule rule = it.next();
                    patternLength = rule.getPattern().length();
                    if (rule.patternAndContextMatches(this.input, this.i)) {
                        this.phonemeBuilder.apply(rule.getPhoneme(), this.maxPhonemes);
                        this.found = true;
                        break;
                    }
                }
            }
            if (!this.found) {
                patternLength = 1;
            }
            this.i += patternLength;
            return this;
        }

        public boolean isFound() {
            return this.found;
        }
    }

    static {
        NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"bar", "ben", "da", "de", "van", "von"}))));
        NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
        NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
    }

    private static String join(Iterable<String> strings, String sep) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> si = strings.iterator();
        if (si.hasNext()) {
            sb.append(si.next());
        }
        while (si.hasNext()) {
            sb.append(sep).append(si.next());
        }
        return sb.toString();
    }

    public PhoneticEngine(NameType nameType2, RuleType ruleType2, boolean concat2) {
        this(nameType2, ruleType2, concat2, 20);
    }

    public PhoneticEngine(NameType nameType2, RuleType ruleType2, boolean concat2, int maxPhonemes2) {
        if (ruleType2 == RuleType.RULES) {
            throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
        }
        this.nameType = nameType2;
        this.ruleType = ruleType2;
        this.concat = concat2;
        this.lang = Lang.instance(nameType2);
        this.maxPhonemes = maxPhonemes2;
    }

    private PhonemeBuilder applyFinalRules(PhonemeBuilder phonemeBuilder, Map<String, List<Rule>> finalRules) {
        if (finalRules == null) {
            throw new NullPointerException("finalRules can not be null");
        } else if (finalRules.isEmpty()) {
            return phonemeBuilder;
        } else {
            Map<Rule.Phoneme, Rule.Phoneme> phonemes = new TreeMap<>(Rule.Phoneme.COMPARATOR);
            for (Rule.Phoneme phoneme : phonemeBuilder.getPhonemes()) {
                PhonemeBuilder subBuilder = PhonemeBuilder.empty(phoneme.getLanguages());
                String phonemeText = phoneme.getPhonemeText().toString();
                int i = 0;
                while (i < phonemeText.length()) {
                    RulesApplication rulesApplication = new RulesApplication(finalRules, phonemeText, subBuilder, i, this.maxPhonemes).invoke();
                    boolean found = rulesApplication.isFound();
                    subBuilder = rulesApplication.getPhonemeBuilder();
                    if (!found) {
                        subBuilder.append(phonemeText.subSequence(i, i + 1));
                    }
                    i = rulesApplication.getI();
                }
                for (Rule.Phoneme newPhoneme : subBuilder.getPhonemes()) {
                    if (phonemes.containsKey(newPhoneme)) {
                        Rule.Phoneme mergedPhoneme = phonemes.remove(newPhoneme).mergeWithLanguage(newPhoneme.getLanguages());
                        phonemes.put(mergedPhoneme, mergedPhoneme);
                    } else {
                        phonemes.put(newPhoneme, newPhoneme);
                    }
                }
            }
            return new PhonemeBuilder(phonemes.keySet());
        }
    }

    public String encode(String input) {
        return encode(input, this.lang.guessLanguages(input));
    }

    public String encode(String input, Languages.LanguageSet languageSet) {
        String input2;
        Map<String, List<Rule>> rules = Rule.getInstanceMap(this.nameType, RuleType.RULES, languageSet);
        Map<String, List<Rule>> finalRules1 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
        Map<String, List<Rule>> finalRules2 = Rule.getInstanceMap(this.nameType, this.ruleType, languageSet);
        String input3 = input.toLowerCase(Locale.ENGLISH).replace(Soundex.SILENT_MARKER, ' ').trim();
        if (this.nameType == NameType.GENERIC) {
            if (input3.length() < 2 || !input3.substring(0, 2).equals("d'")) {
                for (String l : NAME_PREFIXES.get(this.nameType)) {
                    if (input3.startsWith(l + " ")) {
                        String remainder = input3.substring(l.length() + 1);
                        return "(" + encode(remainder) + ")-(" + encode(l + remainder) + ")";
                    }
                }
            } else {
                String remainder2 = input3.substring(2);
                return "(" + encode(remainder2) + ")-(" + encode("d" + remainder2) + ")";
            }
        }
        List<String> words = Arrays.asList(input3.split("\\s+"));
        ArrayList<String> arrayList = new ArrayList<>();
        switch (this.nameType) {
            case SEPHARDIC:
                for (String aWord : words) {
                    String[] parts = aWord.split("'");
                    arrayList.add(parts[parts.length - 1]);
                }
                arrayList.removeAll(NAME_PREFIXES.get(this.nameType));
                break;
            case ASHKENAZI:
                arrayList.addAll(words);
                arrayList.removeAll(NAME_PREFIXES.get(this.nameType));
                break;
            case GENERIC:
                arrayList.addAll(words);
                break;
            default:
                throw new IllegalStateException("Unreachable case: " + this.nameType);
        }
        if (this.concat) {
            input2 = join(arrayList, " ");
        } else if (arrayList.size() == 1) {
            input2 = words.iterator().next();
        } else {
            StringBuilder result = new StringBuilder();
            for (String word : arrayList) {
                result.append("-").append(encode(word));
            }
            return result.substring(1);
        }
        PhonemeBuilder phonemeBuilder = PhonemeBuilder.empty(languageSet);
        int i = 0;
        while (i < input2.length()) {
            RulesApplication rulesApplication = new RulesApplication(rules, input2, phonemeBuilder, i, this.maxPhonemes).invoke();
            i = rulesApplication.getI();
            phonemeBuilder = rulesApplication.getPhonemeBuilder();
        }
        return applyFinalRules(applyFinalRules(phonemeBuilder, finalRules1), finalRules2).makeString();
    }

    public Lang getLang() {
        return this.lang;
    }

    public NameType getNameType() {
        return this.nameType;
    }

    public RuleType getRuleType() {
        return this.ruleType;
    }

    public boolean isConcat() {
        return this.concat;
    }

    public int getMaxPhonemes() {
        return this.maxPhonemes;
    }
}
