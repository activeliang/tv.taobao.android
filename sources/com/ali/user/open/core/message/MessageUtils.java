package com.ali.user.open.core.message;

import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ResourceUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Deprecated
public class MessageUtils {
    private static Map<Integer, Message> cachedMessageMetas = new HashMap();
    private static final Object defaultMessageLoadLock = new Object();
    private static final Message defaultMessageNotFoundMessage = new Message();
    private static final Message defaultUnknownErrorMessage = new Message();
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static Message messageNotFoundMessage;
    private static Message unknownErrorMessage;

    static {
        defaultMessageNotFoundMessage.code = 1;
        defaultMessageNotFoundMessage.message = "未在消息文件中找到 id 为 {0} 的消息";
        defaultMessageNotFoundMessage.action = "请检查所依赖的 SDK 项目，或若是手动拷贝 SDK 至当前开发应用所在项目，请检查是否漏拷文件 res/values 下文件";
        defaultMessageNotFoundMessage.type = "E";
        defaultUnknownErrorMessage.code = 2;
        defaultUnknownErrorMessage.message = "检索消息时发生如下错误 {0}";
        defaultUnknownErrorMessage.action = "请检查所依赖的 SDK 项目，或若是手动拷贝 SDK 至当前开发应用所在项目，请检查是否漏拷文件 res/values 下文件";
        defaultUnknownErrorMessage.type = "E";
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ali.user.open.core.message.Message createMessage(int r5, java.lang.Object... r6) {
        /*
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0065 }
            r3.lock()     // Catch:{ Exception -> 0x0065 }
            java.util.Map<java.lang.Integer, com.ali.user.open.core.message.Message> r3 = cachedMessageMetas     // Catch:{ Exception -> 0x0065 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x0065 }
            java.lang.Object r1 = r3.get(r4)     // Catch:{ Exception -> 0x0065 }
            com.ali.user.open.core.message.Message r1 = (com.ali.user.open.core.message.Message) r1     // Catch:{ Exception -> 0x0065 }
            if (r1 != 0) goto L_0x004a
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0065 }
            r3.unlock()     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r3 = r3.writeLock()     // Catch:{ Exception -> 0x0065 }
            r3.lock()     // Catch:{ Exception -> 0x0065 }
            com.ali.user.open.core.message.Message r1 = loadMessage(r5)     // Catch:{ all -> 0x005a }
            if (r1 == 0) goto L_0x0038
            java.util.Map<java.lang.Integer, com.ali.user.open.core.message.Message> r3 = cachedMessageMetas     // Catch:{ all -> 0x005a }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x005a }
            r3.put(r4, r1)     // Catch:{ all -> 0x005a }
        L_0x0038:
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ all -> 0x005a }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ all -> 0x005a }
            r3.lock()     // Catch:{ all -> 0x005a }
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r3 = r3.writeLock()     // Catch:{ Exception -> 0x0065 }
            r3.unlock()     // Catch:{ Exception -> 0x0065 }
        L_0x004a:
            if (r1 != 0) goto L_0x006f
            com.ali.user.open.core.message.Message r1 = createMessageNotFoundMessage(r5)     // Catch:{ all -> 0x0095 }
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0065 }
            r3.unlock()     // Catch:{ Exception -> 0x0065 }
        L_0x0059:
            return r1
        L_0x005a:
            r3 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r4 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r4 = r4.writeLock()     // Catch:{ Exception -> 0x0065 }
            r4.unlock()     // Catch:{ Exception -> 0x0065 }
            throw r3     // Catch:{ Exception -> 0x0065 }
        L_0x0065:
            r0 = move-exception
            java.lang.String r3 = r0.getMessage()
            com.ali.user.open.core.message.Message r1 = createUnknownErrorMessage(r3)
            goto L_0x0059
        L_0x006f:
            int r3 = r6.length     // Catch:{ all -> 0x0095 }
            if (r3 != 0) goto L_0x007c
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0065 }
            r3.unlock()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0059
        L_0x007c:
            java.lang.Object r2 = r1.clone()     // Catch:{ all -> 0x0095 }
            com.ali.user.open.core.message.Message r2 = (com.ali.user.open.core.message.Message) r2     // Catch:{ all -> 0x0095 }
            java.lang.String r3 = r1.message     // Catch:{ all -> 0x0095 }
            java.lang.String r3 = java.text.MessageFormat.format(r3, r6)     // Catch:{ all -> 0x0095 }
            r2.message = r3     // Catch:{ all -> 0x0095 }
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0065 }
            r3.unlock()     // Catch:{ Exception -> 0x0065 }
            r1 = r2
            goto L_0x0059
        L_0x0095:
            r3 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r4 = lock     // Catch:{ Exception -> 0x0065 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r4 = r4.readLock()     // Catch:{ Exception -> 0x0065 }
            r4.unlock()     // Catch:{ Exception -> 0x0065 }
            throw r3     // Catch:{ Exception -> 0x0065 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.message.MessageUtils.createMessage(int, java.lang.Object[]):com.ali.user.open.core.message.Message");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getMessageContent(int r4, java.lang.Object... r5) {
        /*
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r2.readLock()     // Catch:{ Exception -> 0x0067 }
            r2.lock()     // Catch:{ Exception -> 0x0067 }
            java.util.Map<java.lang.Integer, com.ali.user.open.core.message.Message> r2 = cachedMessageMetas     // Catch:{ Exception -> 0x0067 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x0067 }
            java.lang.Object r1 = r2.get(r3)     // Catch:{ Exception -> 0x0067 }
            com.ali.user.open.core.message.Message r1 = (com.ali.user.open.core.message.Message) r1     // Catch:{ Exception -> 0x0067 }
            if (r1 != 0) goto L_0x004a
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r2.readLock()     // Catch:{ Exception -> 0x0067 }
            r2.unlock()     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r2 = r2.writeLock()     // Catch:{ Exception -> 0x0067 }
            r2.lock()     // Catch:{ Exception -> 0x0067 }
            com.ali.user.open.core.message.Message r1 = loadMessage(r4)     // Catch:{ all -> 0x005c }
            if (r1 == 0) goto L_0x0038
            java.util.Map<java.lang.Integer, com.ali.user.open.core.message.Message> r2 = cachedMessageMetas     // Catch:{ all -> 0x005c }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x005c }
            r2.put(r3, r1)     // Catch:{ all -> 0x005c }
        L_0x0038:
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = lock     // Catch:{ all -> 0x005c }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r2.readLock()     // Catch:{ all -> 0x005c }
            r2.lock()     // Catch:{ all -> 0x005c }
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r2 = r2.writeLock()     // Catch:{ Exception -> 0x0067 }
            r2.unlock()     // Catch:{ Exception -> 0x0067 }
        L_0x004a:
            if (r1 != 0) goto L_0x0073
            com.ali.user.open.core.message.Message r2 = createMessageNotFoundMessage(r4)     // Catch:{ all -> 0x0083 }
            java.lang.String r2 = r2.message     // Catch:{ all -> 0x0083 }
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0067 }
            r3.unlock()     // Catch:{ Exception -> 0x0067 }
        L_0x005b:
            return r2
        L_0x005c:
            r2 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r3 = r3.writeLock()     // Catch:{ Exception -> 0x0067 }
            r3.unlock()     // Catch:{ Exception -> 0x0067 }
            throw r2     // Catch:{ Exception -> 0x0067 }
        L_0x0067:
            r0 = move-exception
            java.lang.String r2 = r0.getMessage()
            com.ali.user.open.core.message.Message r2 = createUnknownErrorMessage(r2)
            java.lang.String r2 = r2.message
            goto L_0x005b
        L_0x0073:
            java.lang.String r2 = r1.message     // Catch:{ all -> 0x0083 }
            java.lang.String r2 = java.text.MessageFormat.format(r2, r5)     // Catch:{ all -> 0x0083 }
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0067 }
            r3.unlock()     // Catch:{ Exception -> 0x0067 }
            goto L_0x005b
        L_0x0083:
            r2 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r3 = lock     // Catch:{ Exception -> 0x0067 }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r3.readLock()     // Catch:{ Exception -> 0x0067 }
            r3.unlock()     // Catch:{ Exception -> 0x0067 }
            throw r2     // Catch:{ Exception -> 0x0067 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.message.MessageUtils.getMessageContent(int, java.lang.Object[]):java.lang.String");
    }

    private static Message createUnknownErrorMessage(String message) {
        if (unknownErrorMessage == null) {
            synchronized (defaultMessageLoadLock) {
                if (unknownErrorMessage == null) {
                    unknownErrorMessage = loadMessage(2);
                    if (unknownErrorMessage == null) {
                        unknownErrorMessage = defaultUnknownErrorMessage;
                    }
                }
            }
        }
        try {
            Message retMessage = (Message) unknownErrorMessage.clone();
            retMessage.message = MessageFormat.format(retMessage.message, new Object[]{message});
            return retMessage;
        } catch (CloneNotSupportedException e) {
            return unknownErrorMessage;
        }
    }

    private static Message createMessageNotFoundMessage(int code) {
        if (messageNotFoundMessage == null) {
            synchronized (defaultMessageLoadLock) {
                if (messageNotFoundMessage == null) {
                    messageNotFoundMessage = loadMessage(1);
                    if (messageNotFoundMessage == null) {
                        messageNotFoundMessage = defaultMessageNotFoundMessage;
                    }
                }
            }
        }
        try {
            Message retMessage = (Message) messageNotFoundMessage.clone();
            retMessage.message = MessageFormat.format(retMessage.message, new Object[]{String.valueOf(code)});
            return retMessage;
        } catch (CloneNotSupportedException e) {
            return messageNotFoundMessage;
        }
    }

    private static Message loadMessage(int code) {
        try {
            if (ResourceUtils.getIdentifier(KernelContext.getApplicationContext(), "string", "auth_sdk_message_" + code + "_message") == 0) {
                return null;
            }
            Message message = new Message();
            message.code = code;
            message.message = ResourceUtils.getString(KernelContext.getApplicationContext(), "auth_sdk_message_" + code + "_message");
            if (ResourceUtils.getIdentifier(KernelContext.getApplicationContext(), "string", "auth_sdk_message_" + code + "_action") != 0) {
                message.action = ResourceUtils.getString(KernelContext.getApplicationContext(), "auth_sdk_message_" + code + "_action");
            } else {
                message.action = "";
            }
            if (ResourceUtils.getIdentifier(KernelContext.getApplicationContext(), "string", "auth_sdk_message_" + code + "_type") != 0) {
                message.type = ResourceUtils.getString(KernelContext.getApplicationContext(), "auth_sdk_message_" + code + "_type");
                return message;
            }
            message.type = "I";
            return message;
        } catch (Exception e) {
            SDKLogger.e("kernel", "Fail to get message of the code " + code + ", the error message is " + e.getMessage());
            return null;
        }
    }
}
