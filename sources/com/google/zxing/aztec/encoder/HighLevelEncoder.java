package com.google.zxing.aztec.encoder;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class HighLevelEncoder {
    private static final int[][] CHAR_MAP;
    static final int[][] LATCH_TABLE = {new int[]{0, 327708, 327710, 327709, 656318}, new int[]{590318, 0, 327710, 327709, 656318}, new int[]{262158, 590300, 0, 590301, 932798}, new int[]{327709, 327708, 656318, 0, 327710}, new int[]{327711, 656380, 656382, 656381, 0}};
    static final int MODE_DIGIT = 2;
    static final int MODE_LOWER = 1;
    static final int MODE_MIXED = 3;
    static final String[] MODE_NAMES = {"UPPER", "LOWER", "DIGIT", "MIXED", "PUNCT"};
    static final int MODE_PUNCT = 4;
    static final int MODE_UPPER = 0;
    static final int[][] SHIFT_TABLE;
    private final byte[] text;

    static {
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{5, 256});
        CHAR_MAP = iArr;
        iArr[0][32] = 1;
        for (int c = 65; c <= 90; c++) {
            CHAR_MAP[0][c] = (c - 65) + 2;
        }
        CHAR_MAP[1][32] = 1;
        for (int c2 = 97; c2 <= 122; c2++) {
            CHAR_MAP[1][c2] = (c2 - 97) + 2;
        }
        CHAR_MAP[2][32] = 1;
        for (int c3 = 48; c3 <= 57; c3++) {
            CHAR_MAP[2][c3] = (c3 - 48) + 2;
        }
        CHAR_MAP[2][44] = 12;
        CHAR_MAP[2][46] = 13;
        int[] mixedTable = {0, 32, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 27, 28, 29, 30, 31, 64, 92, 94, 95, 96, 124, 126, 127};
        for (int i = 0; i < 28; i++) {
            CHAR_MAP[3][mixedTable[i]] = i;
        }
        int[] punctTable = {0, 13, 0, 0, 0, 0, 33, 39, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 58, 59, 60, 61, 62, 63, 91, 93, 123, 125};
        for (int i2 = 0; i2 < 31; i2++) {
            if (punctTable[i2] > 0) {
                CHAR_MAP[4][punctTable[i2]] = i2;
            }
        }
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{6, 6});
        SHIFT_TABLE = iArr2;
        for (int[] fill : iArr2) {
            Arrays.fill(fill, -1);
        }
        SHIFT_TABLE[0][4] = 0;
        SHIFT_TABLE[1][4] = 0;
        SHIFT_TABLE[1][0] = 28;
        SHIFT_TABLE[3][4] = 0;
        SHIFT_TABLE[2][4] = 0;
        SHIFT_TABLE[2][0] = 15;
    }

    public HighLevelEncoder(byte[] text2) {
        this.text = text2;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v2, types: [byte] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.common.BitArray encode() {
        /*
            r8 = this;
            r7 = 32
            r4 = 0
            com.google.zxing.aztec.encoder.State r5 = com.google.zxing.aztec.encoder.State.INITIAL_STATE
            java.util.List r3 = java.util.Collections.singletonList(r5)
            r0 = 0
        L_0x000a:
            byte[] r5 = r8.text
            int r5 = r5.length
            if (r0 >= r5) goto L_0x0050
            int r5 = r0 + 1
            byte[] r6 = r8.text
            int r6 = r6.length
            if (r5 >= r6) goto L_0x002f
            byte[] r5 = r8.text
            int r6 = r0 + 1
            byte r1 = r5[r6]
        L_0x001c:
            byte[] r5 = r8.text
            byte r5 = r5[r0]
            switch(r5) {
                case 13: goto L_0x0031;
                case 44: goto L_0x003f;
                case 46: goto L_0x0039;
                case 58: goto L_0x0045;
                default: goto L_0x0023;
            }
        L_0x0023:
            r2 = 0
        L_0x0024:
            if (r2 <= 0) goto L_0x004b
            java.util.Collection r3 = updateStateListForPair(r3, r0, r2)
            int r0 = r0 + 1
        L_0x002c:
            int r0 = r0 + 1
            goto L_0x000a
        L_0x002f:
            r1 = r4
            goto L_0x001c
        L_0x0031:
            r5 = 10
            if (r1 != r5) goto L_0x0037
            r2 = 2
        L_0x0036:
            goto L_0x0024
        L_0x0037:
            r2 = r4
            goto L_0x0036
        L_0x0039:
            if (r1 != r7) goto L_0x003d
            r2 = 3
        L_0x003c:
            goto L_0x0024
        L_0x003d:
            r2 = r4
            goto L_0x003c
        L_0x003f:
            if (r1 != r7) goto L_0x0043
            r2 = 4
        L_0x0042:
            goto L_0x0024
        L_0x0043:
            r2 = r4
            goto L_0x0042
        L_0x0045:
            if (r1 != r7) goto L_0x0049
            r2 = 5
        L_0x0048:
            goto L_0x0024
        L_0x0049:
            r2 = r4
            goto L_0x0048
        L_0x004b:
            java.util.Collection r3 = r8.updateStateListForChar(r3, r0)
            goto L_0x002c
        L_0x0050:
            com.google.zxing.aztec.encoder.HighLevelEncoder$1 r4 = new com.google.zxing.aztec.encoder.HighLevelEncoder$1
            r4.<init>()
            java.lang.Object r4 = java.util.Collections.min(r3, r4)
            com.google.zxing.aztec.encoder.State r4 = (com.google.zxing.aztec.encoder.State) r4
            byte[] r5 = r8.text
            com.google.zxing.common.BitArray r4 = r4.toBitArray(r5)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.aztec.encoder.HighLevelEncoder.encode():com.google.zxing.common.BitArray");
    }

    private Collection<State> updateStateListForChar(Iterable<State> states, int index) {
        Collection<State> result = new LinkedList<>();
        for (State state : states) {
            updateStateForChar(state, index, result);
        }
        return simplifyStates(result);
    }

    private void updateStateForChar(State state, int index, Collection<State> result) {
        char ch = (char) (this.text[index] & OnReminderListener.RET_FULL);
        boolean charInCurrentTable = CHAR_MAP[state.getMode()][ch] > 0;
        State stateNoBinary = null;
        for (int mode = 0; mode <= 4; mode++) {
            int charInMode = CHAR_MAP[mode][ch];
            if (charInMode > 0) {
                if (stateNoBinary == null) {
                    stateNoBinary = state.endBinaryShift(index);
                }
                if (!charInCurrentTable || mode == state.getMode() || mode == 2) {
                    result.add(stateNoBinary.latchAndAppend(mode, charInMode));
                }
                if (!charInCurrentTable && SHIFT_TABLE[state.getMode()][mode] >= 0) {
                    result.add(stateNoBinary.shiftAndAppend(mode, charInMode));
                }
            }
        }
        if (state.getBinaryShiftByteCount() > 0 || CHAR_MAP[state.getMode()][ch] == 0) {
            result.add(state.addBinaryShiftChar(index));
        }
    }

    private static Collection<State> updateStateListForPair(Iterable<State> states, int index, int pairCode) {
        Collection<State> result = new LinkedList<>();
        for (State updateStateForPair : states) {
            updateStateForPair(updateStateForPair, index, pairCode, result);
        }
        return simplifyStates(result);
    }

    private static void updateStateForPair(State state, int index, int pairCode, Collection<State> result) {
        State stateNoBinary = state.endBinaryShift(index);
        result.add(stateNoBinary.latchAndAppend(4, pairCode));
        if (state.getMode() != 4) {
            result.add(stateNoBinary.shiftAndAppend(4, pairCode));
        }
        if (pairCode == 3 || pairCode == 4) {
            result.add(stateNoBinary.latchAndAppend(2, 16 - pairCode).latchAndAppend(2, 1));
        }
        if (state.getBinaryShiftByteCount() > 0) {
            result.add(state.addBinaryShiftChar(index).addBinaryShiftChar(index + 1));
        }
    }

    private static Collection<State> simplifyStates(Iterable<State> states) {
        List<State> result = new LinkedList<>();
        for (State newState : states) {
            boolean add = true;
            Iterator<State> iterator = result.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                State oldState = iterator.next();
                if (oldState.isBetterThanOrEqualTo(newState)) {
                    add = false;
                    break;
                } else if (newState.isBetterThanOrEqualTo(oldState)) {
                    iterator.remove();
                }
            }
            if (add) {
                result.add(newState);
            }
        }
        return result;
    }
}
