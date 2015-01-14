package com.sungung.adapter;

import java.util.Map;

/**
 * @author PARK Sungung
 * @Since 0.0.1
 */
public interface IGroovyAdapter {
    public void execute(Map<String, String> params) throws GroovyReportException;
}
