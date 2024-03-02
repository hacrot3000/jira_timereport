package little.report.services;

import java.sql.Statement;

public interface SqlProcessorIct {
   void close();

   Statement createStatement();
}
