package com.jtomaszk.apps.common.logger;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.eid.exceptions.Eid;

/**
 * Created by jarema-user on 2016-01-23.
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class EidLogger {

    @NonNull
    private final Logger logger;

    public static EidLogger getLogger() {
        return new EidLogger(LoggerManager.getLogger());
    }

    /**
     * Send a {@link Level#ERROR} log message.
     *
     * @param throwable a throwable object to send.
     */
    public void e(String id, Throwable throwable) {
        logger.e(new Eid(id).toString(), throwable);
    }

    /**
     * Send a {@link Level#VERBOSE} log message.
     *
     * @param messageFormat a message format you would like logged.
     * @param args          arguments for a formatter.
     */
    public void v(String id, String messageFormat, Object... args) {
        logger.v(new Eid(id).makeLogMessage(messageFormat, args));
    }

    /**
     * Send a {@link Level#DEBUG} log message.
     *
     * @param messageFormat a message format you would like logged.
     * @param args          arguments for a formatter.
     */
    public void d(String id, String messageFormat, Object... args) {
        logger.d(new Eid(id).makeLogMessage(messageFormat, args));
    }

    /**
     * Send a {@link Level#INFO} log message.
     *
     * @param messageFormat a message format you would like logged.
     * @param args          arguments for a formatter.
     */
    public void i(String id, String messageFormat, Object... args) {
        logger.i(new Eid(id).makeLogMessage(messageFormat, args));
    }


    /**
     * Send a {@link Level#WARN} log message.
     *
     * @param messageFormat a message format you would like logged.
     * @param args          arguments for a formatter.
     */
    public void w(String id, String messageFormat, Object... args) {
        logger.w(new Eid(id).makeLogMessage(messageFormat, args));
    }


    /**
     * Send a {@link Level#ERROR} log message.
     *
     * @param messageFormat a message format you would like logged.
     * @param args          arguments for a formatter.
     */
    public void e(String id, String messageFormat, Object... args) {
        logger.e(new Eid(id).makeLogMessage(messageFormat, args));
    }


    /**
     * Send a {@link Level#ASSERT} log message.
     *
     * @param messageFormat a message format you would like logged.
     * @param args          arguments for a formatter.
     */
    public void a(String id, String messageFormat, Object... args) {
        logger.a(new Eid(id).makeLogMessage(messageFormat, args));
    }

}
