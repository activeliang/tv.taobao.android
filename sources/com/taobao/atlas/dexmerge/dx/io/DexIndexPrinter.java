package com.taobao.atlas.dexmerge.dx.io;

import android.util.Log;
import com.taobao.atlas.dex.ClassDef;
import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.FieldId;
import com.taobao.atlas.dex.MethodId;
import com.taobao.atlas.dex.ProtoId;
import com.taobao.atlas.dex.TableOfContents;
import java.io.File;
import java.io.IOException;

public final class DexIndexPrinter {
    private static final String TAG = "DexIndexPrinter";
    private final Dex dex;
    private final TableOfContents tableOfContents = this.dex.getTableOfContents();

    public DexIndexPrinter(File file) throws IOException {
        this.dex = new Dex(file);
    }

    private void printMap() {
        for (TableOfContents.Section section : this.tableOfContents.sections) {
            if (section.off != -1) {
                Log.d(TAG, "section " + Integer.toHexString(section.type) + " off=" + Integer.toHexString(section.off) + " size=" + Integer.toHexString(section.size) + " byteCount=" + Integer.toHexString(section.byteCount));
            }
        }
    }

    private void printStrings() throws IOException {
        int index = 0;
        for (String string : this.dex.strings()) {
            Log.d(TAG, "string " + index + ": " + string);
            index++;
        }
    }

    private void printTypeIds() throws IOException {
        int index = 0;
        for (Integer type : this.dex.typeIds()) {
            Log.d(TAG, "type " + index + ": " + this.dex.strings().get(type.intValue()));
            index++;
        }
    }

    private void printProtoIds() throws IOException {
        int index = 0;
        for (ProtoId protoId : this.dex.protoIds()) {
            Log.d(TAG, "proto " + index + ": " + protoId);
            index++;
        }
    }

    private void printFieldIds() throws IOException {
        int index = 0;
        for (FieldId fieldId : this.dex.fieldIds()) {
            Log.d(TAG, "field " + index + ": " + fieldId);
            index++;
        }
    }

    private void printMethodIds() throws IOException {
        int index = 0;
        for (MethodId methodId : this.dex.methodIds()) {
            Log.d(TAG, "methodId " + index + ": " + methodId);
            index++;
        }
    }

    private void printTypeLists() throws IOException {
        if (this.tableOfContents.typeLists.off == -1) {
            Log.d(TAG, "No type lists");
            return;
        }
        Dex.Section in = this.dex.open(this.tableOfContents.typeLists.off);
        for (int i = 0; i < this.tableOfContents.typeLists.size; i++) {
            int size = in.readInt();
            Log.d(TAG, "Type list i=" + i + ", size=" + size + ", elements=");
            for (int t = 0; t < size; t++) {
                Log.d(TAG, " " + this.dex.typeNames().get(in.readShort()));
            }
            if (size % 2 == 1) {
                in.readShort();
            }
        }
    }

    private void printClassDefs() {
        int index = 0;
        for (ClassDef classDef : this.dex.classDefs()) {
            Log.d(TAG, "class def " + index + ": " + classDef);
            index++;
        }
    }

    public static void main(String[] args) throws IOException {
        DexIndexPrinter indexPrinter = new DexIndexPrinter(new File(args[0]));
        indexPrinter.printMap();
        indexPrinter.printStrings();
        indexPrinter.printTypeIds();
        indexPrinter.printProtoIds();
        indexPrinter.printFieldIds();
        indexPrinter.printMethodIds();
        indexPrinter.printTypeLists();
        indexPrinter.printClassDefs();
    }
}
