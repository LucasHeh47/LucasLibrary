package com.lucasj.lucaslibrary.log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorCatcher implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		String stackTrace = sw.toString();
        Debug.err("FATAL] [" + e.getClass().getSimpleName(), stackTrace);
	}

    public static void install() {
        Thread.setDefaultUncaughtExceptionHandler(new ErrorCatcher());
    }

}
