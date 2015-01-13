package groovy

import com.sungung.adapter.GroovyAdapter
import groovy.sql.Sql
import com.sungung.adapter.GroovyReportException

/**
 * Simple tabular report, groovy class should be extended from GroovyAdapter.
 * @author PARK Sungung
 * @since 0.0.1
 */
class BrewerReport extends GroovyAdapter{

    @Override
    void execute(Map<String, String> params) throws GroovyReportException {

        // Print out parameters in report file
        getReportFile().withWriter { out ->
            params.each {
                k, v -> out.writeLine("${k}:${v}")
            }
        }

        if (!getDataSource()) {
            logWarn("Database is not initiated. The report is failed.")
            throw new GroovyReportException("Datasource is not defined.");
        }

        def db = new Sql(getDataSource())

        // Print query records to report file

        getReportFile().withWriterAppend{ out ->
            out.writeLine("Brewer,Contact,Address,Suburb,Postcode,Phone,Email,Website")
        }

        getReportFile().withWriterAppend{ out ->
            db.eachRow("select name,contact,address,suburb,postcode,phone,email,website from brewer") {
                row -> out.writeLine("${row.name},${row.contact},${row.address},${row.suburb},${row.postcode},${row.phone},${row.email},${row.website}")
            }
        }

    }
}
