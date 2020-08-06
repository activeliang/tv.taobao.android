package com.taobao.statistic;

public class Arg {
    private Object mArg1;
    private Object mArg2;
    private Object mArg3;
    private String[] mArgs;

    public Arg(Object arg1, Object arg2, Object arg3, String... args) {
        this.mArg1 = arg1;
        this.mArg2 = arg2;
        this.mArg3 = arg3;
        this.mArgs = args;
    }

    public Object getArg1() {
        return this.mArg1;
    }

    public void setArg1(Object arg1) {
        this.mArg1 = arg1;
    }

    public Object getArg2() {
        return this.mArg2;
    }

    public void setArg2(Object arg2) {
        this.mArg2 = arg2;
    }

    public Object getArg3() {
        return this.mArg3;
    }

    public void setArg3(Object arg3) {
        this.mArg3 = arg3;
    }

    public String[] getArgs() {
        return this.mArgs;
    }

    public void setArgs(String... args) {
        this.mArgs = args;
    }
}
