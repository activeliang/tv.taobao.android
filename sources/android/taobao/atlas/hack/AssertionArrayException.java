package android.taobao.atlas.hack;

import android.taobao.atlas.hack.Hack;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class AssertionArrayException extends Exception {
    private static final long serialVersionUID = 1;
    private List<Hack.HackDeclaration.HackAssertionException> mAssertionErr = new ArrayList();

    public AssertionArrayException(String detailMsg) {
        super(detailMsg);
    }

    public void addException(Hack.HackDeclaration.HackAssertionException exception) {
        this.mAssertionErr.add(exception);
    }

    public void addException(List<Hack.HackDeclaration.HackAssertionException> exception) {
        this.mAssertionErr.addAll(exception);
    }

    public List<Hack.HackDeclaration.HackAssertionException> getExceptions() {
        return this.mAssertionErr;
    }

    public static AssertionArrayException mergeException(AssertionArrayException first, AssertionArrayException second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        AssertionArrayException ret = new AssertionArrayException(first.getMessage() + SymbolExpUtil.SYMBOL_SEMICOLON + second.getMessage());
        ret.addException(first.getExceptions());
        ret.addException(second.getExceptions());
        return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Hack.HackDeclaration.HackAssertionException failure : this.mAssertionErr) {
            sb.append(failure.toString()).append(SymbolExpUtil.SYMBOL_SEMICOLON);
            try {
                if (failure.getCause() instanceof NoSuchFieldException) {
                    Field[] fields = failure.getHackedClass().getDeclaredFields();
                    sb.append(failure.getHackedClass().getName()).append(".").append(failure.getHackedFieldName()).append(SymbolExpUtil.SYMBOL_SEMICOLON);
                    for (Field name : fields) {
                        sb.append(name.getName()).append(WVNativeCallbackUtil.SEPERATER);
                    }
                } else if (failure.getCause() instanceof NoSuchMethodException) {
                    Method[] methods = failure.getHackedClass().getDeclaredMethods();
                    sb.append(failure.getHackedClass().getName()).append("->").append(failure.getHackedMethodName()).append(SymbolExpUtil.SYMBOL_SEMICOLON);
                    for (int i = 0; i < methods.length; i++) {
                        if (failure.getHackedMethodName().equals(methods[i].getName())) {
                            sb.append(methods[i].toGenericString()).append(WVNativeCallbackUtil.SEPERATER);
                        }
                    }
                } else {
                    sb.append(failure.getCause());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            sb.append("@@@@");
        }
        return sb.toString();
    }
}
