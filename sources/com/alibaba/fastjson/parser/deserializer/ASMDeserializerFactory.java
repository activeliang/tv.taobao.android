package com.alibaba.fastjson.parser.deserializer;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public class ASMDeserializerFactory implements Opcodes {
    static final String DefaultJSONParser = ASMUtils.type(DefaultJSONParser.class);
    static final String JSONLexerBase = ASMUtils.type(JSONLexerBase.class);
    public final ASMClassLoader classLoader;
    protected final AtomicLong seed = new AtomicLong();

    public ASMDeserializerFactory(ClassLoader parentClassLoader) {
        this.classLoader = parentClassLoader instanceof ASMClassLoader ? (ASMClassLoader) parentClassLoader : new ASMClassLoader(parentClassLoader);
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig config, JavaBeanInfo beanInfo) throws Exception {
        Class<?> clazz = beanInfo.clazz;
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("not support type :" + clazz.getName());
        }
        String className = "FastjsonASMDeserializer_" + this.seed.incrementAndGet() + "_" + clazz.getSimpleName();
        String packageName = ASMDeserializerFactory.class.getPackage().getName();
        String classNameType = packageName.replace('.', '/') + WVNativeCallbackUtil.SEPERATER + className;
        String classNameFull = packageName + "." + className;
        ClassWriter cw = new ClassWriter();
        cw.visit(49, 33, classNameType, ASMUtils.type(JavaBeanDeserializer.class), (String[]) null);
        _init(cw, new Context(classNameType, config, beanInfo, 3));
        _createInstance(cw, new Context(classNameType, config, beanInfo, 3));
        _deserialze(cw, new Context(classNameType, config, beanInfo, 5));
        _deserialzeArrayMapping(cw, new Context(classNameType, config, beanInfo, 4));
        byte[] code = cw.toByteArray();
        return (ObjectDeserializer) defineClassPublic(classNameFull, code, 0, code.length).getConstructor(new Class[]{ParserConfig.class, JavaBeanInfo.class}).newInstance(new Object[]{config, beanInfo});
    }

    private Class<?> defineClassPublic(String name, byte[] b, int off, int len) {
        return this.classLoader.defineClassPublic(name, b, off, len);
    }

    private void _setFlag(MethodVisitor mw, Context context, int i) {
        String varName = "_asm_flag_" + (i / 32);
        mw.visitVarInsn(21, context.var(varName));
        mw.visitLdcInsn(Integer.valueOf(1 << i));
        mw.visitInsn(128);
        mw.visitVarInsn(54, context.var(varName));
    }

    private void _isFlag(MethodVisitor mw, Context context, int i, Label label) {
        mw.visitVarInsn(21, context.var("_asm_flag_" + (i / 32)));
        mw.visitLdcInsn(Integer.valueOf(1 << i));
        mw.visitInsn(126);
        mw.visitJumpInsn(153, label);
    }

    private void _deserialzeArrayMapping(ClassWriter cw, Context context) {
        MethodWriter methodWriter = new MethodWriter(cw, 1, "deserialzeArrayMapping", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", (String) null, (String[]) null);
        defineVarLexer(context, methodWriter);
        _createInstance(context, (MethodVisitor) methodWriter);
        FieldInfo[] sortedFieldInfoList = context.beanInfo.sortedFields;
        int fieldListSize = sortedFieldInfoList.length;
        int i = 0;
        while (i < fieldListSize) {
            boolean last = i == fieldListSize + -1;
            char seperator = last ? ']' : ',';
            FieldInfo fieldInfo = sortedFieldInfoList[i];
            Class<?> fieldClass = fieldInfo.fieldClass;
            Type fieldType = fieldInfo.fieldType;
            if (fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Integer.TYPE) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanInt", "(C)I");
                methodWriter.visitVarInsn(54, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == Long.TYPE) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanLong", "(C)J");
                methodWriter.visitVarInsn(55, context.var(fieldInfo.name + "_asm", 2));
            } else if (fieldClass == Boolean.TYPE) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanBoolean", "(C)Z");
                methodWriter.visitVarInsn(54, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == Float.TYPE) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFloat", "(C)F");
                methodWriter.visitVarInsn(56, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == Double.TYPE) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanDouble", "(C)D");
                methodWriter.visitVarInsn(57, context.var(fieldInfo.name + "_asm", 2));
            } else if (fieldClass == Character.TYPE) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanString", "(C)Ljava/lang/String;");
                methodWriter.visitInsn(3);
                methodWriter.visitMethodInsn(182, "java/lang/String", "charAt", "(I)C");
                methodWriter.visitVarInsn(54, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass == String.class) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanString", "(C)Ljava/lang/String;");
                methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
            } else if (fieldClass.isEnum()) {
                Label enumNumIf_ = new Label();
                Label enumNumErr_ = new Label();
                Label enumStore_ = new Label();
                Label enumQuote_ = new Label();
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitMethodInsn(182, JSONLexerBase, "getCurrent", "()C");
                methodWriter.visitInsn(89);
                methodWriter.visitVarInsn(54, context.var("ch"));
                methodWriter.visitLdcInsn(110);
                methodWriter.visitJumpInsn(159, enumQuote_);
                methodWriter.visitVarInsn(21, context.var("ch"));
                methodWriter.visitLdcInsn(34);
                methodWriter.visitJumpInsn(160, enumNumIf_);
                methodWriter.visitLabel(enumQuote_);
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldClass)));
                methodWriter.visitVarInsn(25, 1);
                methodWriter.visitMethodInsn(182, DefaultJSONParser, "getSymbolTable", "()" + ASMUtils.desc((Class<?>) SymbolTable.class));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanEnum", "(Ljava/lang/Class;" + ASMUtils.desc((Class<?>) SymbolTable.class) + "C)Ljava/lang/Enum;");
                methodWriter.visitJumpInsn(167, enumStore_);
                methodWriter.visitLabel(enumNumIf_);
                methodWriter.visitVarInsn(21, context.var("ch"));
                methodWriter.visitLdcInsn(48);
                methodWriter.visitJumpInsn(161, enumNumErr_);
                methodWriter.visitVarInsn(21, context.var("ch"));
                methodWriter.visitLdcInsn(57);
                methodWriter.visitJumpInsn(163, enumNumErr_);
                _getFieldDeser(context, methodWriter, fieldInfo);
                methodWriter.visitTypeInsn(192, ASMUtils.type(EnumDeserializer.class));
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "scanInt", "(C)I");
                methodWriter.visitMethodInsn(182, ASMUtils.type(EnumDeserializer.class), "valueOf", "(I)Ljava/lang/Enum;");
                methodWriter.visitJumpInsn(167, enumStore_);
                methodWriter.visitLabel(enumNumErr_);
                methodWriter.visitVarInsn(25, 0);
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(16, seperator);
                methodWriter.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "scanEnum", "(L" + JSONLexerBase + ";C)Ljava/lang/Enum;");
                methodWriter.visitLabel(enumStore_);
                methodWriter.visitTypeInsn(192, ASMUtils.type(fieldClass));
                methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class<?> itemClass = TypeUtils.getCollectionItemClass(fieldType);
                if (itemClass == String.class) {
                    if (fieldClass == List.class || fieldClass == Collections.class || fieldClass == ArrayList.class) {
                        methodWriter.visitTypeInsn(187, ASMUtils.type(ArrayList.class));
                        methodWriter.visitInsn(89);
                        methodWriter.visitMethodInsn(183, ASMUtils.type(ArrayList.class), "<init>", "()V");
                    } else {
                        methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldClass)));
                        methodWriter.visitMethodInsn(184, ASMUtils.type(TypeUtils.class), "createCollection", "(Ljava/lang/Class;)Ljava/util/Collection;");
                    }
                    methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
                    methodWriter.visitVarInsn(16, seperator);
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanStringArray", "(Ljava/util/Collection;C)V");
                    Label valueNullEnd_ = new Label();
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
                    methodWriter.visitLdcInsn(5);
                    methodWriter.visitJumpInsn(160, valueNullEnd_);
                    methodWriter.visitInsn(1);
                    methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
                    methodWriter.visitLabel(valueNullEnd_);
                } else {
                    Label notError_ = new Label();
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "token", "()I");
                    methodWriter.visitVarInsn(54, context.var("token"));
                    methodWriter.visitVarInsn(21, context.var("token"));
                    methodWriter.visitLdcInsn(Integer.valueOf(i == 0 ? 14 : 16));
                    methodWriter.visitJumpInsn(159, notError_);
                    methodWriter.visitVarInsn(25, 1);
                    methodWriter.visitVarInsn(21, context.var("token"));
                    methodWriter.visitMethodInsn(182, DefaultJSONParser, "throwException", "(I)V");
                    methodWriter.visitLabel(notError_);
                    Label quickElse_ = new Label();
                    Label quickEnd_ = new Label();
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "getCurrent", "()C");
                    methodWriter.visitVarInsn(16, 91);
                    methodWriter.visitJumpInsn(160, quickElse_);
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "next", "()C");
                    methodWriter.visitInsn(87);
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitLdcInsn(14);
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
                    methodWriter.visitJumpInsn(167, quickEnd_);
                    methodWriter.visitLabel(quickElse_);
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitLdcInsn(14);
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
                    methodWriter.visitLabel(quickEnd_);
                    _newCollection(methodWriter, fieldClass, i, false);
                    methodWriter.visitInsn(89);
                    methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
                    _getCollectionFieldItemDeser(context, methodWriter, fieldInfo, itemClass);
                    methodWriter.visitVarInsn(25, 1);
                    methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(itemClass)));
                    methodWriter.visitVarInsn(25, 3);
                    methodWriter.visitMethodInsn(184, ASMUtils.type(JavaBeanDeserializer.class), "parseArray", "(Ljava/util/Collection;" + ASMUtils.desc((Class<?>) ObjectDeserializer.class) + "L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)V");
                }
            } else if (fieldClass.isArray()) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitLdcInsn(14);
                methodWriter.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
                methodWriter.visitVarInsn(25, 1);
                methodWriter.visitVarInsn(25, 0);
                methodWriter.visitLdcInsn(Integer.valueOf(i));
                methodWriter.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
                methodWriter.visitMethodInsn(182, DefaultJSONParser, "parseObject", "(Ljava/lang/reflect/Type;)Ljava/lang/Object;");
                methodWriter.visitTypeInsn(192, ASMUtils.type(fieldClass));
                methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
            } else {
                Label objElseIf_ = new Label();
                Label objEndIf_ = new Label();
                if (fieldClass == Date.class) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "getCurrent", "()C");
                    methodWriter.visitLdcInsn(49);
                    methodWriter.visitJumpInsn(160, objElseIf_);
                    methodWriter.visitTypeInsn(187, ASMUtils.type(Date.class));
                    methodWriter.visitInsn(89);
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(16, seperator);
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanLong", "(C)J");
                    methodWriter.visitMethodInsn(183, ASMUtils.type(Date.class), "<init>", "(J)V");
                    methodWriter.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
                    methodWriter.visitJumpInsn(167, objEndIf_);
                }
                methodWriter.visitLabel(objElseIf_);
                _quickNextToken(context, methodWriter, 14);
                _deserObject(context, methodWriter, fieldInfo, fieldClass, i);
                methodWriter.visitVarInsn(25, 0);
                methodWriter.visitVarInsn(25, context.var("lexer"));
                if (!last) {
                    methodWriter.visitLdcInsn(16);
                } else {
                    methodWriter.visitLdcInsn(15);
                }
                methodWriter.visitMethodInsn(183, ASMUtils.type(JavaBeanDeserializer.class), "check", "(" + ASMUtils.desc((Class<?>) JSONLexer.class) + "I)V");
                methodWriter.visitLabel(objEndIf_);
            }
            i++;
        }
        _batchSet(context, methodWriter, false);
        Label quickElse_2 = new Label();
        Label quickElseIf_ = new Label();
        Label quickElseIfEOI_ = new Label();
        Label quickEnd_2 = new Label();
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitMethodInsn(182, JSONLexerBase, "getCurrent", "()C");
        methodWriter.visitInsn(89);
        methodWriter.visitVarInsn(54, context.var("ch"));
        methodWriter.visitVarInsn(16, 44);
        methodWriter.visitJumpInsn(160, quickElseIf_);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        methodWriter.visitInsn(87);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitLdcInsn(16);
        methodWriter.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        methodWriter.visitJumpInsn(167, quickEnd_2);
        methodWriter.visitLabel(quickElseIf_);
        methodWriter.visitVarInsn(21, context.var("ch"));
        methodWriter.visitVarInsn(16, 93);
        methodWriter.visitJumpInsn(160, quickElseIfEOI_);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        methodWriter.visitInsn(87);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitLdcInsn(15);
        methodWriter.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        methodWriter.visitJumpInsn(167, quickEnd_2);
        methodWriter.visitLabel(quickElseIfEOI_);
        methodWriter.visitVarInsn(21, context.var("ch"));
        methodWriter.visitVarInsn(16, 26);
        methodWriter.visitJumpInsn(160, quickElse_2);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        methodWriter.visitInsn(87);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitLdcInsn(20);
        methodWriter.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        methodWriter.visitJumpInsn(167, quickEnd_2);
        methodWriter.visitLabel(quickElse_2);
        methodWriter.visitVarInsn(25, context.var("lexer"));
        methodWriter.visitLdcInsn(16);
        methodWriter.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
        methodWriter.visitLabel(quickEnd_2);
        methodWriter.visitVarInsn(25, context.var("instance"));
        methodWriter.visitInsn(176);
        methodWriter.visitMaxs(5, context.variantIndex);
        methodWriter.visitEnd();
    }

    private void _deserialze(ClassWriter cw, Context context) {
        if (context.fieldInfoList.length != 0) {
            FieldInfo[] access$200 = context.fieldInfoList;
            int length = access$200.length;
            int i = 0;
            while (i < length) {
                FieldInfo fieldInfo = access$200[i];
                Class<?> fieldClass = fieldInfo.fieldClass;
                Type fieldType = fieldInfo.fieldType;
                if (fieldClass == Character.TYPE) {
                    return;
                }
                if (!Collection.class.isAssignableFrom(fieldClass) || ((fieldType instanceof ParameterizedType) && (((ParameterizedType) fieldType).getActualTypeArguments()[0] instanceof Class))) {
                    i++;
                } else {
                    return;
                }
            }
            JavaBeanInfo beanInfo = context.beanInfo;
            FieldInfo[] unused = context.fieldInfoList = beanInfo.sortedFields;
            MethodWriter methodWriter = new MethodWriter(cw, 1, "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;I)Ljava/lang/Object;", (String) null, (String[]) null);
            Label reset_ = new Label();
            Label super_ = new Label();
            Label return_ = new Label();
            Label end_ = new Label();
            defineVarLexer(context, methodWriter);
            Label next_ = new Label();
            methodWriter.visitVarInsn(25, context.var("lexer"));
            methodWriter.visitMethodInsn(182, JSONLexerBase, "token", "()I");
            methodWriter.visitLdcInsn(14);
            methodWriter.visitJumpInsn(160, next_);
            if ((beanInfo.parserFeatures & Feature.SupportArrayToBean.mask) == 0) {
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitVarInsn(21, 4);
                methodWriter.visitLdcInsn(Integer.valueOf(Feature.SupportArrayToBean.mask));
                methodWriter.visitMethodInsn(182, JSONLexerBase, "isEnabled", "(II)Z");
                methodWriter.visitJumpInsn(153, next_);
            }
            methodWriter.visitVarInsn(25, 0);
            methodWriter.visitVarInsn(25, 1);
            methodWriter.visitVarInsn(25, 2);
            methodWriter.visitVarInsn(25, 3);
            methodWriter.visitInsn(1);
            methodWriter.visitMethodInsn(183, context.className, "deserialzeArrayMapping", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            methodWriter.visitInsn(176);
            methodWriter.visitLabel(next_);
            methodWriter.visitVarInsn(25, context.var("lexer"));
            methodWriter.visitLdcInsn(Integer.valueOf(Feature.SortFeidFastMatch.mask));
            methodWriter.visitMethodInsn(182, JSONLexerBase, "isEnabled", "(I)Z");
            methodWriter.visitJumpInsn(153, super_);
            methodWriter.visitVarInsn(25, context.var("lexer"));
            methodWriter.visitLdcInsn(context.clazz.getName());
            methodWriter.visitMethodInsn(182, JSONLexerBase, "scanType", "(Ljava/lang/String;)I");
            methodWriter.visitLdcInsn(-1);
            methodWriter.visitJumpInsn(159, super_);
            methodWriter.visitVarInsn(25, 1);
            methodWriter.visitMethodInsn(182, DefaultJSONParser, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
            methodWriter.visitVarInsn(58, context.var("mark_context"));
            methodWriter.visitInsn(3);
            methodWriter.visitVarInsn(54, context.var("matchedCount"));
            _createInstance(context, (MethodVisitor) methodWriter);
            methodWriter.visitVarInsn(25, 1);
            methodWriter.visitMethodInsn(182, DefaultJSONParser, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
            methodWriter.visitVarInsn(58, context.var("context"));
            methodWriter.visitVarInsn(25, 1);
            methodWriter.visitVarInsn(25, context.var("context"));
            methodWriter.visitVarInsn(25, context.var("instance"));
            methodWriter.visitVarInsn(25, 3);
            methodWriter.visitMethodInsn(182, DefaultJSONParser, "setContext", "(" + ASMUtils.desc((Class<?>) ParseContext.class) + "Ljava/lang/Object;Ljava/lang/Object;)" + ASMUtils.desc((Class<?>) ParseContext.class));
            methodWriter.visitVarInsn(58, context.var("childContext"));
            methodWriter.visitVarInsn(25, context.var("lexer"));
            methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
            methodWriter.visitLdcInsn(4);
            methodWriter.visitJumpInsn(159, return_);
            methodWriter.visitInsn(3);
            methodWriter.visitIntInsn(54, context.var("matchStat"));
            int fieldListSize = context.fieldInfoList.length;
            for (int i2 = 0; i2 < fieldListSize; i2 += 32) {
                methodWriter.visitInsn(3);
                methodWriter.visitVarInsn(54, context.var("_asm_flag_" + (i2 / 32)));
            }
            methodWriter.visitVarInsn(25, context.var("lexer"));
            methodWriter.visitLdcInsn(Integer.valueOf(Feature.InitStringFieldAsEmpty.mask));
            methodWriter.visitMethodInsn(182, JSONLexerBase, "isEnabled", "(I)Z");
            methodWriter.visitIntInsn(54, context.var("initStringFieldAsEmpty"));
            for (int i3 = 0; i3 < fieldListSize; i3++) {
                FieldInfo fieldInfo2 = context.fieldInfoList[i3];
                Class<?> fieldClass2 = fieldInfo2.fieldClass;
                if (fieldClass2 == Boolean.TYPE || fieldClass2 == Byte.TYPE || fieldClass2 == Short.TYPE || fieldClass2 == Integer.TYPE) {
                    methodWriter.visitInsn(3);
                    methodWriter.visitVarInsn(54, context.var(fieldInfo2.name + "_asm"));
                } else if (fieldClass2 == Long.TYPE) {
                    methodWriter.visitInsn(9);
                    methodWriter.visitVarInsn(55, context.var(fieldInfo2.name + "_asm", 2));
                } else if (fieldClass2 == Float.TYPE) {
                    methodWriter.visitInsn(11);
                    methodWriter.visitVarInsn(56, context.var(fieldInfo2.name + "_asm"));
                } else if (fieldClass2 == Double.TYPE) {
                    methodWriter.visitInsn(14);
                    methodWriter.visitVarInsn(57, context.var(fieldInfo2.name + "_asm", 2));
                } else {
                    if (fieldClass2 == String.class) {
                        Label flagEnd_ = new Label();
                        Label flagElse_ = new Label();
                        methodWriter.visitVarInsn(21, context.var("initStringFieldAsEmpty"));
                        methodWriter.visitJumpInsn(153, flagElse_);
                        _setFlag(methodWriter, context, i3);
                        methodWriter.visitVarInsn(25, context.var("lexer"));
                        methodWriter.visitMethodInsn(182, JSONLexerBase, "stringDefaultValue", "()Ljava/lang/String;");
                        methodWriter.visitJumpInsn(167, flagEnd_);
                        methodWriter.visitLabel(flagElse_);
                        methodWriter.visitInsn(1);
                        methodWriter.visitLabel(flagEnd_);
                    } else {
                        methodWriter.visitInsn(1);
                    }
                    methodWriter.visitTypeInsn(192, ASMUtils.type(fieldClass2));
                    methodWriter.visitVarInsn(58, context.var(fieldInfo2.name + "_asm"));
                }
            }
            for (int i4 = 0; i4 < fieldListSize; i4++) {
                FieldInfo fieldInfo3 = context.fieldInfoList[i4];
                Class<?> fieldClass3 = fieldInfo3.fieldClass;
                Type fieldType2 = fieldInfo3.fieldType;
                Label notMatch_ = new Label();
                if (fieldClass3 == Boolean.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldBoolean", "([C)Z");
                    methodWriter.visitVarInsn(54, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == Byte.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldInt", "([C)I");
                    methodWriter.visitVarInsn(54, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == Short.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldInt", "([C)I");
                    methodWriter.visitVarInsn(54, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == Integer.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldInt", "([C)I");
                    methodWriter.visitVarInsn(54, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == Long.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldLong", "([C)J");
                    methodWriter.visitVarInsn(55, context.var(fieldInfo3.name + "_asm", 2));
                } else if (fieldClass3 == Float.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldFloat", "([C)F");
                    methodWriter.visitVarInsn(56, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == Double.TYPE) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldDouble", "([C)D");
                    methodWriter.visitVarInsn(57, context.var(fieldInfo3.name + "_asm", 2));
                } else if (fieldClass3 == String.class) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldString", "([C)Ljava/lang/String;");
                    methodWriter.visitVarInsn(58, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == int[].class) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldIntArray", "([C)[I");
                    methodWriter.visitVarInsn(58, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == float[].class) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldFloatArray", "([C)[F");
                    methodWriter.visitVarInsn(58, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3 == float[][].class) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldFloatArray2", "([C)[[F");
                    methodWriter.visitVarInsn(58, context.var(fieldInfo3.name + "_asm"));
                } else if (fieldClass3.isEnum()) {
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitVarInsn(25, 0);
                    methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                    _getFieldDeser(context, methodWriter, fieldInfo3);
                    methodWriter.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "scanEnum", "(L" + JSONLexerBase + ";[C" + ASMUtils.desc((Class<?>) ObjectDeserializer.class) + ")Ljava/lang/Enum;");
                    methodWriter.visitTypeInsn(192, ASMUtils.type(fieldClass3));
                    methodWriter.visitVarInsn(58, context.var(fieldInfo3.name + "_asm"));
                } else {
                    if (Collection.class.isAssignableFrom(fieldClass3)) {
                        methodWriter.visitVarInsn(25, context.var("lexer"));
                        methodWriter.visitVarInsn(25, 0);
                        methodWriter.visitFieldInsn(180, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
                        Class<?> itemClass = TypeUtils.getCollectionItemClass(fieldType2);
                        if (itemClass == String.class) {
                            methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldClass3)));
                            methodWriter.visitMethodInsn(182, JSONLexerBase, "scanFieldStringArray", "([CLjava/lang/Class;)" + ASMUtils.desc((Class<?>) Collection.class));
                            methodWriter.visitVarInsn(58, context.var(fieldInfo3.name + "_asm"));
                        } else {
                            _deserialze_list_obj(context, methodWriter, reset_, fieldInfo3, fieldClass3, itemClass, i4);
                            if (i4 == fieldListSize - 1) {
                                _deserialize_endCheck(context, methodWriter, reset_);
                            }
                        }
                    } else {
                        _deserialze_obj(context, methodWriter, reset_, fieldInfo3, fieldClass3, i4);
                        if (i4 == fieldListSize - 1) {
                            _deserialize_endCheck(context, methodWriter, reset_);
                        }
                    }
                }
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
                Label flag_ = new Label();
                methodWriter.visitJumpInsn(158, flag_);
                _setFlag(methodWriter, context, i4);
                methodWriter.visitLabel(flag_);
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
                methodWriter.visitInsn(89);
                methodWriter.visitVarInsn(54, context.var("matchStat"));
                methodWriter.visitLdcInsn(-1);
                methodWriter.visitJumpInsn(159, reset_);
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
                methodWriter.visitJumpInsn(158, notMatch_);
                methodWriter.visitVarInsn(21, context.var("matchedCount"));
                methodWriter.visitInsn(4);
                methodWriter.visitInsn(96);
                methodWriter.visitVarInsn(54, context.var("matchedCount"));
                methodWriter.visitVarInsn(25, context.var("lexer"));
                methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
                methodWriter.visitLdcInsn(4);
                methodWriter.visitJumpInsn(159, end_);
                methodWriter.visitLabel(notMatch_);
                if (i4 == fieldListSize - 1) {
                    methodWriter.visitVarInsn(25, context.var("lexer"));
                    methodWriter.visitFieldInsn(180, JSONLexerBase, "matchStat", "I");
                    methodWriter.visitLdcInsn(4);
                    methodWriter.visitJumpInsn(160, reset_);
                }
            }
            methodWriter.visitLabel(end_);
            if (!context.clazz.isInterface() && !Modifier.isAbstract(context.clazz.getModifiers())) {
                _batchSet(context, methodWriter);
            }
            methodWriter.visitLabel(return_);
            _setContext(context, methodWriter);
            methodWriter.visitVarInsn(25, context.var("instance"));
            Method buildMethod = context.beanInfo.buildMethod;
            if (buildMethod != null) {
                methodWriter.visitMethodInsn(182, ASMUtils.type(context.getInstClass()), buildMethod.getName(), "()" + ASMUtils.desc(buildMethod.getReturnType()));
            }
            methodWriter.visitInsn(176);
            methodWriter.visitLabel(reset_);
            _batchSet(context, methodWriter);
            methodWriter.visitVarInsn(25, 0);
            methodWriter.visitVarInsn(25, 1);
            methodWriter.visitVarInsn(25, 2);
            methodWriter.visitVarInsn(25, 3);
            methodWriter.visitVarInsn(25, context.var("instance"));
            methodWriter.visitVarInsn(21, 4);
            methodWriter.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "parseRest", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;");
            methodWriter.visitTypeInsn(192, ASMUtils.type(context.clazz));
            methodWriter.visitInsn(176);
            methodWriter.visitLabel(super_);
            methodWriter.visitVarInsn(25, 0);
            methodWriter.visitVarInsn(25, 1);
            methodWriter.visitVarInsn(25, 2);
            methodWriter.visitVarInsn(25, 3);
            methodWriter.visitVarInsn(21, 4);
            methodWriter.visitMethodInsn(183, ASMUtils.type(JavaBeanDeserializer.class), "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;I)Ljava/lang/Object;");
            methodWriter.visitInsn(176);
            methodWriter.visitMaxs(6, context.variantIndex);
            methodWriter.visitEnd();
        }
    }

    private void defineVarLexer(Context context, MethodVisitor mw) {
        mw.visitVarInsn(25, 1);
        mw.visitFieldInsn(180, DefaultJSONParser, "lexer", ASMUtils.desc((Class<?>) JSONLexer.class));
        mw.visitTypeInsn(192, JSONLexerBase);
        mw.visitVarInsn(58, context.var("lexer"));
    }

    private void _createInstance(Context context, MethodVisitor mw) {
        Constructor<?> defaultConstructor = context.beanInfo.defaultConstructor;
        if (Modifier.isPublic(defaultConstructor.getModifiers())) {
            mw.visitTypeInsn(187, ASMUtils.type(context.getInstClass()));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(defaultConstructor.getDeclaringClass()), "<init>", "()V");
            mw.visitVarInsn(58, context.var("instance"));
            return;
        }
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, ASMUtils.type(JavaBeanDeserializer.class), "clazz", "Ljava/lang/Class;");
        mw.visitMethodInsn(183, ASMUtils.type(JavaBeanDeserializer.class), "createInstance", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;");
        mw.visitTypeInsn(192, ASMUtils.type(context.getInstClass()));
        mw.visitVarInsn(58, context.var("instance"));
    }

    private void _batchSet(Context context, MethodVisitor mw) {
        _batchSet(context, mw, true);
    }

    private void _batchSet(Context context, MethodVisitor mw, boolean flag) {
        int size = context.fieldInfoList.length;
        for (int i = 0; i < size; i++) {
            Label notSet_ = new Label();
            if (flag) {
                _isFlag(mw, context, i, notSet_);
            }
            _loadAndSet(context, mw, context.fieldInfoList[i]);
            if (flag) {
                mw.visitLabel(notSet_);
            }
        }
    }

    private void _loadAndSet(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.fieldClass;
        Type fieldType = fieldInfo.fieldType;
        if (fieldClass == Boolean.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(21, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Integer.TYPE || fieldClass == Character.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(21, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == Long.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(22, context.var(fieldInfo.name + "_asm", 2));
            if (fieldInfo.method != null) {
                mw.visitMethodInsn(182, ASMUtils.type(context.getInstClass()), fieldInfo.method.getName(), ASMUtils.desc(fieldInfo.method));
                if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                    mw.visitInsn(87);
                    return;
                }
                return;
            }
            mw.visitFieldInsn(181, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
        } else if (fieldClass == Float.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(23, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == Double.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(24, context.var(fieldInfo.name + "_asm", 2));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == String.class) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass.isEnum()) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (Collection.class.isAssignableFrom(fieldClass)) {
            mw.visitVarInsn(25, context.var("instance"));
            if (TypeUtils.getCollectionItemClass(fieldType) == String.class) {
                mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
                mw.visitTypeInsn(192, ASMUtils.type(fieldClass));
            } else {
                mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            }
            _set(context, mw, fieldInfo);
        } else {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            _set(context, mw, fieldInfo);
        }
    }

    private void _set(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Method method = fieldInfo.method;
        if (method != null) {
            mw.visitMethodInsn(method.getDeclaringClass().isInterface() ? 185 : 182, ASMUtils.type(fieldInfo.declaringClass), method.getName(), ASMUtils.desc(method));
            if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                mw.visitInsn(87);
                return;
            }
            return;
        }
        mw.visitFieldInsn(181, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
    }

    private void _setContext(Context context, MethodVisitor mw) {
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var("context"));
        mw.visitMethodInsn(182, DefaultJSONParser, "setContext", "(" + ASMUtils.desc((Class<?>) ParseContext.class) + ")V");
        Label endIf_ = new Label();
        mw.visitVarInsn(25, context.var("childContext"));
        mw.visitJumpInsn(198, endIf_);
        mw.visitVarInsn(25, context.var("childContext"));
        mw.visitVarInsn(25, context.var("instance"));
        mw.visitFieldInsn(181, ASMUtils.type(ParseContext.class), "object", "Ljava/lang/Object;");
        mw.visitLabel(endIf_);
    }

    private void _deserialize_endCheck(Context context, MethodVisitor mw, Label reset_) {
        mw.visitIntInsn(21, context.var("matchedCount"));
        mw.visitJumpInsn(158, reset_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(13);
        mw.visitJumpInsn(160, reset_);
        _quickNextTokenComma(context, mw);
    }

    private void _deserialze_list_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo, Class<?> fieldClass, Class<?> itemType, int i) {
        Label _end_if = new Label();
        mw.visitMethodInsn(182, JSONLexerBase, "matchField", "([C)Z");
        mw.visitJumpInsn(153, _end_if);
        _setFlag(mw, context, i);
        Label valueNotNull_ = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(8);
        mw.visitJumpInsn(160, valueNotNull_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(16);
        mw.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
        mw.visitJumpInsn(167, _end_if);
        mw.visitLabel(valueNotNull_);
        Label storeCollection_ = new Label();
        Label endSet_ = new Label();
        Label lbacketNormal_ = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(21);
        mw.visitJumpInsn(160, endSet_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(14);
        mw.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
        _newCollection(mw, fieldClass, i, true);
        mw.visitJumpInsn(167, storeCollection_);
        mw.visitLabel(endSet_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(14);
        mw.visitJumpInsn(159, lbacketNormal_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(12);
        mw.visitJumpInsn(160, reset_);
        _newCollection(mw, fieldClass, i, false);
        mw.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
        _getCollectionFieldItemDeser(context, mw, fieldInfo, itemType);
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(itemType)));
        mw.visitInsn(3);
        mw.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mw.visitMethodInsn(185, ASMUtils.type(ObjectDeserializer.class), "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitVarInsn(58, context.var("list_item_value"));
        mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
        mw.visitVarInsn(25, context.var("list_item_value"));
        if (fieldClass.isInterface()) {
            mw.visitMethodInsn(185, ASMUtils.type(fieldClass), "add", "(Ljava/lang/Object;)Z");
        } else {
            mw.visitMethodInsn(182, ASMUtils.type(fieldClass), "add", "(Ljava/lang/Object;)Z");
        }
        mw.visitInsn(87);
        mw.visitJumpInsn(167, _end_if);
        mw.visitLabel(lbacketNormal_);
        _newCollection(mw, fieldClass, i, false);
        mw.visitLabel(storeCollection_);
        mw.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
        boolean isPrimitive = ParserConfig.isPrimitive2(fieldInfo.fieldClass);
        _getCollectionFieldItemDeser(context, mw, fieldInfo, itemType);
        if (isPrimitive) {
            mw.visitMethodInsn(185, ASMUtils.type(ObjectDeserializer.class), "getFastMatchToken", "()I");
            mw.visitVarInsn(54, context.var("fastMatchToken"));
            mw.visitVarInsn(25, context.var("lexer"));
            mw.visitVarInsn(21, context.var("fastMatchToken"));
            mw.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
        } else {
            mw.visitInsn(87);
            mw.visitLdcInsn(12);
            mw.visitVarInsn(54, context.var("fastMatchToken"));
            _quickNextToken(context, mw, 12);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, DefaultJSONParser, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
        mw.visitVarInsn(58, context.var("listContext"));
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
        mw.visitLdcInsn(fieldInfo.name);
        mw.visitMethodInsn(182, DefaultJSONParser, "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)" + ASMUtils.desc((Class<?>) ParseContext.class));
        mw.visitInsn(87);
        Label loop_ = new Label();
        Label loop_end_ = new Label();
        mw.visitInsn(3);
        mw.visitVarInsn(54, context.var(UploadQueueMgr.MSGTYPE_INTERVAL));
        mw.visitLabel(loop_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(15);
        mw.visitJumpInsn(159, loop_end_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.className, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(itemType)));
        mw.visitVarInsn(21, context.var(UploadQueueMgr.MSGTYPE_INTERVAL));
        mw.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mw.visitMethodInsn(185, ASMUtils.type(ObjectDeserializer.class), "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitVarInsn(58, context.var("list_item_value"));
        mw.visitIincInsn(context.var(UploadQueueMgr.MSGTYPE_INTERVAL), 1);
        mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
        mw.visitVarInsn(25, context.var("list_item_value"));
        if (fieldClass.isInterface()) {
            mw.visitMethodInsn(185, ASMUtils.type(fieldClass), "add", "(Ljava/lang/Object;)Z");
        } else {
            mw.visitMethodInsn(182, ASMUtils.type(fieldClass), "add", "(Ljava/lang/Object;)Z");
        }
        mw.visitInsn(87);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
        mw.visitMethodInsn(182, DefaultJSONParser, "checkListResolve", "(Ljava/util/Collection;)V");
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(16);
        mw.visitJumpInsn(160, loop_);
        if (isPrimitive) {
            mw.visitVarInsn(25, context.var("lexer"));
            mw.visitVarInsn(21, context.var("fastMatchToken"));
            mw.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
        } else {
            _quickNextToken(context, mw, 12);
        }
        mw.visitJumpInsn(167, loop_);
        mw.visitLabel(loop_end_);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var("listContext"));
        mw.visitMethodInsn(182, DefaultJSONParser, "setContext", "(" + ASMUtils.desc((Class<?>) ParseContext.class) + ")V");
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "token", "()I");
        mw.visitLdcInsn(15);
        mw.visitJumpInsn(160, reset_);
        _quickNextTokenComma(context, mw);
        mw.visitLabel(_end_if);
    }

    private void _quickNextToken(Context context, MethodVisitor mw, int token) {
        Label quickElse_ = new Label();
        Label quickEnd_ = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "getCurrent", "()C");
        if (token == 12) {
            mw.visitVarInsn(16, 123);
        } else if (token == 14) {
            mw.visitVarInsn(16, 91);
        } else {
            throw new IllegalStateException();
        }
        mw.visitJumpInsn(160, quickElse_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        mw.visitInsn(87);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(Integer.valueOf(token));
        mw.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        mw.visitJumpInsn(167, quickEnd_);
        mw.visitLabel(quickElse_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(Integer.valueOf(token));
        mw.visitMethodInsn(182, JSONLexerBase, "nextToken", "(I)V");
        mw.visitLabel(quickEnd_);
    }

    private void _quickNextTokenComma(Context context, MethodVisitor mw) {
        Label quickElse_ = new Label();
        Label quickElseIf0_ = new Label();
        Label quickElseIf1_ = new Label();
        Label quickElseIf2_ = new Label();
        Label quickEnd_ = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "getCurrent", "()C");
        mw.visitInsn(89);
        mw.visitVarInsn(54, context.var("ch"));
        mw.visitVarInsn(16, 44);
        mw.visitJumpInsn(160, quickElseIf0_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        mw.visitInsn(87);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(16);
        mw.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        mw.visitJumpInsn(167, quickEnd_);
        mw.visitLabel(quickElseIf0_);
        mw.visitVarInsn(21, context.var("ch"));
        mw.visitVarInsn(16, 125);
        mw.visitJumpInsn(160, quickElseIf1_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        mw.visitInsn(87);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(13);
        mw.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        mw.visitJumpInsn(167, quickEnd_);
        mw.visitLabel(quickElseIf1_);
        mw.visitVarInsn(21, context.var("ch"));
        mw.visitVarInsn(16, 93);
        mw.visitJumpInsn(160, quickElseIf2_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "next", "()C");
        mw.visitInsn(87);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(15);
        mw.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        mw.visitJumpInsn(167, quickEnd_);
        mw.visitLabel(quickElseIf2_);
        mw.visitVarInsn(21, context.var("ch"));
        mw.visitVarInsn(16, 26);
        mw.visitJumpInsn(160, quickElse_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitLdcInsn(20);
        mw.visitMethodInsn(182, JSONLexerBase, "setToken", "(I)V");
        mw.visitJumpInsn(167, quickEnd_);
        mw.visitLabel(quickElse_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, JSONLexerBase, "nextToken", "()V");
        mw.visitLabel(quickEnd_);
    }

    private void _getCollectionFieldItemDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> itemType) {
        Label notNull_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.className, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, DefaultJSONParser, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(itemType)));
        mw.visitMethodInsn(182, ASMUtils.type(ParserConfig.class), "getDeserializer", "(Ljava/lang/reflect/Type;)" + ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitFieldInsn(181, context.className, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.className, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
    }

    private void _newCollection(MethodVisitor mw, Class<?> fieldClass, int i, boolean set) {
        if (fieldClass.isAssignableFrom(ArrayList.class) && !set) {
            mw.visitTypeInsn(187, "java/util/ArrayList");
            mw.visitInsn(89);
            mw.visitMethodInsn(183, "java/util/ArrayList", "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(LinkedList.class) && !set) {
            mw.visitTypeInsn(187, ASMUtils.type(LinkedList.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(LinkedList.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(HashSet.class)) {
            mw.visitTypeInsn(187, ASMUtils.type(HashSet.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(HashSet.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(TreeSet.class)) {
            mw.visitTypeInsn(187, ASMUtils.type(TreeSet.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(TreeSet.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(LinkedHashSet.class)) {
            mw.visitTypeInsn(187, ASMUtils.type(LinkedHashSet.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(LinkedHashSet.class), "<init>", "()V");
        } else if (set) {
            mw.visitTypeInsn(187, ASMUtils.type(HashSet.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(HashSet.class), "<init>", "()V");
        } else {
            mw.visitVarInsn(25, 0);
            mw.visitLdcInsn(Integer.valueOf(i));
            mw.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
            mw.visitMethodInsn(184, ASMUtils.type(TypeUtils.class), "createCollection", "(Ljava/lang/reflect/Type;)Ljava/util/Collection;");
        }
        mw.visitTypeInsn(192, ASMUtils.type(fieldClass));
    }

    private void _deserialze_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo, Class<?> fieldClass, int i) {
        Label matched_ = new Label();
        Label _end_if = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.className, fieldInfo.name + "_asm_prefix__", "[C");
        mw.visitMethodInsn(182, JSONLexerBase, "matchField", "([C)Z");
        mw.visitJumpInsn(154, matched_);
        mw.visitInsn(1);
        mw.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
        mw.visitJumpInsn(167, _end_if);
        mw.visitLabel(matched_);
        _setFlag(mw, context, i);
        mw.visitVarInsn(21, context.var("matchedCount"));
        mw.visitInsn(4);
        mw.visitInsn(96);
        mw.visitVarInsn(54, context.var("matchedCount"));
        _deserObject(context, mw, fieldInfo, fieldClass, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, DefaultJSONParser, "getResolveStatus", "()I");
        mw.visitLdcInsn(1);
        mw.visitJumpInsn(160, _end_if);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, DefaultJSONParser, "getLastResolveTask", "()" + ASMUtils.desc((Class<?>) DefaultJSONParser.ResolveTask.class));
        mw.visitVarInsn(58, context.var("resolveTask"));
        mw.visitVarInsn(25, context.var("resolveTask"));
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, DefaultJSONParser, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
        mw.visitFieldInsn(181, ASMUtils.type(DefaultJSONParser.ResolveTask.class), "ownerContext", ASMUtils.desc((Class<?>) ParseContext.class));
        mw.visitVarInsn(25, context.var("resolveTask"));
        mw.visitVarInsn(25, 0);
        mw.visitLdcInsn(fieldInfo.name);
        mw.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "getFieldDeserializer", "(Ljava/lang/String;)" + ASMUtils.desc((Class<?>) FieldDeserializer.class));
        mw.visitFieldInsn(181, ASMUtils.type(DefaultJSONParser.ResolveTask.class), "fieldDeserializer", ASMUtils.desc((Class<?>) FieldDeserializer.class));
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn(0);
        mw.visitMethodInsn(182, DefaultJSONParser, "setResolveStatus", "(I)V");
        mw.visitLabel(_end_if);
    }

    private void _deserObject(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> fieldClass, int i) {
        _getFieldDeser(context, mw, fieldInfo);
        Label instanceOfElse_ = new Label();
        Label instanceOfEnd_ = new Label();
        if ((fieldInfo.parserFeatures & Feature.SupportArrayToBean.mask) != 0) {
            mw.visitInsn(89);
            mw.visitTypeInsn(193, ASMUtils.type(JavaBeanDeserializer.class));
            mw.visitJumpInsn(153, instanceOfElse_);
            mw.visitTypeInsn(192, ASMUtils.type(JavaBeanDeserializer.class));
            mw.visitVarInsn(25, 1);
            if (fieldInfo.fieldType instanceof Class) {
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo.fieldClass)));
            } else {
                mw.visitVarInsn(25, 0);
                mw.visitLdcInsn(Integer.valueOf(i));
                mw.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
            }
            mw.visitLdcInsn(fieldInfo.name);
            mw.visitLdcInsn(Integer.valueOf(fieldInfo.parserFeatures));
            mw.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;I)Ljava/lang/Object;");
            mw.visitTypeInsn(192, ASMUtils.type(fieldClass));
            mw.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
            mw.visitJumpInsn(167, instanceOfEnd_);
            mw.visitLabel(instanceOfElse_);
        }
        mw.visitVarInsn(25, 1);
        if (fieldInfo.fieldType instanceof Class) {
            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo.fieldClass)));
        } else {
            mw.visitVarInsn(25, 0);
            mw.visitLdcInsn(Integer.valueOf(i));
            mw.visitMethodInsn(182, ASMUtils.type(JavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
        }
        mw.visitLdcInsn(fieldInfo.name);
        mw.visitMethodInsn(185, ASMUtils.type(ObjectDeserializer.class), "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(192, ASMUtils.type(fieldClass));
        mw.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
        mw.visitLabel(instanceOfEnd_);
    }

    private void _getFieldDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Label notNull_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.className, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, DefaultJSONParser, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo.fieldClass)));
        mw.visitMethodInsn(182, ASMUtils.type(ParserConfig.class), "getDeserializer", "(Ljava/lang/reflect/Type;)" + ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitFieldInsn(181, context.className, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.className, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
    }

    static class Context {
        static final int fieldName = 3;
        static final int parser = 1;
        static final int type = 2;
        /* access modifiers changed from: private */
        public final JavaBeanInfo beanInfo;
        /* access modifiers changed from: private */
        public final String className;
        /* access modifiers changed from: private */
        public final Class<?> clazz;
        /* access modifiers changed from: private */
        public FieldInfo[] fieldInfoList;
        /* access modifiers changed from: private */
        public int variantIndex = -1;
        private final Map<String, Integer> variants = new HashMap();

        public Context(String className2, ParserConfig config, JavaBeanInfo beanInfo2, int initVariantIndex) {
            this.className = className2;
            this.clazz = beanInfo2.clazz;
            this.variantIndex = initVariantIndex;
            this.beanInfo = beanInfo2;
            this.fieldInfoList = beanInfo2.fields;
        }

        public Class<?> getInstClass() {
            Class<?> instClass = this.beanInfo.builderClass;
            if (instClass == null) {
                return this.clazz;
            }
            return instClass;
        }

        public int var(String name, int increment) {
            if (this.variants.get(name) == null) {
                this.variants.put(name, Integer.valueOf(this.variantIndex));
                this.variantIndex += increment;
            }
            return this.variants.get(name).intValue();
        }

        public int var(String name) {
            if (this.variants.get(name) == null) {
                Map<String, Integer> map = this.variants;
                int i = this.variantIndex;
                this.variantIndex = i + 1;
                map.put(name, Integer.valueOf(i));
            }
            return this.variants.get(name).intValue();
        }
    }

    private void _init(ClassWriter cw, Context context) {
        int size = context.fieldInfoList.length;
        for (int i = 0; i < size; i++) {
            new FieldWriter(cw, 1, context.fieldInfoList[i].name + "_asm_prefix__", "[C").visitEnd();
        }
        for (FieldInfo fieldInfo : context.fieldInfoList) {
            Class<?> fieldClass = fieldInfo.fieldClass;
            if (!fieldClass.isPrimitive()) {
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    new FieldWriter(cw, 1, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class)).visitEnd();
                } else {
                    new FieldWriter(cw, 1, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class)).visitEnd();
                }
            }
        }
        MethodVisitor mw = new MethodWriter(cw, 1, "<init>", "(" + ASMUtils.desc((Class<?>) ParserConfig.class) + ASMUtils.desc((Class<?>) JavaBeanInfo.class) + ")V", (String) null, (String[]) null);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitMethodInsn(183, ASMUtils.type(JavaBeanDeserializer.class), "<init>", "(" + ASMUtils.desc((Class<?>) ParserConfig.class) + ASMUtils.desc((Class<?>) JavaBeanInfo.class) + ")V");
        for (FieldInfo fieldInfo2 : context.fieldInfoList) {
            mw.visitVarInsn(25, 0);
            mw.visitLdcInsn("\"" + fieldInfo2.name + "\":");
            mw.visitMethodInsn(182, "java/lang/String", "toCharArray", "()[C");
            mw.visitFieldInsn(181, context.className, fieldInfo2.name + "_asm_prefix__", "[C");
        }
        mw.visitInsn(177);
        mw.visitMaxs(4, 4);
        mw.visitEnd();
    }

    private void _createInstance(ClassWriter cw, Context context) {
        if (Modifier.isPublic(context.beanInfo.defaultConstructor.getModifiers())) {
            MethodVisitor mw = new MethodWriter(cw, 1, "createInstance", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;", (String) null, (String[]) null);
            mw.visitTypeInsn(187, ASMUtils.type(context.getInstClass()));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.type(context.getInstClass()), "<init>", "()V");
            mw.visitInsn(176);
            mw.visitMaxs(3, 3);
            mw.visitEnd();
        }
    }
}
