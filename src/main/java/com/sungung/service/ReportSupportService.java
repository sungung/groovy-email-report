package com.sungung.service;

import com.sungung.adapter.GroovyAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author PARK Sungung
 * @Since 0.0.1
 */
@Service
public class ReportSupportService {
    @Async
    public void executeGroovy(GroovyAdapter groovyAdapter, Map<String, String> queryParams) throws InterruptedException{
        groovyAdapter.execute(queryParams);
        groovyAdapter.fetchEmailReport(
                queryParams.get(GroovyAdapter.PARAM_REPORT_TITLE)==null ? groovyAdapter.getClass().getName() : queryParams.get(GroovyAdapter.PARAM_REPORT_TITLE),
                queryParams
        );
    }

}
