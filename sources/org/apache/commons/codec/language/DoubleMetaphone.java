package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class DoubleMetaphone implements StringEncoder {
    private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = {"ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
    private static final String[] L_R_N_M_B_H_F_V_W_SPACE = {"L", "R", "N", "M", "B", "H", "F", "V", "W", " "};
    private static final String[] L_T_K_S_N_M_B_Z = {"L", "T", "K", "S", "N", "M", "B", "Z"};
    private static final String[] SILENT_START = {"GN", "KN", "PN", "WR", "PS"};
    private static final String VOWELS = "AEIOUY";
    private int maxCodeLen = 4;

    public String doubleMetaphone(String value) {
        return doubleMetaphone(value, false);
    }

    public String doubleMetaphone(String value, boolean alternate) {
        String value2 = cleanInput(value);
        if (value2 == null) {
            return null;
        }
        boolean slavoGermanic = isSlavoGermanic(value2);
        int index = isSilentStart(value2) ? 1 : 0;
        DoubleMetaphoneResult result = new DoubleMetaphoneResult(getMaxCodeLen());
        while (!result.isComplete() && index <= value2.length() - 1) {
            switch (value2.charAt(index)) {
                case 'A':
                case 'E':
                case 'I':
                case 'O':
                case 'U':
                case 'Y':
                    index = handleAEIOUY(result, index);
                    break;
                case 'B':
                    result.append('P');
                    if (charAt(value2, index + 1) != 'B') {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'C':
                    index = handleC(value2, result, index);
                    break;
                case 'D':
                    index = handleD(value2, result, index);
                    break;
                case 'F':
                    result.append('F');
                    if (charAt(value2, index + 1) != 'F') {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'G':
                    index = handleG(value2, result, index, slavoGermanic);
                    break;
                case 'H':
                    index = handleH(value2, result, index);
                    break;
                case 'J':
                    index = handleJ(value2, result, index, slavoGermanic);
                    break;
                case 'K':
                    result.append('K');
                    if (charAt(value2, index + 1) != 'K') {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'L':
                    index = handleL(value2, result, index);
                    break;
                case 'M':
                    result.append('M');
                    if (!conditionM0(value2, index)) {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'N':
                    result.append('N');
                    if (charAt(value2, index + 1) != 'N') {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'P':
                    index = handleP(value2, result, index);
                    break;
                case 'Q':
                    result.append('K');
                    if (charAt(value2, index + 1) != 'Q') {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'R':
                    index = handleR(value2, result, index, slavoGermanic);
                    break;
                case 'S':
                    index = handleS(value2, result, index, slavoGermanic);
                    break;
                case 'T':
                    index = handleT(value2, result, index);
                    break;
                case 'V':
                    result.append('F');
                    if (charAt(value2, index + 1) != 'V') {
                        index++;
                        break;
                    } else {
                        index += 2;
                        break;
                    }
                case 'W':
                    index = handleW(value2, result, index);
                    break;
                case 'X':
                    index = handleX(value2, result, index);
                    break;
                case 'Z':
                    index = handleZ(value2, result, index, slavoGermanic);
                    break;
                case 199:
                    result.append('S');
                    index++;
                    break;
                case 209:
                    result.append('N');
                    index++;
                    break;
                default:
                    index++;
                    break;
            }
        }
        return alternate ? result.getAlternate() : result.getPrimary();
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return doubleMetaphone((String) obj);
        }
        throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
    }

    public String encode(String value) {
        return doubleMetaphone(value);
    }

    public boolean isDoubleMetaphoneEqual(String value1, String value2) {
        return isDoubleMetaphoneEqual(value1, value2, false);
    }

    public boolean isDoubleMetaphoneEqual(String value1, String value2, boolean alternate) {
        return StringUtils.equals(doubleMetaphone(value1, alternate), doubleMetaphone(value2, alternate));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int maxCodeLen2) {
        this.maxCodeLen = maxCodeLen2;
    }

    private int handleAEIOUY(DoubleMetaphoneResult result, int index) {
        if (index == 0) {
            result.append('A');
        }
        return index + 1;
    }

    private int handleC(String value, DoubleMetaphoneResult result, int index) {
        int index2;
        if (conditionC0(value, index)) {
            result.append('K');
            index2 = index + 2;
        } else {
            if (index == 0) {
                if (contains(value, index, 6, "CAESAR")) {
                    result.append('S');
                    index2 = index + 2;
                }
            }
            if (contains(value, index, 2, "CH")) {
                index2 = handleCH(value, result, index);
            } else {
                if (contains(value, index, 2, "CZ")) {
                    if (!contains(value, index - 2, 4, "WICZ")) {
                        result.append('S', 'X');
                        index2 = index + 2;
                    }
                }
                if (contains(value, index + 1, 3, "CIA")) {
                    result.append('X');
                    index2 = index + 3;
                } else {
                    if (contains(value, index, 2, "CC") && (index != 1 || charAt(value, 0) != 'M')) {
                        return handleCC(value, result, index);
                    }
                    if (contains(value, index, 2, "CK", "CG", "CQ")) {
                        result.append('K');
                        index2 = index + 2;
                    } else {
                        if (contains(value, index, 2, "CI", "CE", "CY")) {
                            if (contains(value, index, 3, "CIO", "CIE", "CIA")) {
                                result.append('S', 'X');
                            } else {
                                result.append('S');
                            }
                            index2 = index + 2;
                        } else {
                            result.append('K');
                            if (contains(value, index + 1, 2, " C", " Q", " G")) {
                                index2 = index + 3;
                            } else {
                                if (contains(value, index + 1, 1, "C", "K", "Q")) {
                                    if (!contains(value, index + 1, 2, "CE", "CI")) {
                                        index2 = index + 2;
                                    }
                                }
                                index2 = index + 1;
                            }
                        }
                    }
                }
            }
        }
        return index2;
    }

    private int handleCC(String value, DoubleMetaphoneResult result, int index) {
        if (contains(value, index + 2, 1, "I", "E", "H")) {
            if (!contains(value, index + 2, 2, "HU")) {
                if (!(index == 1 && charAt(value, index - 1) == 'A')) {
                    if (!contains(value, index - 1, 5, "UCCEE", "UCCES")) {
                        result.append('X');
                        return index + 3;
                    }
                }
                result.append("KS");
                return index + 3;
            }
        }
        result.append('K');
        return index + 2;
    }

    private int handleCH(String value, DoubleMetaphoneResult result, int index) {
        if (index > 0) {
            if (contains(value, index, 4, "CHAE")) {
                result.append('K', 'X');
                return index + 2;
            }
        }
        if (conditionCH0(value, index)) {
            result.append('K');
            return index + 2;
        } else if (conditionCH1(value, index)) {
            result.append('K');
            return index + 2;
        } else {
            if (index > 0) {
                if (contains(value, 0, 2, "MC")) {
                    result.append('K');
                } else {
                    result.append('X', 'K');
                }
            } else {
                result.append('X');
            }
            return index + 2;
        }
    }

    private int handleD(String value, DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 2, "DG")) {
            if (contains(value, index + 2, 1, "I", "E", "Y")) {
                result.append('J');
                return index + 3;
            }
            result.append("TK");
            return index + 2;
        }
        if (contains(value, index, 2, "DT", "DD")) {
            result.append('T');
            return index + 2;
        }
        result.append('T');
        return index + 1;
    }

    private int handleG(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if (charAt(value, index + 1) == 'H') {
            return handleGH(value, result, index);
        }
        if (charAt(value, index + 1) == 'N') {
            if (index != 1 || !isVowel(charAt(value, 0)) || slavoGermanic) {
                if (contains(value, index + 2, 2, "EY") || charAt(value, index + 1) == 'Y' || slavoGermanic) {
                    result.append("KN");
                } else {
                    result.append("N", "KN");
                }
            } else {
                result.append("KN", "N");
            }
            return index + 2;
        }
        if (contains(value, index + 1, 2, "LI") && !slavoGermanic) {
            result.append("KL", "L");
            return index + 2;
        } else if (index != 0 || (charAt(value, index + 1) != 'Y' && !contains(value, index + 1, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
            if (contains(value, index + 1, 2, "ER") || charAt(value, index + 1) == 'Y') {
                if (!contains(value, 0, 6, "DANGER", "RANGER", "MANGER")) {
                    if (!contains(value, index - 1, 1, "E", "I")) {
                        if (!contains(value, index - 1, 3, "RGY", "OGY")) {
                            result.append('K', 'J');
                            return index + 2;
                        }
                    }
                }
            }
            if (!contains(value, index + 1, 1, "E", "I", "Y")) {
                if (!contains(value, index - 1, 4, "AGGI", "OGGI")) {
                    if (charAt(value, index + 1) == 'G') {
                        int index2 = index + 2;
                        result.append('K');
                        return index2;
                    }
                    int index3 = index + 1;
                    result.append('K');
                    return index3;
                }
            }
            if (!contains(value, 0, 4, "VAN ", "VON ")) {
                if (!contains(value, 0, 3, "SCH")) {
                    if (!contains(value, index + 1, 2, "ET")) {
                        if (contains(value, index + 1, 3, "IER")) {
                            result.append('J');
                        } else {
                            result.append('J', 'K');
                        }
                        return index + 2;
                    }
                }
            }
            result.append('K');
            return index + 2;
        } else {
            result.append('K', 'J');
            return index + 2;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004b, code lost:
        if (contains(r9, r11 - 2, 1, "B", "H", "D") == false) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0066, code lost:
        if (contains(r9, r11 - 3, 1, "B", "H", "D") == false) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x007c, code lost:
        if (contains(r9, r11 - 4, 1, "B", "H") != false) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        return r11 + 2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleGH(java.lang.String r9, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult r10, int r11) {
        /*
            r8 = this;
            r7 = 75
            r6 = 3
            r5 = 0
            r3 = 2
            r4 = 1
            if (r11 <= 0) goto L_0x001a
            int r0 = r11 + -1
            char r0 = r8.charAt(r9, r0)
            boolean r0 = r8.isVowel(r0)
            if (r0 != 0) goto L_0x001a
            r10.append((char) r7)
            int r11 = r11 + 2
        L_0x0019:
            return r11
        L_0x001a:
            if (r11 != 0) goto L_0x0032
            int r0 = r11 + 2
            char r0 = r8.charAt(r9, r0)
            r1 = 73
            if (r0 != r1) goto L_0x002e
            r0 = 74
            r10.append((char) r0)
        L_0x002b:
            int r11 = r11 + 2
            goto L_0x0019
        L_0x002e:
            r10.append((char) r7)
            goto L_0x002b
        L_0x0032:
            if (r11 <= r4) goto L_0x004d
            int r0 = r11 + -2
            java.lang.String[] r1 = new java.lang.String[r6]
            java.lang.String r2 = "B"
            r1[r5] = r2
            java.lang.String r2 = "H"
            r1[r4] = r2
            java.lang.String r2 = "D"
            r1[r3] = r2
            boolean r0 = contains(r9, r0, r4, r1)
            if (r0 != 0) goto L_0x007e
        L_0x004d:
            if (r11 <= r3) goto L_0x0068
            int r0 = r11 + -3
            java.lang.String[] r1 = new java.lang.String[r6]
            java.lang.String r2 = "B"
            r1[r5] = r2
            java.lang.String r2 = "H"
            r1[r4] = r2
            java.lang.String r2 = "D"
            r1[r3] = r2
            boolean r0 = contains(r9, r0, r4, r1)
            if (r0 != 0) goto L_0x007e
        L_0x0068:
            if (r11 <= r6) goto L_0x0081
            int r0 = r11 + -4
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.String r2 = "B"
            r1[r5] = r2
            java.lang.String r2 = "H"
            r1[r4] = r2
            boolean r0 = contains(r9, r0, r4, r1)
            if (r0 == 0) goto L_0x0081
        L_0x007e:
            int r11 = r11 + 2
            goto L_0x0019
        L_0x0081:
            if (r11 <= r3) goto L_0x00bb
            int r0 = r11 + -1
            char r0 = r8.charAt(r9, r0)
            r1 = 85
            if (r0 != r1) goto L_0x00bb
            int r0 = r11 + -3
            r1 = 5
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.String r2 = "C"
            r1[r5] = r2
            java.lang.String r2 = "G"
            r1[r4] = r2
            java.lang.String r2 = "L"
            r1[r3] = r2
            java.lang.String r2 = "R"
            r1[r6] = r2
            r2 = 4
            java.lang.String r3 = "T"
            r1[r2] = r3
            boolean r0 = contains(r9, r0, r4, r1)
            if (r0 == 0) goto L_0x00bb
            r0 = 70
            r10.append((char) r0)
        L_0x00b7:
            int r11 = r11 + 2
            goto L_0x0019
        L_0x00bb:
            if (r11 <= 0) goto L_0x00b7
            int r0 = r11 + -1
            char r0 = r8.charAt(r9, r0)
            r1 = 73
            if (r0 == r1) goto L_0x00b7
            r10.append((char) r7)
            goto L_0x00b7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.handleGH(java.lang.String, org.apache.commons.codec.language.DoubleMetaphone$DoubleMetaphoneResult, int):int");
    }

    private int handleH(String value, DoubleMetaphoneResult result, int index) {
        if ((index != 0 && !isVowel(charAt(value, index - 1))) || !isVowel(charAt(value, index + 1))) {
            return index + 1;
        }
        result.append('H');
        return index + 2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00c6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleJ(java.lang.String r8, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult r9, int r10, boolean r11) {
        /*
            r7 = this;
            r6 = 72
            r2 = 4
            r5 = 74
            r4 = 1
            r3 = 0
            java.lang.String[] r0 = new java.lang.String[r4]
            java.lang.String r1 = "JOSE"
            r0[r3] = r1
            boolean r0 = contains(r8, r10, r2, r0)
            if (r0 != 0) goto L_0x0021
            java.lang.String[] r0 = new java.lang.String[r4]
            java.lang.String r1 = "SAN "
            r0[r3] = r1
            boolean r0 = contains(r8, r3, r2, r0)
            if (r0 == 0) goto L_0x004a
        L_0x0021:
            if (r10 != 0) goto L_0x002d
            int r0 = r10 + 4
            char r0 = r7.charAt(r8, r0)
            r1 = 32
            if (r0 == r1) goto L_0x0040
        L_0x002d:
            int r0 = r8.length()
            if (r0 == r2) goto L_0x0040
            java.lang.String[] r0 = new java.lang.String[r4]
            java.lang.String r1 = "SAN "
            r0[r3] = r1
            boolean r0 = contains(r8, r3, r2, r0)
            if (r0 == 0) goto L_0x0046
        L_0x0040:
            r9.append((char) r6)
        L_0x0043:
            int r10 = r10 + 1
        L_0x0045:
            return r10
        L_0x0046:
            r9.append((char) r5, (char) r6)
            goto L_0x0043
        L_0x004a:
            if (r10 != 0) goto L_0x0069
            java.lang.String[] r0 = new java.lang.String[r4]
            java.lang.String r1 = "JOSE"
            r0[r3] = r1
            boolean r0 = contains(r8, r10, r2, r0)
            if (r0 != 0) goto L_0x0069
            r0 = 65
            r9.append((char) r5, (char) r0)
        L_0x005e:
            int r0 = r10 + 1
            char r0 = r7.charAt(r8, r0)
            if (r0 != r5) goto L_0x00c6
            int r10 = r10 + 2
            goto L_0x0045
        L_0x0069:
            int r0 = r10 + -1
            char r0 = r7.charAt(r8, r0)
            boolean r0 = r7.isVowel(r0)
            if (r0 == 0) goto L_0x008f
            if (r11 != 0) goto L_0x008f
            int r0 = r10 + 1
            char r0 = r7.charAt(r8, r0)
            r1 = 65
            if (r0 == r1) goto L_0x008b
            int r0 = r10 + 1
            char r0 = r7.charAt(r8, r0)
            r1 = 79
            if (r0 != r1) goto L_0x008f
        L_0x008b:
            r9.append((char) r5, (char) r6)
            goto L_0x005e
        L_0x008f:
            int r0 = r8.length()
            int r0 = r0 + -1
            if (r10 != r0) goto L_0x009d
            r0 = 32
            r9.append((char) r5, (char) r0)
            goto L_0x005e
        L_0x009d:
            int r0 = r10 + 1
            java.lang.String[] r1 = L_T_K_S_N_M_B_Z
            boolean r0 = contains(r8, r0, r4, r1)
            if (r0 != 0) goto L_0x005e
            int r0 = r10 + -1
            r1 = 3
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.String r2 = "S"
            r1[r3] = r2
            java.lang.String r2 = "K"
            r1[r4] = r2
            r2 = 2
            java.lang.String r3 = "L"
            r1[r2] = r3
            boolean r0 = contains(r8, r0, r4, r1)
            if (r0 != 0) goto L_0x005e
            r9.append((char) r5)
            goto L_0x005e
        L_0x00c6:
            int r10 = r10 + 1
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.handleJ(java.lang.String, org.apache.commons.codec.language.DoubleMetaphone$DoubleMetaphoneResult, int, boolean):int");
    }

    private int handleL(String value, DoubleMetaphoneResult result, int index) {
        if (charAt(value, index + 1) == 'L') {
            if (conditionL0(value, index)) {
                result.appendPrimary('L');
            } else {
                result.append('L');
            }
            return index + 2;
        }
        int index2 = index + 1;
        result.append('L');
        return index2;
    }

    private int handleP(String value, DoubleMetaphoneResult result, int index) {
        if (charAt(value, index + 1) == 'H') {
            result.append('F');
            return index + 2;
        }
        result.append('P');
        return contains(value, index + 1, 1, "P", "B") ? index + 2 : index + 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleR(java.lang.String r8, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult r9, int r10, boolean r11) {
        /*
            r7 = this;
            r6 = 1
            r5 = 0
            r4 = 82
            r3 = 2
            int r0 = r8.length()
            int r0 = r0 + -1
            if (r10 != r0) goto L_0x0040
            if (r11 != 0) goto L_0x0040
            int r0 = r10 + -2
            java.lang.String[] r1 = new java.lang.String[r6]
            java.lang.String r2 = "IE"
            r1[r5] = r2
            boolean r0 = contains(r8, r0, r3, r1)
            if (r0 == 0) goto L_0x0040
            int r0 = r10 + -4
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.String r2 = "ME"
            r1[r5] = r2
            java.lang.String r2 = "MA"
            r1[r6] = r2
            boolean r0 = contains(r8, r0, r3, r1)
            if (r0 != 0) goto L_0x0040
            r9.appendAlternate((char) r4)
        L_0x0035:
            int r0 = r10 + 1
            char r0 = r7.charAt(r8, r0)
            if (r0 != r4) goto L_0x0044
            int r0 = r10 + 2
        L_0x003f:
            return r0
        L_0x0040:
            r9.append((char) r4)
            goto L_0x0035
        L_0x0044:
            int r0 = r10 + 1
            goto L_0x003f
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.handleR(java.lang.String, org.apache.commons.codec.language.DoubleMetaphone$DoubleMetaphoneResult, int, boolean):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00bb, code lost:
        if (contains(r10, r12 + 1, 1, "M", "N", "L", "W") == false) goto L_0x00bd;
     */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0135  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleS(java.lang.String r10, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult r11, int r12, boolean r13) {
        /*
            r9 = this;
            r8 = 3
            r7 = 83
            r6 = 2
            r5 = 0
            r4 = 1
            int r0 = r12 + -1
            java.lang.String[] r1 = new java.lang.String[r6]
            java.lang.String r2 = "ISL"
            r1[r5] = r2
            java.lang.String r2 = "YSL"
            r1[r4] = r2
            boolean r0 = contains(r10, r0, r8, r1)
            if (r0 == 0) goto L_0x001d
            int r12 = r12 + 1
        L_0x001c:
            return r12
        L_0x001d:
            if (r12 != 0) goto L_0x0035
            r0 = 5
            java.lang.String[] r1 = new java.lang.String[r4]
            java.lang.String r2 = "SUGAR"
            r1[r5] = r2
            boolean r0 = contains(r10, r12, r0, r1)
            if (r0 == 0) goto L_0x0035
            r0 = 88
            r11.append((char) r0, (char) r7)
            int r12 = r12 + 1
            goto L_0x001c
        L_0x0035:
            java.lang.String[] r0 = new java.lang.String[r4]
            java.lang.String r1 = "SH"
            r0[r5] = r1
            boolean r0 = contains(r10, r12, r6, r0)
            if (r0 == 0) goto L_0x006e
            int r0 = r12 + 1
            r1 = 4
            r2 = 4
            java.lang.String[] r2 = new java.lang.String[r2]
            java.lang.String r3 = "HEIM"
            r2[r5] = r3
            java.lang.String r3 = "HOEK"
            r2[r4] = r3
            java.lang.String r3 = "HOLM"
            r2[r6] = r3
            java.lang.String r3 = "HOLZ"
            r2[r8] = r3
            boolean r0 = contains(r10, r0, r1, r2)
            if (r0 == 0) goto L_0x0068
            r11.append((char) r7)
        L_0x0065:
            int r12 = r12 + 2
            goto L_0x001c
        L_0x0068:
            r0 = 88
            r11.append((char) r0)
            goto L_0x0065
        L_0x006e:
            java.lang.String[] r0 = new java.lang.String[r6]
            java.lang.String r1 = "SIO"
            r0[r5] = r1
            java.lang.String r1 = "SIA"
            r0[r4] = r1
            boolean r0 = contains(r10, r12, r8, r0)
            if (r0 != 0) goto L_0x008e
            r0 = 4
            java.lang.String[] r1 = new java.lang.String[r4]
            java.lang.String r2 = "SIAN"
            r1[r5] = r2
            boolean r0 = contains(r10, r12, r0, r1)
            if (r0 == 0) goto L_0x009c
        L_0x008e:
            if (r13 == 0) goto L_0x0096
            r11.append((char) r7)
        L_0x0093:
            int r12 = r12 + 3
            goto L_0x001c
        L_0x0096:
            r0 = 88
            r11.append((char) r7, (char) r0)
            goto L_0x0093
        L_0x009c:
            if (r12 != 0) goto L_0x00bd
            int r0 = r12 + 1
            r1 = 4
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.String r2 = "M"
            r1[r5] = r2
            java.lang.String r2 = "N"
            r1[r4] = r2
            java.lang.String r2 = "L"
            r1[r6] = r2
            java.lang.String r2 = "W"
            r1[r8] = r2
            boolean r0 = contains(r10, r0, r4, r1)
            if (r0 != 0) goto L_0x00cc
        L_0x00bd:
            int r0 = r12 + 1
            java.lang.String[] r1 = new java.lang.String[r4]
            java.lang.String r2 = "Z"
            r1[r5] = r2
            boolean r0 = contains(r10, r0, r4, r1)
            if (r0 == 0) goto L_0x00e7
        L_0x00cc:
            r0 = 88
            r11.append((char) r7, (char) r0)
            int r0 = r12 + 1
            java.lang.String[] r1 = new java.lang.String[r4]
            java.lang.String r2 = "Z"
            r1[r5] = r2
            boolean r0 = contains(r10, r0, r4, r1)
            if (r0 == 0) goto L_0x00e4
            int r12 = r12 + 2
        L_0x00e2:
            goto L_0x001c
        L_0x00e4:
            int r12 = r12 + 1
            goto L_0x00e2
        L_0x00e7:
            java.lang.String[] r0 = new java.lang.String[r4]
            java.lang.String r1 = "SC"
            r0[r5] = r1
            boolean r0 = contains(r10, r12, r6, r0)
            if (r0 == 0) goto L_0x00fa
            int r12 = r9.handleSC(r10, r11, r12)
            goto L_0x001c
        L_0x00fa:
            int r0 = r10.length()
            int r0 = r0 + -1
            if (r12 != r0) goto L_0x0131
            int r0 = r12 + -2
            java.lang.String[] r1 = new java.lang.String[r6]
            java.lang.String r2 = "AI"
            r1[r5] = r2
            java.lang.String r2 = "OI"
            r1[r4] = r2
            boolean r0 = contains(r10, r0, r6, r1)
            if (r0 == 0) goto L_0x0131
            r11.appendAlternate((char) r7)
        L_0x0119:
            int r0 = r12 + 1
            java.lang.String[] r1 = new java.lang.String[r6]
            java.lang.String r2 = "S"
            r1[r5] = r2
            java.lang.String r2 = "Z"
            r1[r4] = r2
            boolean r0 = contains(r10, r0, r4, r1)
            if (r0 == 0) goto L_0x0135
            int r12 = r12 + 2
        L_0x012f:
            goto L_0x001c
        L_0x0131:
            r11.append((char) r7)
            goto L_0x0119
        L_0x0135:
            int r12 = r12 + 1
            goto L_0x012f
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.handleS(java.lang.String, org.apache.commons.codec.language.DoubleMetaphone$DoubleMetaphoneResult, int, boolean):int");
    }

    private int handleSC(String value, DoubleMetaphoneResult result, int index) {
        if (charAt(value, index + 2) == 'H') {
            if (contains(value, index + 3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
                if (contains(value, index + 3, 2, "ER", "EN")) {
                    result.append("X", "SK");
                } else {
                    result.append("SK");
                }
            } else if (index != 0 || isVowel(charAt(value, 3)) || charAt(value, 3) == 'W') {
                result.append('X');
            } else {
                result.append('X', 'S');
            }
        } else {
            if (contains(value, index + 2, 1, "I", "E", "Y")) {
                result.append('S');
            } else {
                result.append("SK");
            }
        }
        return index + 3;
    }

    private int handleT(String value, DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 4, "TION")) {
            result.append('X');
            return index + 3;
        }
        if (contains(value, index, 3, "TIA", "TCH")) {
            result.append('X');
            return index + 3;
        }
        if (!contains(value, index, 2, "TH")) {
            if (!contains(value, index, 3, "TTH")) {
                result.append('T');
                return contains(value, index + 1, 1, "T", "D") ? index + 2 : index + 1;
            }
        }
        if (!contains(value, index + 2, 2, "OM", "AM")) {
            if (!contains(value, 0, 4, "VAN ", "VON ")) {
                if (!contains(value, 0, 3, "SCH")) {
                    result.append('0', 'T');
                    return index + 2;
                }
            }
        }
        result.append('T');
        return index + 2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0033, code lost:
        if (contains(r10, r12, 2, "WH") != false) goto L_0x0035;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleW(java.lang.String r10, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult r11, int r12) {
        /*
            r9 = this;
            r8 = 4
            r7 = 3
            r6 = 2
            r5 = 1
            r4 = 0
            java.lang.String[] r0 = new java.lang.String[r5]
            java.lang.String r1 = "WR"
            r0[r4] = r1
            boolean r0 = contains(r10, r12, r6, r0)
            if (r0 == 0) goto L_0x001a
            r0 = 82
            r11.append((char) r0)
            int r12 = r12 + 2
        L_0x0019:
            return r12
        L_0x001a:
            if (r12 != 0) goto L_0x0051
            int r0 = r12 + 1
            char r0 = r9.charAt(r10, r0)
            boolean r0 = r9.isVowel(r0)
            if (r0 != 0) goto L_0x0035
            java.lang.String[] r0 = new java.lang.String[r5]
            java.lang.String r1 = "WH"
            r0[r4] = r1
            boolean r0 = contains(r10, r12, r6, r0)
            if (r0 == 0) goto L_0x0051
        L_0x0035:
            int r0 = r12 + 1
            char r0 = r9.charAt(r10, r0)
            boolean r0 = r9.isVowel(r0)
            if (r0 == 0) goto L_0x004b
            r0 = 65
            r1 = 70
            r11.append((char) r0, (char) r1)
        L_0x0048:
            int r12 = r12 + 1
            goto L_0x0019
        L_0x004b:
            r0 = 65
            r11.append((char) r0)
            goto L_0x0048
        L_0x0051:
            int r0 = r10.length()
            int r0 = r0 + -1
            if (r12 != r0) goto L_0x0065
            int r0 = r12 + -1
            char r0 = r9.charAt(r10, r0)
            boolean r0 = r9.isVowel(r0)
            if (r0 != 0) goto L_0x0091
        L_0x0065:
            int r0 = r12 + -1
            r1 = 5
            java.lang.String[] r2 = new java.lang.String[r8]
            java.lang.String r3 = "EWSKI"
            r2[r4] = r3
            java.lang.String r3 = "EWSKY"
            r2[r5] = r3
            java.lang.String r3 = "OWSKI"
            r2[r6] = r3
            java.lang.String r3 = "OWSKY"
            r2[r7] = r3
            boolean r0 = contains(r10, r0, r1, r2)
            if (r0 != 0) goto L_0x0091
            java.lang.String[] r0 = new java.lang.String[r5]
            java.lang.String r1 = "SCH"
            r0[r4] = r1
            boolean r0 = contains(r10, r4, r7, r0)
            if (r0 == 0) goto L_0x0099
        L_0x0091:
            r0 = 70
            r11.appendAlternate((char) r0)
            int r12 = r12 + 1
            goto L_0x0019
        L_0x0099:
            java.lang.String[] r0 = new java.lang.String[r6]
            java.lang.String r1 = "WICZ"
            r0[r4] = r1
            java.lang.String r1 = "WITZ"
            r0[r5] = r1
            boolean r0 = contains(r10, r12, r8, r0)
            if (r0 == 0) goto L_0x00b8
            java.lang.String r0 = "TS"
            java.lang.String r1 = "FX"
            r11.append((java.lang.String) r0, (java.lang.String) r1)
            int r12 = r12 + 4
            goto L_0x0019
        L_0x00b8:
            int r12 = r12 + 1
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.handleW(java.lang.String, org.apache.commons.codec.language.DoubleMetaphone$DoubleMetaphoneResult, int):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003c, code lost:
        if (contains(r8, r10 - 2, 2, "AU", "OU") == false) goto L_0x003e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x005b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleX(java.lang.String r8, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult r9, int r10) {
        /*
            r7 = this;
            r6 = 0
            r5 = 2
            r4 = 1
            if (r10 != 0) goto L_0x000d
            r0 = 83
            r9.append((char) r0)
            int r10 = r10 + 1
        L_0x000c:
            return r10
        L_0x000d:
            int r0 = r8.length()
            int r0 = r0 + -1
            if (r10 != r0) goto L_0x003e
            int r0 = r10 + -3
            r1 = 3
            java.lang.String[] r2 = new java.lang.String[r5]
            java.lang.String r3 = "IAU"
            r2[r6] = r3
            java.lang.String r3 = "EAU"
            r2[r4] = r3
            boolean r0 = contains(r8, r0, r1, r2)
            if (r0 != 0) goto L_0x0044
            int r0 = r10 + -2
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "AU"
            r1[r6] = r2
            java.lang.String r2 = "OU"
            r1[r4] = r2
            boolean r0 = contains(r8, r0, r5, r1)
            if (r0 != 0) goto L_0x0044
        L_0x003e:
            java.lang.String r0 = "KS"
            r9.append((java.lang.String) r0)
        L_0x0044:
            int r0 = r10 + 1
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "C"
            r1[r6] = r2
            java.lang.String r2 = "X"
            r1[r4] = r2
            boolean r0 = contains(r8, r0, r4, r1)
            if (r0 == 0) goto L_0x005b
            int r10 = r10 + 2
        L_0x005a:
            goto L_0x000c
        L_0x005b:
            int r10 = r10 + 1
            goto L_0x005a
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.handleX(java.lang.String, org.apache.commons.codec.language.DoubleMetaphone$DoubleMetaphoneResult, int):int");
    }

    private int handleZ(String value, DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if (charAt(value, index + 1) == 'H') {
            result.append('J');
            return index + 2;
        }
        if (contains(value, index + 1, 2, "ZO", "ZI", "ZA") || (slavoGermanic && index > 0 && charAt(value, index - 1) != 'T')) {
            result.append("S", "TS");
        } else {
            result.append('S');
        }
        return charAt(value, index + 1) == 'Z' ? index + 2 : index + 1;
    }

    private boolean conditionC0(String value, int index) {
        if (contains(value, index, 4, "CHIA")) {
            return true;
        }
        if (index <= 1 || isVowel(charAt(value, index - 2))) {
            return false;
        }
        if (!contains(value, index - 1, 3, "ACH")) {
            return false;
        }
        char c = charAt(value, index + 2);
        if (c == 'I' || c == 'E') {
            if (!contains(value, index - 2, 6, "BACHER", "MACHER")) {
                return false;
            }
        }
        return true;
    }

    private boolean conditionCH0(String value, int index) {
        if (index != 0) {
            return false;
        }
        if (!contains(value, index + 1, 5, "HARAC", "HARIS")) {
            if (!contains(value, index + 1, 3, "HOR", "HYM", "HIA", "HEM")) {
                return false;
            }
        }
        if (!contains(value, 0, 5, "CHORE")) {
            return true;
        }
        return false;
    }

    private boolean conditionCH1(String value, int index) {
        if (!contains(value, 0, 4, "VAN ", "VON ")) {
            if (!contains(value, 0, 3, "SCH")) {
                if (!contains(value, index - 2, 6, "ORCHES", "ARCHIT", "ORCHID")) {
                    if (!contains(value, index + 2, 1, "T", "S")) {
                        if (!contains(value, index - 1, 1, "A", "O", "U", "E") && index != 0) {
                            return false;
                        }
                        if (!contains(value, index + 2, 1, L_R_N_M_B_H_F_V_W_SPACE) && index + 1 != value.length() - 1) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0055, code lost:
        if (contains(r8, r8.length() - 1, 1, "A", "O") != false) goto L_0x0057;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean conditionL0(java.lang.String r8, int r9) {
        /*
            r7 = this;
            r6 = 4
            r5 = 2
            r1 = 0
            r0 = 1
            int r2 = r8.length()
            int r2 = r2 + -3
            if (r9 != r2) goto L_0x0027
            int r2 = r9 + -1
            r3 = 3
            java.lang.String[] r3 = new java.lang.String[r3]
            java.lang.String r4 = "ILLO"
            r3[r1] = r4
            java.lang.String r4 = "ILLA"
            r3[r0] = r4
            java.lang.String r4 = "ALLE"
            r3[r5] = r4
            boolean r2 = contains(r8, r2, r6, r3)
            if (r2 == 0) goto L_0x0027
        L_0x0026:
            return r0
        L_0x0027:
            int r2 = r8.length()
            int r2 = r2 + -2
            java.lang.String[] r3 = new java.lang.String[r5]
            java.lang.String r4 = "AS"
            r3[r1] = r4
            java.lang.String r4 = "OS"
            r3[r0] = r4
            boolean r2 = contains(r8, r2, r5, r3)
            if (r2 != 0) goto L_0x0057
            int r2 = r8.length()
            int r2 = r2 + -1
            java.lang.String[] r3 = new java.lang.String[r5]
            java.lang.String r4 = "A"
            r3[r1] = r4
            java.lang.String r4 = "O"
            r3[r0] = r4
            boolean r2 = contains(r8, r2, r0, r3)
            if (r2 == 0) goto L_0x0066
        L_0x0057:
            int r2 = r9 + -1
            java.lang.String[] r3 = new java.lang.String[r0]
            java.lang.String r4 = "ALLE"
            r3[r1] = r4
            boolean r2 = contains(r8, r2, r6, r3)
            if (r2 != 0) goto L_0x0026
        L_0x0066:
            r0 = r1
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.language.DoubleMetaphone.conditionL0(java.lang.String, int):boolean");
    }

    private boolean conditionM0(String value, int index) {
        if (charAt(value, index + 1) == 'M') {
            return true;
        }
        if (contains(value, index - 1, 3, "UMB")) {
            if (index + 1 == value.length() - 1) {
                return true;
            }
            if (contains(value, index + 2, 2, "ER")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSlavoGermanic(String value) {
        return value.indexOf(87) > -1 || value.indexOf(75) > -1 || value.indexOf("CZ") > -1 || value.indexOf("WITZ") > -1;
    }

    private boolean isVowel(char ch) {
        return VOWELS.indexOf(ch) != -1;
    }

    private boolean isSilentStart(String value) {
        for (String element : SILENT_START) {
            if (value.startsWith(element)) {
                return true;
            }
        }
        return false;
    }

    private String cleanInput(String input) {
        if (input == null) {
            return null;
        }
        String input2 = input.trim();
        if (input2.length() != 0) {
            return input2.toUpperCase(Locale.ENGLISH);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public char charAt(String value, int index) {
        if (index < 0 || index >= value.length()) {
            return 0;
        }
        return value.charAt(index);
    }

    protected static boolean contains(String value, int start, int length, String... criteria) {
        if (start < 0 || start + length > value.length()) {
            return false;
        }
        String target = value.substring(start, start + length);
        for (String element : criteria) {
            if (target.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public class DoubleMetaphoneResult {
        private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
        private final int maxLength;
        private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());

        public DoubleMetaphoneResult(int maxLength2) {
            this.maxLength = maxLength2;
        }

        public void append(char value) {
            appendPrimary(value);
            appendAlternate(value);
        }

        public void append(char primary2, char alternate2) {
            appendPrimary(primary2);
            appendAlternate(alternate2);
        }

        public void appendPrimary(char value) {
            if (this.primary.length() < this.maxLength) {
                this.primary.append(value);
            }
        }

        public void appendAlternate(char value) {
            if (this.alternate.length() < this.maxLength) {
                this.alternate.append(value);
            }
        }

        public void append(String value) {
            appendPrimary(value);
            appendAlternate(value);
        }

        public void append(String primary2, String alternate2) {
            appendPrimary(primary2);
            appendAlternate(alternate2);
        }

        public void appendPrimary(String value) {
            int addChars = this.maxLength - this.primary.length();
            if (value.length() <= addChars) {
                this.primary.append(value);
            } else {
                this.primary.append(value.substring(0, addChars));
            }
        }

        public void appendAlternate(String value) {
            int addChars = this.maxLength - this.alternate.length();
            if (value.length() <= addChars) {
                this.alternate.append(value);
            } else {
                this.alternate.append(value.substring(0, addChars));
            }
        }

        public String getPrimary() {
            return this.primary.toString();
        }

        public String getAlternate() {
            return this.alternate.toString();
        }

        public boolean isComplete() {
            return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
        }
    }
}
