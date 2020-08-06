package anet.channel.statist;

import anet.channel.GlobalAppRuntimeInfo;

@Monitor(module = "networkPrefer", monitorPoint = "strategy_stat")
public class StrategyStatObject extends StatObject {
    @Dimension
    public StringBuilder errorTrace;
    @Dimension
    public int isFileExists;
    @Dimension
    public int isReadObjectSucceed;
    @Dimension
    public int isRenameSucceed;
    @Dimension
    public int isSucceed;
    @Dimension
    public int isTempWriteSucceed;
    @Dimension
    public String readStrategyFileId;
    @Dimension
    public String readStrategyFilePath;
    @Dimension
    public int type = -1;
    @Dimension
    public String writeStrategyFileId;
    @Dimension
    public String writeStrategyFilePath;
    @Dimension
    public String writeTempFilePath;

    public StrategyStatObject(int type2) {
        this.type = type2;
    }

    public void appendErrorTrace(String tag, Throwable throwable) {
        String message = throwable.getMessage();
        if (this.errorTrace == null) {
            this.errorTrace = new StringBuilder();
        }
        this.errorTrace.append('[').append(tag).append(']').append(tag).append(' ').append(message).append(10);
    }

    public boolean beforeCommit() {
        return GlobalAppRuntimeInfo.isTargetProcess();
    }
}
