package com.sungung.adapter;

/**
 * @author PARK Sungung
 * @Since 0.0.1
 */
public class GroovyReportException extends RuntimeException {
    public GroovyReportException(String s) {
        super(s);
    }
    public GroovyReportException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
