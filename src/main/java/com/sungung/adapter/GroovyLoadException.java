package com.sungung.adapter;

/**
 * @author PARK Sungung
 * @Since 0.0.1
 */
public class GroovyLoadException extends RuntimeException {
    public GroovyLoadException(String s) {
        super(s);
    }

    public GroovyLoadException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
