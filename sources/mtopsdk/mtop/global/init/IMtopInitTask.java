package mtopsdk.mtop.global.init;

import mtopsdk.mtop.global.MtopConfig;

public interface IMtopInitTask {
    void executeCoreTask(MtopConfig mtopConfig);

    void executeExtraTask(MtopConfig mtopConfig);
}
