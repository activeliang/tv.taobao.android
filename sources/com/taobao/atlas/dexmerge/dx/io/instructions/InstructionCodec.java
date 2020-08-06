package com.taobao.atlas.dexmerge.dx.io.instructions;

import android.support.v4.view.InputDeviceCompat;
import com.taobao.atlas.dex.DexException2;
import com.taobao.atlas.dexmerge.dx.io.IndexType;
import com.taobao.atlas.dexmerge.dx.io.OpcodeInfo;
import com.taobao.atlas.dexmerge.dx.util.Hex;
import java.io.EOFException;

public enum InstructionCodec {
    FORMAT_00X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, opcodeUnit, 0, (IndexType) null, 0, 0);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit());
        }
    },
    FORMAT_10X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, (long) InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit());
        }
    },
    FORMAT_12X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, 0, InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcodeUnit(), InstructionCodec.makeByte(insn.getA(), insn.getB())));
        }
    },
    FORMAT_11N {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new OneRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, (long) ((InstructionCodec.nibble3(opcodeUnit) << 28) >> 28), InstructionCodec.nibble2(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcodeUnit(), InstructionCodec.makeByte(insn.getA(), insn.getLiteralNibble())));
        }
    },
    FORMAT_11X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new OneRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, 0, InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()));
        }
    },
    FORMAT_10T {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, (in.cursor() - 1) + ((byte) InstructionCodec.byte1(opcodeUnit)), 0);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getTargetByte(out.cursor())));
        }
    },
    FORMAT_20T {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, (in.cursor() - 1) + ((short) in.read()), (long) InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit(), insn.getTargetUnit(out.cursor()));
        }
    },
    FORMAT_20BC {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), in.read(), IndexType.VARIES, 0, (long) InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getLiteralByte()), insn.getIndexUnit());
        }
    },
    FORMAT_22X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, 0, InstructionCodec.byte1(opcodeUnit), in.read());
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getBUnit());
        }
    },
    FORMAT_21T {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new OneRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, (in.cursor() - 1) + ((short) in.read()), 0, InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getTargetUnit(out.cursor()));
        }
    },
    FORMAT_21S {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new OneRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, (long) ((short) in.read()), InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getLiteralUnit());
        }
    },
    FORMAT_21H {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType) null, 0, ((long) ((short) in.read())) << (opcode == 21 ? 16 : '0'), InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            int opcode = insn.getOpcode();
            out.write(InstructionCodec.codeUnit(opcode, insn.getA()), (short) ((int) (insn.getLiteral() >> (opcode == 21 ? 16 : 48))));
        }
    },
    FORMAT_21C {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            return new OneRegisterDecodedInstruction(this, opcode, in.read(), OpcodeInfo.getIndexType(opcode), 0, 0, InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getIndexUnit());
        }
    },
    FORMAT_23X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int bc = in.read();
            return new ThreeRegisterDecodedInstruction(this, opcode, 0, (IndexType) null, 0, 0, a, InstructionCodec.byte0(bc), InstructionCodec.byte1(bc));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.codeUnit(insn.getB(), insn.getC()));
        }
    },
    FORMAT_22B {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int bc = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType) null, 0, (long) ((byte) InstructionCodec.byte1(bc)), a, InstructionCodec.byte0(bc));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.codeUnit(insn.getB(), insn.getLiteralByte()));
        }
    },
    FORMAT_22T {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, (in.cursor() - 1) + ((short) in.read()), 0, InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getTargetUnit(out.cursor()));
        }
    },
    FORMAT_22S {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, (long) ((short) in.read()), InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getLiteralUnit());
        }
    },
    FORMAT_22C {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            return new TwoRegisterDecodedInstruction(this, opcode, in.read(), OpcodeInfo.getIndexType(opcode), 0, 0, InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getIndexUnit());
        }
    },
    FORMAT_22CS {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), in.read(), IndexType.FIELD_OFFSET, 0, 0, InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getIndexUnit());
        }
    },
    FORMAT_30T {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, (in.cursor() - 1) + in.readInt(), (long) InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTarget(out.cursor());
            out.write(insn.getOpcodeUnit(), InstructionCodec.unit0(relativeTarget), InstructionCodec.unit1(relativeTarget));
        }
    },
    FORMAT_32X {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, (long) InstructionCodec.byte1(opcodeUnit), in.read(), in.read());
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit(), insn.getAUnit(), insn.getBUnit());
        }
    },
    FORMAT_31I {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new OneRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, (long) in.readInt(), InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            int literal = insn.getLiteralInt();
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(literal), InstructionCodec.unit1(literal));
        }
    },
    FORMAT_31T {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int target = baseAddress + in.readInt();
            switch (opcode) {
                case 43:
                case 44:
                    in.setBaseAddress(target, baseAddress);
                    break;
            }
            return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType) null, target, 0, a);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTarget(out.cursor());
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(relativeTarget), InstructionCodec.unit1(relativeTarget));
        }
    },
    FORMAT_31C {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            return new OneRegisterDecodedInstruction(this, opcode, in.readInt(), OpcodeInfo.getIndexType(opcode), 0, 0, InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(index), InstructionCodec.unit1(index));
        }
    },
    FORMAT_35C {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterList(this, opcodeUnit, in);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterList(insn, out);
        }
    },
    FORMAT_35MS {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterList(this, opcodeUnit, in);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterList(insn, out);
        }
    },
    FORMAT_35MI {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterList(this, opcodeUnit, in);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterList(insn, out);
        }
    },
    FORMAT_3RC {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterRange(this, opcodeUnit, in);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterRange(insn, out);
        }
    },
    FORMAT_3RMS {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterRange(this, opcodeUnit, in);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterRange(insn, out);
        }
    },
    FORMAT_3RMI {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterRange(this, opcodeUnit, in);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterRange(insn, out);
        }
    },
    FORMAT_51L {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new OneRegisterDecodedInstruction(this, InstructionCodec.byte0(opcodeUnit), 0, (IndexType) null, 0, in.readLong(), InstructionCodec.byte1(opcodeUnit));
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            long literal = insn.getLiteral();
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(literal), InstructionCodec.unit1(literal), InstructionCodec.unit2(literal), InstructionCodec.unit3(literal));
        }
    },
    FORMAT_PACKED_SWITCH_PAYLOAD {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.baseAddressForCursor() - 1;
            int size = in.read();
            int firstKey = in.readInt();
            int[] targets = new int[size];
            for (int i = 0; i < size; i++) {
                targets[i] = in.readInt() + baseAddress;
            }
            return new PackedSwitchPayloadDecodedInstruction(this, opcodeUnit, firstKey, targets);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            PackedSwitchPayloadDecodedInstruction payload = (PackedSwitchPayloadDecodedInstruction) insn;
            int[] targets = payload.getTargets();
            int baseAddress = out.baseAddressForCursor();
            out.write(payload.getOpcodeUnit());
            out.write(InstructionCodec.asUnsignedUnit(targets.length));
            out.writeInt(payload.getFirstKey());
            for (int target : targets) {
                out.writeInt(target - baseAddress);
            }
        }
    },
    FORMAT_SPARSE_SWITCH_PAYLOAD {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.baseAddressForCursor() - 1;
            int size = in.read();
            int[] keys = new int[size];
            int[] targets = new int[size];
            for (int i = 0; i < size; i++) {
                keys[i] = in.readInt();
            }
            for (int i2 = 0; i2 < size; i2++) {
                targets[i2] = in.readInt() + baseAddress;
            }
            return new SparseSwitchPayloadDecodedInstruction(this, opcodeUnit, keys, targets);
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            SparseSwitchPayloadDecodedInstruction payload = (SparseSwitchPayloadDecodedInstruction) insn;
            int[] keys = payload.getKeys();
            int[] targets = payload.getTargets();
            int baseAddress = out.baseAddressForCursor();
            out.write(payload.getOpcodeUnit());
            out.write(InstructionCodec.asUnsignedUnit(targets.length));
            for (int key : keys) {
                out.writeInt(key);
            }
            for (int target : targets) {
                out.writeInt(target - baseAddress);
            }
        }
    },
    FORMAT_FILL_ARRAY_DATA_PAYLOAD {
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int elementWidth = in.read();
            int size = in.readInt();
            switch (elementWidth) {
                case 1:
                    byte[] array = new byte[size];
                    boolean even = true;
                    int i = 0;
                    int value = 0;
                    while (i < size) {
                        if (even) {
                            value = in.read();
                        }
                        array[i] = (byte) (value & 255);
                        value >>= 8;
                        i++;
                        even = !even;
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec) this, opcodeUnit, array);
                case 2:
                    short[] array2 = new short[size];
                    for (int i2 = 0; i2 < size; i2++) {
                        array2[i2] = (short) in.read();
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec) this, opcodeUnit, array2);
                case 4:
                    int[] array3 = new int[size];
                    for (int i3 = 0; i3 < size; i3++) {
                        array3[i3] = in.readInt();
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec) this, opcodeUnit, array3);
                case 8:
                    long[] array4 = new long[size];
                    for (int i4 = 0; i4 < size; i4++) {
                        array4[i4] = in.readLong();
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec) this, opcodeUnit, array4);
                default:
                    throw new DexException2("bogus element_width: " + Hex.u2(elementWidth));
            }
        }

        public void encode(DecodedInstruction insn, CodeOutput out) {
            FillArrayDataPayloadDecodedInstruction payload = (FillArrayDataPayloadDecodedInstruction) insn;
            short elementWidth = payload.getElementWidthUnit();
            Object data = payload.getData();
            out.write(payload.getOpcodeUnit());
            out.write(elementWidth);
            out.writeInt(payload.getSize());
            switch (elementWidth) {
                case 1:
                    out.write((byte[]) (byte[]) data);
                    return;
                case 2:
                    out.write((short[]) (short[]) data);
                    return;
                case 4:
                    out.write((int[]) (int[]) data);
                    return;
                case 8:
                    out.write((long[]) (long[]) data);
                    return;
                default:
                    throw new DexException2("bogus element_width: " + Hex.u2(elementWidth));
            }
        }
    };

    public abstract DecodedInstruction decode(int i, CodeInput codeInput) throws EOFException;

    public abstract void encode(DecodedInstruction decodedInstruction, CodeOutput codeOutput);

    /* access modifiers changed from: private */
    public static DecodedInstruction decodeRegisterList(InstructionCodec format, int opcodeUnit, CodeInput in) throws EOFException {
        int opcode = byte0(opcodeUnit);
        int e = nibble2(opcodeUnit);
        int registerCount = nibble3(opcodeUnit);
        int index = in.read();
        int abcd = in.read();
        int a = nibble0(abcd);
        int b = nibble1(abcd);
        int c = nibble2(abcd);
        int d = nibble3(abcd);
        IndexType indexType = OpcodeInfo.getIndexType(opcode);
        switch (registerCount) {
            case 0:
                return new ZeroRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0);
            case 1:
                return new OneRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0, a);
            case 2:
                return new TwoRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0, a, b);
            case 3:
                return new ThreeRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0, a, b, c);
            case 4:
                return new FourRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0, a, b, c, d);
            case 5:
                return new FiveRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0, a, b, c, d, e);
            default:
                throw new DexException2("bogus registerCount: " + Hex.uNibble(registerCount));
        }
    }

    /* access modifiers changed from: private */
    public static void encodeRegisterList(DecodedInstruction insn, CodeOutput out) {
        out.write(codeUnit(insn.getOpcode(), makeByte(insn.getE(), insn.getRegisterCount())), insn.getIndexUnit(), codeUnit(insn.getA(), insn.getB(), insn.getC(), insn.getD()));
    }

    /* access modifiers changed from: private */
    public static DecodedInstruction decodeRegisterRange(InstructionCodec format, int opcodeUnit, CodeInput in) throws EOFException {
        int opcode = byte0(opcodeUnit);
        int registerCount = byte1(opcodeUnit);
        return new RegisterRangeDecodedInstruction(format, opcode, in.read(), OpcodeInfo.getIndexType(opcode), 0, 0, in.read(), registerCount);
    }

    /* access modifiers changed from: private */
    public static void encodeRegisterRange(DecodedInstruction insn, CodeOutput out) {
        out.write(codeUnit(insn.getOpcode(), insn.getRegisterCount()), insn.getIndexUnit(), insn.getAUnit());
    }

    /* access modifiers changed from: private */
    public static short codeUnit(int lowByte, int highByte) {
        if ((lowByte & InputDeviceCompat.SOURCE_ANY) != 0) {
            throw new IllegalArgumentException("bogus lowByte");
        } else if ((highByte & InputDeviceCompat.SOURCE_ANY) == 0) {
            return (short) ((highByte << 8) | lowByte);
        } else {
            throw new IllegalArgumentException("bogus highByte");
        }
    }

    private static short codeUnit(int nibble0, int nibble1, int nibble2, int nibble3) {
        if ((nibble0 & -16) != 0) {
            throw new IllegalArgumentException("bogus nibble0");
        } else if ((nibble1 & -16) != 0) {
            throw new IllegalArgumentException("bogus nibble1");
        } else if ((nibble2 & -16) != 0) {
            throw new IllegalArgumentException("bogus nibble2");
        } else if ((nibble3 & -16) == 0) {
            return (short) ((nibble1 << 4) | nibble0 | (nibble2 << 8) | (nibble3 << 12));
        } else {
            throw new IllegalArgumentException("bogus nibble3");
        }
    }

    /* access modifiers changed from: private */
    public static int makeByte(int lowNibble, int highNibble) {
        if ((lowNibble & -16) != 0) {
            throw new IllegalArgumentException("bogus lowNibble");
        } else if ((highNibble & -16) == 0) {
            return (highNibble << 4) | lowNibble;
        } else {
            throw new IllegalArgumentException("bogus highNibble");
        }
    }

    /* access modifiers changed from: private */
    public static short asUnsignedUnit(int value) {
        if ((-65536 & value) == 0) {
            return (short) value;
        }
        throw new IllegalArgumentException("bogus unsigned code unit");
    }

    /* access modifiers changed from: private */
    public static short unit0(int value) {
        return (short) value;
    }

    /* access modifiers changed from: private */
    public static short unit1(int value) {
        return (short) (value >> 16);
    }

    /* access modifiers changed from: private */
    public static short unit0(long value) {
        return (short) ((int) value);
    }

    /* access modifiers changed from: private */
    public static short unit1(long value) {
        return (short) ((int) (value >> 16));
    }

    /* access modifiers changed from: private */
    public static short unit2(long value) {
        return (short) ((int) (value >> 32));
    }

    /* access modifiers changed from: private */
    public static short unit3(long value) {
        return (short) ((int) (value >> 48));
    }

    /* access modifiers changed from: private */
    public static int byte0(int value) {
        return value & 255;
    }

    /* access modifiers changed from: private */
    public static int byte1(int value) {
        return (value >> 8) & 255;
    }

    private static int byte2(int value) {
        return (value >> 16) & 255;
    }

    private static int byte3(int value) {
        return value >>> 24;
    }

    private static int nibble0(int value) {
        return value & 15;
    }

    private static int nibble1(int value) {
        return (value >> 4) & 15;
    }

    /* access modifiers changed from: private */
    public static int nibble2(int value) {
        return (value >> 8) & 15;
    }

    /* access modifiers changed from: private */
    public static int nibble3(int value) {
        return (value >> 12) & 15;
    }
}
