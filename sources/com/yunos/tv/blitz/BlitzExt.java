package com.yunos.tv.blitz;

import java.lang.ref.WeakReference;

public class BlitzExt {
    public static final int MemberAttributeNone = 0;
    public static final int MemberAttributeReadOnly = 1;
    private static WeakReference<BlitzExtensionCallback> mBlitzExtensionCallback;

    public interface BlitzEventHandler {
        void onCall(Object obj);
    }

    public interface BlitzExtensionCallback {
        void onInit(BlitzExtension blitzExtension);

        void onRelease(BlitzExtension blitzExtension);
    }

    public interface BlitzJSFunction {
        void onCall(BlitzJSArguments blitzJSArguments);
    }

    public interface BlitzObjectListener {
        void onCall(int i, Object obj);
    }

    private static native void nativeInit();

    static {
        nativeInit();
    }

    public static class BlitzJSValue {
        private long mNative;

        private native BlitzJSValue nativeAppendArray(long j);

        private native BlitzJSValue nativeAppendBool(long j, boolean z);

        private native BlitzJSValue nativeAppendDouble(long j, double d);

        private native BlitzJSValue nativeAppendFunc(long j, BlitzJSFunction blitzJSFunction, BlitzObjectListener blitzObjectListener);

        private native BlitzJSValue nativeAppendInt(long j, int i);

        private native BlitzJSValue nativeAppendNull(long j);

        private native BlitzJSValue nativeAppendObject(long j);

        private native BlitzJSValue nativeAppendObjectType(long j, String str, BlitzJSArguments blitzJSArguments);

        private native BlitzJSValue nativeAppendString(long j, String str);

        private native boolean nativeAsBool(long j) throws FormatError;

        private native double nativeAsDouble(long j) throws FormatError;

        private native int nativeAsInt(long j) throws FormatError;

        private native String nativeAsString(long j) throws FormatError;

        private native boolean nativeCall(long j, BlitzJSArguments blitzJSArguments);

        private native int nativeGetExtensionObjectID(long j);

        private native Object nativeGetExtensionObjectUserPtr(long j);

        private native BlitzJSValue nativeGetMemberIndex(long j, int i);

        private native BlitzJSValue nativeGetMemberKey(long j, String str);

        private native boolean nativeIsArray(long j);

        private native boolean nativeIsBool(long j);

        private native boolean nativeIsDouble(long j);

        private native boolean nativeIsFunction(long j);

        private native boolean nativeIsInt(long j);

        private native boolean nativeIsNull(long j);

        private native boolean nativeIsObject(long j);

        private native boolean nativeIsString(long j);

        private native boolean nativeIsUndefined(long j);

        private native boolean nativeNewObject(long j, BlitzJSArguments blitzJSArguments, BlitzJSValue blitzJSValue);

        private native void nativeRelease(long j);

        private native BlitzJSValue nativeSetArrayMember(long j, String str, int i);

        private native boolean nativeSetExtensionObjectUserPtr(long j, Object obj);

        private native BlitzJSValue nativeSetMemberBool(long j, String str, boolean z, int i);

        private native BlitzJSValue nativeSetMemberDouble(long j, String str, double d, int i);

        private native BlitzJSValue nativeSetMemberFunc(long j, String str, BlitzJSFunction blitzJSFunction, int i, BlitzObjectListener blitzObjectListener);

        private native BlitzJSValue nativeSetMemberGetter(long j, String str, BlitzJSFunction blitzJSFunction, int i);

        private native BlitzJSValue nativeSetMemberInt(long j, String str, int i, int i2);

        private native BlitzJSValue nativeSetMemberObject(long j, String str, int i);

        private native BlitzJSValue nativeSetMemberObjectType(long j, String str, String str2, BlitzJSArguments blitzJSArguments, int i);

        private native BlitzJSValue nativeSetMemberSetter(long j, String str, BlitzJSFunction blitzJSFunction, int i);

        private native BlitzJSValue nativeSetMemberString(long j, String str, String str2, int i);

        private native BlitzJSValue nativeSetNullMember(long j, String str, int i);

        private native BlitzJSValue nativeSetPrototypeArrayMember(long j, String str, int i);

        private native BlitzJSValue nativeSetPrototypeMemberBool(long j, String str, boolean z, int i);

        private native BlitzJSValue nativeSetPrototypeMemberDouble(long j, String str, double d, int i);

        private native BlitzJSValue nativeSetPrototypeMemberFunc(long j, String str, BlitzJSFunction blitzJSFunction, int i, BlitzObjectListener blitzObjectListener);

        private native BlitzJSValue nativeSetPrototypeMemberGetter(long j, String str, BlitzJSFunction blitzJSFunction, int i);

        private native BlitzJSValue nativeSetPrototypeMemberInt(long j, String str, int i, int i2);

        private native BlitzJSValue nativeSetPrototypeMemberSetter(long j, String str, BlitzJSFunction blitzJSFunction, int i);

        private native BlitzJSValue nativeSetPrototypeMemberString(long j, String str, String str2, int i);

        private native BlitzJSValue nativeSetPrototypeNullMember(long j, String str, int i);

        private native BlitzJSValue nativeSetPrototypeObjectMember(long j, String str, int i);

        private native BlitzJSValue nativeSetPrototypeObjectMemberType(long j, String str, String str2, BlitzJSArguments blitzJSArguments, int i);

        public static class FormatError extends Exception {
            private static final long serialVersionUID = 1;

            FormatError(String msg) {
                super(msg);
            }
        }

        private BlitzJSValue(long pNative) {
            this.mNative = pNative;
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            nativeRelease(this.mNative);
        }

        public void release() {
            nativeRelease(this.mNative);
        }

        public BlitzJSValue GetMember(int index) {
            return nativeGetMemberIndex(this.mNative, index);
        }

        public BlitzJSValue GetMember(String key) {
            return nativeGetMemberKey(this.mNative, key);
        }

        public int GetExtensionObjectID() {
            return nativeGetExtensionObjectID(this.mNative);
        }

        public Object GetExtensionObjectUserPtr() {
            return nativeGetExtensionObjectUserPtr(this.mNative);
        }

        public boolean SetExtensionObjectUserPtr(Object object) {
            return nativeSetExtensionObjectUserPtr(this.mNative, object);
        }

        public BlitzJSValue Append(BlitzJSFunction func) {
            return Append(func, (BlitzObjectListener) null);
        }

        public BlitzJSValue Append(BlitzJSFunction func, BlitzObjectListener deleteListener) {
            return nativeAppendFunc(this.mNative, func, deleteListener);
        }

        public BlitzJSValue Append(int val) {
            return nativeAppendInt(this.mNative, val);
        }

        public BlitzJSValue Append(double val) {
            return nativeAppendDouble(this.mNative, val);
        }

        public BlitzJSValue Append(boolean val) {
            return nativeAppendBool(this.mNative, val);
        }

        public BlitzJSValue Append(String val) {
            return nativeAppendString(this.mNative, val);
        }

        public BlitzJSValue AppendObject() {
            return nativeAppendObject(this.mNative);
        }

        public BlitzJSValue AppendObject(String type, BlitzJSArguments args) {
            return nativeAppendObjectType(this.mNative, type, args);
        }

        public BlitzJSValue AppendArray() {
            return nativeAppendArray(this.mNative);
        }

        public BlitzJSValue AppendNull() {
            return nativeAppendNull(this.mNative);
        }

        public BlitzJSValue SetMember(String name, BlitzJSFunction func) {
            return SetMember(name, func, 0, (BlitzObjectListener) null);
        }

        public BlitzJSValue SetMember(String name, BlitzJSFunction func, int attribute) {
            return SetMember(name, func, attribute, (BlitzObjectListener) null);
        }

        public BlitzJSValue SetMember(String name, BlitzJSFunction func, BlitzObjectListener deleteListener) {
            return SetMember(name, func, 0, deleteListener);
        }

        public BlitzJSValue SetMember(String name, BlitzJSFunction func, int attribute, BlitzObjectListener deleteListener) {
            return nativeSetMemberFunc(this.mNative, name, func, attribute, deleteListener);
        }

        public BlitzJSValue SetMember(String name, int val) {
            return SetMember(name, val, 0);
        }

        public BlitzJSValue SetMember(String name, int val, int attribute) {
            return nativeSetMemberInt(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetMember(String name, double val) {
            return SetMember(name, val, 0);
        }

        public BlitzJSValue SetMember(String name, double val, int attribute) {
            return nativeSetMemberDouble(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetMember(String name, boolean val) {
            return SetMember(name, val, 0);
        }

        public BlitzJSValue SetMember(String name, boolean val, int attribute) {
            return nativeSetMemberBool(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetMember(String name, String val) {
            return SetMember(name, val, 0);
        }

        public BlitzJSValue SetMember(String name, String val, int attribute) {
            return nativeSetMemberString(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetObjectMember(String name) {
            return SetObjectMember(name, 0);
        }

        public BlitzJSValue SetObjectMember(String name, int attribute) {
            return nativeSetMemberObject(this.mNative, name, attribute);
        }

        public BlitzJSValue SetObjectMember(String name, String type, BlitzJSArguments args) {
            return SetObjectMember(name, type, args, 0);
        }

        public BlitzJSValue SetObjectMember(String name, String type, BlitzJSArguments args, int attribute) {
            return nativeSetMemberObjectType(this.mNative, name, type, args, attribute);
        }

        public BlitzJSValue SetArrayMember(String name) {
            return SetArrayMember(name, 0);
        }

        public BlitzJSValue SetArrayMember(String name, int attribute) {
            return nativeSetArrayMember(this.mNative, name, attribute);
        }

        public BlitzJSValue SetNullMember(String name) {
            return SetNullMember(name, 0);
        }

        public BlitzJSValue SetNullMember(String name, int attribute) {
            return nativeSetNullMember(this.mNative, name, attribute);
        }

        public BlitzJSValue SetMemberGetter(String name, BlitzJSFunction func) {
            return SetMemberGetter(name, func, 0);
        }

        public BlitzJSValue SetMemberGetter(String name, BlitzJSFunction func, int attribute) {
            return nativeSetMemberGetter(this.mNative, name, func, attribute);
        }

        public BlitzJSValue SetMemberSetter(String name, BlitzJSFunction func) {
            return SetMemberSetter(name, func, 0);
        }

        public BlitzJSValue SetMemberSetter(String name, BlitzJSFunction func, int attribute) {
            return nativeSetMemberSetter(this.mNative, name, func, attribute);
        }

        public BlitzJSValue SetPrototypeMember(String name, BlitzJSFunction func) {
            return SetPrototypeMember(name, func, 0, (BlitzObjectListener) null);
        }

        public BlitzJSValue SetPrototypeMember(String name, BlitzJSFunction func, int attribute) {
            return SetPrototypeMember(name, func, attribute, (BlitzObjectListener) null);
        }

        public BlitzJSValue SetPrototypeMember(String name, BlitzJSFunction func, BlitzObjectListener deleteListener) {
            return SetPrototypeMember(name, func, 0, deleteListener);
        }

        public BlitzJSValue SetPrototypeMember(String name, BlitzJSFunction func, int attribute, BlitzObjectListener deleteListener) {
            return nativeSetPrototypeMemberFunc(this.mNative, name, func, attribute, deleteListener);
        }

        public BlitzJSValue SetPrototypeMember(String name, int val) {
            return SetPrototypeMember(name, val, 0);
        }

        public BlitzJSValue SetPrototypeMember(String name, int val, int attribute) {
            return nativeSetPrototypeMemberInt(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetPrototypeMember(String name, double val) {
            return SetPrototypeMember(name, val, 0);
        }

        public BlitzJSValue SetPrototypeMember(String name, double val, int attribute) {
            return nativeSetPrototypeMemberDouble(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetPrototypeMember(String name, boolean val) {
            return SetPrototypeMember(name, val, 0);
        }

        public BlitzJSValue SetPrototypeMember(String name, boolean val, int attribute) {
            return nativeSetPrototypeMemberBool(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetPrototypeMember(String name, String val) {
            return SetPrototypeMember(name, val, 0);
        }

        public BlitzJSValue SetPrototypeMember(String name, String val, int attribute) {
            return nativeSetPrototypeMemberString(this.mNative, name, val, attribute);
        }

        public BlitzJSValue SetPrototypeObjectMember(String name) {
            return SetPrototypeObjectMember(name, 0);
        }

        public BlitzJSValue SetPrototypeObjectMember(String name, int attribute) {
            return nativeSetPrototypeObjectMember(this.mNative, name, attribute);
        }

        public BlitzJSValue SetPrototypeObjectMember(String name, String type, BlitzJSArguments args) {
            return SetPrototypeObjectMember(name, type, args, 0);
        }

        public BlitzJSValue SetPrototypeObjectMember(String name, String type, BlitzJSArguments args, int attribute) {
            return nativeSetPrototypeObjectMemberType(this.mNative, name, type, args, attribute);
        }

        public BlitzJSValue SetPrototypeArrayMember(String name) {
            return SetPrototypeArrayMember(name, 0);
        }

        public BlitzJSValue SetPrototypeArrayMember(String name, int attribute) {
            return nativeSetPrototypeArrayMember(this.mNative, name, attribute);
        }

        public BlitzJSValue SetPrototypeNullMember(String name) {
            return SetPrototypeNullMember(name, 0);
        }

        public BlitzJSValue SetPrototypeNullMember(String name, int attribute) {
            return nativeSetPrototypeNullMember(this.mNative, name, attribute);
        }

        public BlitzJSValue SetPrototypeMemberGetter(String name, BlitzJSFunction func) {
            return SetPrototypeMemberGetter(name, func, 0);
        }

        public BlitzJSValue SetPrototypeMemberGetter(String name, BlitzJSFunction func, int attribute) {
            return nativeSetPrototypeMemberGetter(this.mNative, name, func, attribute);
        }

        public BlitzJSValue SetPrototypeMemberSetter(String name, BlitzJSFunction func) {
            return SetPrototypeMemberSetter(name, func, 0);
        }

        public BlitzJSValue SetPrototypeMemberSetter(String name, BlitzJSFunction func, int attribute) {
            return nativeSetPrototypeMemberSetter(this.mNative, name, func, attribute);
        }

        public boolean IsUndefined() {
            return nativeIsUndefined(this.mNative);
        }

        private boolean IsNull() {
            return nativeIsNull(this.mNative);
        }

        public boolean IsBool() {
            return nativeIsBool(this.mNative);
        }

        public boolean IsInt() {
            return nativeIsInt(this.mNative);
        }

        public boolean IsDouble() {
            return nativeIsDouble(this.mNative);
        }

        public boolean IsString() {
            return nativeIsString(this.mNative);
        }

        public boolean IsArray() {
            return nativeIsArray(this.mNative);
        }

        public boolean IsObject() {
            return nativeIsObject(this.mNative);
        }

        public boolean IsFunction() {
            return nativeIsFunction(this.mNative);
        }

        public int AsInt() throws FormatError {
            return nativeAsInt(this.mNative);
        }

        public double AsDouble() throws FormatError {
            return nativeAsDouble(this.mNative);
        }

        public boolean AsBool() throws FormatError {
            return nativeAsBool(this.mNative);
        }

        public String AsString() throws FormatError {
            return nativeAsString(this.mNative);
        }

        public boolean Call(BlitzJSArguments args) {
            return nativeCall(this.mNative, args);
        }

        public boolean NewObject(BlitzJSArguments args, BlitzJSValue newObj) {
            return nativeNewObject(this.mNative, args, newObj);
        }
    }

    public static class BlitzJSGlobalRef {
        private long mNative;

        private native void nativeRelease(long j);

        private native BlitzJSValue nativeToJSValue(long j);

        private BlitzJSGlobalRef(long pNative) {
            this.mNative = pNative;
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            nativeRelease(this.mNative);
        }

        public void release() {
            nativeRelease(this.mNative);
        }

        public BlitzJSValue ToJSValue() {
            return nativeToJSValue(this.mNative);
        }
    }

    public static class BlitzJSArguments {
        private long mNative;

        private native BlitzJSValue nativeAddArrayParameter(long j);

        private native void nativeAddNullParameter(long j);

        private native BlitzJSValue nativeAddObjectParameter(long j);

        private native BlitzJSValue nativeAddObjectParameterType(long j, String str, BlitzJSArguments blitzJSArguments);

        private native void nativeAddParameterBool(long j, boolean z);

        private native void nativeAddParameterDouble(long j, double d);

        private native void nativeAddParameterInt(long j, int i);

        private native void nativeAddParameterString(long j, String str);

        private native void nativeAddParameterValue(long j, BlitzJSValue blitzJSValue);

        private native long nativeGetCoreContentIndex(long j);

        private native long nativeGetJSEngineId(long j);

        private native BlitzJSValue nativeGetReturnValue(long j);

        private native BlitzJSValue nativeGetThisObject(long j);

        private native void nativeRelease(long j);

        private native BlitzJSValue nativeSetReturnArrayValue(long j);

        private native BlitzJSValue nativeSetReturnObjectValue(long j);

        private native BlitzJSValue nativeSetReturnObjectValueType(long j, String str, BlitzJSArguments blitzJSArguments);

        private native void nativeSetReturnValueBool(long j, boolean z);

        private native void nativeSetReturnValueDouble(long j, double d);

        private native void nativeSetReturnValueInt(long j, int i);

        private native void nativeSetReturnValueString(long j, String str);

        private native void nativeSetReturnValueValue(long j, BlitzJSValue blitzJSValue);

        private native void nativeSetThisObject(long j, BlitzJSValue blitzJSValue);

        private native void nativeThrowException(long j);

        private native BlitzJSValue nativeValueAt(long j, int i);

        private native int nativelength(long j);

        private BlitzJSArguments(long pNative) {
            this.mNative = pNative;
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            nativeRelease(this.mNative);
        }

        public void release() {
            nativeRelease(this.mNative);
        }

        public int length() {
            return nativelength(this.mNative);
        }

        public BlitzJSValue ValueAt(int index) {
            return nativeValueAt(this.mNative, index);
        }

        public void AddParameter(BlitzJSValue param) {
            nativeAddParameterValue(this.mNative, param);
        }

        public void AddParameter(int param) {
            nativeAddParameterInt(this.mNative, param);
        }

        public void AddParameter(boolean param) {
            nativeAddParameterBool(this.mNative, param);
        }

        public void AddParameter(double param) {
            nativeAddParameterDouble(this.mNative, param);
        }

        public void AddParameter(String param) {
            nativeAddParameterString(this.mNative, param);
        }

        public BlitzJSValue AddObjectParameter() {
            return nativeAddObjectParameter(this.mNative);
        }

        public BlitzJSValue AddObjectParameter(String type, BlitzJSArguments args) {
            return nativeAddObjectParameterType(this.mNative, type, args);
        }

        public BlitzJSValue AddArrayParameter() {
            return nativeAddArrayParameter(this.mNative);
        }

        public void AddNullParameter() {
            nativeAddNullParameter(this.mNative);
        }

        public BlitzJSValue GetThisObject() {
            return nativeGetThisObject(this.mNative);
        }

        public BlitzJSValue GetReturnValue() {
            return nativeGetReturnValue(this.mNative);
        }

        public void SetReturnValue(BlitzJSValue retval) {
            nativeSetReturnValueValue(this.mNative, retval);
        }

        public void SetReturnValue(int value) {
            nativeSetReturnValueInt(this.mNative, value);
        }

        public void SetReturnValue(double value) {
            nativeSetReturnValueDouble(this.mNative, value);
        }

        public void SetReturnValue(boolean value) {
            nativeSetReturnValueBool(this.mNative, value);
        }

        public void SetReturnValue(String value) {
            nativeSetReturnValueString(this.mNative, value);
        }

        public BlitzJSValue SetReturnObjectValue() {
            return nativeSetReturnObjectValue(this.mNative);
        }

        public BlitzJSValue SetReturnObjectValue(String type, BlitzJSArguments args) {
            return nativeSetReturnObjectValueType(this.mNative, type, args);
        }

        public BlitzJSValue SetReturnArrayValue() {
            return nativeSetReturnArrayValue(this.mNative);
        }

        public void SetThisObject(BlitzJSValue thisobj) {
            nativeSetThisObject(this.mNative, thisobj);
        }

        public void ThrowException() {
            nativeThrowException(this.mNative);
        }

        public long GetCoreContentIndex() {
            return nativeGetCoreContentIndex(this.mNative);
        }

        public long GetJSEngineId() {
            return nativeGetJSEngineId(this.mNative);
        }
    }

    public static class BlitzExtension {
        private long mNative;

        private native BlitzJSValue nativeAddConstructorMethod(long j, BlitzJSFunction blitzJSFunction, String str, BlitzObjectListener blitzObjectListener);

        private native BlitzJSValue nativeAddMethod(long j, BlitzJSFunction blitzJSFunction, String str, BlitzObjectListener blitzObjectListener);

        private native BlitzJSValue nativeAddObject(long j, String str);

        private native BlitzJSValue nativeAddObjectOther(long j, String str, String str2);

        private native BlitzJSArguments nativeCreateArguments(long j);

        private native long nativeGetCoreContentIndex(long j);

        private native BlitzJSValue nativeGetExtensionObjectName(long j, String str, int i);

        private native BlitzJSValue nativeGetExtensionObjectType(long j, BlitzJSValue blitzJSValue, int i);

        private native BlitzJSGlobalRef nativeGetGlobalRef(long j, BlitzJSValue blitzJSValue);

        private native long nativeGetJSEngineId(long j);

        private native void nativeRelease(long j);

        private native void nativeReleaseGlobalRef(long j, BlitzJSGlobalRef blitzJSGlobalRef);

        private native void nativeTriggerEvent(long j, BlitzEventHandler blitzEventHandler, Object obj);

        private BlitzExtension(long pNative) {
            this.mNative = pNative;
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            nativeRelease(this.mNative);
        }

        public void release() {
            nativeRelease(this.mNative);
        }

        public BlitzJSValue AddMethod(BlitzJSFunction func, String funname) {
            return AddMethod(func, funname, (BlitzObjectListener) null);
        }

        public BlitzJSValue AddMethod(BlitzJSFunction func, String funname, BlitzObjectListener deleteListener) {
            return nativeAddMethod(this.mNative, func, funname, deleteListener);
        }

        public BlitzJSValue AddConstructorMethod(BlitzJSFunction func, String funname) {
            return AddConstructorMethod(func, funname, (BlitzObjectListener) null);
        }

        public BlitzJSValue AddConstructorMethod(BlitzJSFunction func, String funname, BlitzObjectListener deleteListener) {
            return nativeAddConstructorMethod(this.mNative, func, funname, deleteListener);
        }

        public BlitzJSValue AddObject(String objname) {
            return nativeAddObject(this.mNative, objname);
        }

        public BlitzJSValue AddObject(String toObjectName, String objname) {
            return nativeAddObjectOther(this.mNative, toObjectName, objname);
        }

        public BlitzJSArguments CreateArguments() {
            return nativeCreateArguments(this.mNative);
        }

        public BlitzJSValue GetExtensionObject(String funcname, int objid) {
            return nativeGetExtensionObjectName(this.mNative, funcname, objid);
        }

        public BlitzJSValue GetExtensionObject(BlitzJSValue functype, int objid) {
            return nativeGetExtensionObjectType(this.mNative, functype, objid);
        }

        public BlitzJSGlobalRef GetGlobalRef(BlitzJSValue jsval) {
            return nativeGetGlobalRef(this.mNative, jsval);
        }

        /* access modifiers changed from: package-private */
        public void ReleaseGlobalRef(BlitzJSGlobalRef globalRef) {
            nativeReleaseGlobalRef(this.mNative, globalRef);
        }

        public void TriggerEvent(BlitzEventHandler handler, Object usrptr) {
            nativeTriggerEvent(this.mNative, handler, usrptr);
        }

        public long GetCoreContentIndex() {
            return nativeGetCoreContentIndex(this.mNative);
        }

        public long GetJSEngineId() {
            return nativeGetJSEngineId(this.mNative);
        }
    }

    public static void setBlitzExtensionCallback(BlitzExtensionCallback cb) {
        mBlitzExtensionCallback = cb != null ? new WeakReference<>(cb) : null;
    }

    private static void onInitFromNative(BlitzExtension ext) {
        BlitzExtensionCallback cb;
        if (mBlitzExtensionCallback != null && (cb = (BlitzExtensionCallback) mBlitzExtensionCallback.get()) != null) {
            cb.onInit(ext);
        }
    }

    private static void onReleaseFromNative(BlitzExtension ext) {
        BlitzExtensionCallback cb;
        if (mBlitzExtensionCallback != null && (cb = (BlitzExtensionCallback) mBlitzExtensionCallback.get()) != null) {
            cb.onRelease(ext);
        }
    }
}
