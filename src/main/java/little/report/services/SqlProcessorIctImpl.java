package little.report.services;

import com.atlassian.jira.component.ComponentAccessor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import org.ofbiz.core.entity.DelegatorInterface;
import org.ofbiz.core.entity.GenericDataSourceException;
import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.jdbc.SQLProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlProcessorIctImpl implements SqlProcessorIct {
   private Connection connection;
   private SQLProcessor sqlProcessor;
   private Logger logger = LoggerFactory.getLogger(SqlProcessorIctImpl.class);

   public SqlProcessorIctImpl() {
      boolean success = false;

      try {
         DelegatorInterface delegator = (DelegatorInterface)ComponentAccessor.getComponentOfType(DelegatorInterface.class);
         String helperName = delegator.getGroupHelperName("default");
         this.sqlProcessor = new SQLProcessor(helperName);
         this.connection = this.sqlProcessor.getConnection();
         success = true;
      } catch (GenericEntityException e1) {
         e1.printStackTrace();
      } catch (Exception e2) {
         e2.printStackTrace();
      }

      if (this.logger.isDebugEnabled()) {
         this.logger.debug(MessageFormat.format("SqlProcessorIct constructor con is: {0}", success));
      }

   }

   public SqlProcessorIctImpl(String nonDefaultHelper) {
      try {
         DelegatorInterface delegator = (DelegatorInterface)ComponentAccessor.getComponentOfType(DelegatorInterface.class);
         String helperName = delegator.getGroupHelperName(nonDefaultHelper);
         this.sqlProcessor = new SQLProcessor(helperName);
         this.connection = this.sqlProcessor.getConnection();
      } catch (GenericEntityException e) {
         e.printStackTrace();
      }

   }

   public void close() {

      if (this.sqlProcessor != null) {
         try {
            this.sqlProcessor.close();
         } catch (GenericDataSourceException e) {
         }
      }

   }

   public Statement createStatement() {
      Statement statement = null;
      if (this.connection != null) {
         try {
            statement = this.connection.createStatement();
         } catch (SQLException e) {
            if (this.logger.isDebugEnabled()) {
               this.logger.error("Exception on sqlProcessor close", e);
            }
         }
      }

      return statement;
   }
}
