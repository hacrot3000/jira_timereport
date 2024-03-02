package little.report.services;


import com.atlassian.jira.component.ComponentAccessor;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import org.ofbiz.core.entity.DelegatorInterface;
import org.ofbiz.core.entity.GenericDataSourceException;
import org.ofbiz.core.entity.config.DatasourceInfo;
import org.ofbiz.core.entity.config.EntityConfigUtil;
import org.ofbiz.core.entity.jdbc.SQLProcessor;

public class SqlHelper {
   private static final Long ORACLE_EXPRESSION_LIMIT_NUM = 1000L;
   private String sqlQuotes;
   private String schemaName = "";
   private boolean isOracle;
   private boolean isMssql;
   private boolean isPostgres;
   private SQLProcessor sqlProcessor;
   private static SqlHelper instance;
   private static Boolean hasExpressionLimit;
   private DatabaseType databaseType;

   public static SqlHelper getInstance() {

      if (instance == null) {
         Class var0 = SqlHelper.class;
         synchronized(SqlHelper.class) {
            if (instance == null) {
               instance = new SqlHelper();
            }
         }
      }

      return instance;
   }

   public void close() {
      SqlHelper tmp = getInstance();
      tmp.closeSqlProcessor();
   }

   private void closeSqlProcessor() {
      try {
         this.sqlProcessor.close();
      } catch (GenericDataSourceException e) {
      }

   }

   private SqlHelper() {
      this.databaseType = DatabaseType.MYSQL;
      this.sqlProcessor = this.initSQLProcessor();

      try {
         Connection connection = this.sqlProcessor.getConnection();
         if (connection != null) {
            String name = connection.getMetaData().getDatabaseProductName();
            DatabaseType dbDatabaseType = DatabaseType.getDatabaseTypeFromIdentifier(name);
            this.setDatabaseTypeInternal(dbDatabaseType);
            connection.close();
         }
      } catch (Exception ex) {
         this.sqlQuotes = "";
      }

   }

   private void setDatabaseTypeInternal(DatabaseType dbDatabaseType) {
      this.sqlQuotes = "\"";
      switch(dbDatabaseType) {
      case MYSQL:
         this.sqlQuotes = "`";
         this.setDatabaseType(DatabaseType.MYSQL);
         break;
      case ORACLE:
         this.isOracle = true;
         this.setDatabaseType(DatabaseType.ORACLE);
         break;
      case POSTGRES:
         this.isPostgres = true;
         this.setDatabaseType(DatabaseType.POSTGRES);
         break;
      case MSSQL:
         this.isMssql = true;
         this.setDatabaseType(DatabaseType.MSSQL);
         break;
      case H2:
         this.sqlQuotes = "`";
         this.setDatabaseType(DatabaseType.H2);
         this.isOracle = false;
         this.isPostgres = false;
         this.isMssql = false;
         break;
      default:
         this.setDatabaseType(DatabaseType.MYSQL);
         this.isOracle = false;
         this.isPostgres = false;
         this.sqlQuotes = "`";
      }

   }

   private SQLProcessor initSQLProcessor() {

      DelegatorInterface delegator = (DelegatorInterface)ComponentAccessor.getComponentOfType(DelegatorInterface.class);
      String helperName = delegator.getGroupHelperName("default");
      SQLProcessor sqlProcessor = new SQLProcessor(helperName);
      DatasourceInfo datasourceInfo = EntityConfigUtil.getInstance().getDatasourceInfo(helperName);
      String schema = datasourceInfo.getSchemaName();
      if (!schema.isEmpty()) {
         this.schemaName = datasourceInfo.getSchemaName() + ".";
      }

      return sqlProcessor;
   }

   public DatabaseType getDatabaseType() {
      return this.databaseType;
   }

   private void setDatabaseType(DatabaseType databaseType) {
      this.databaseType = databaseType;
   }

   public void setDatabaseTypeOverride(DatabaseType databaseType) {
      this.databaseType = databaseType;
      this.setDatabaseTypeInternal(databaseType);
   }

   public String ec(String columnName) {
      return this.sqlQuotes + columnName + this.sqlQuotes;
   }

   public String eDate(Date date) {
      if (this.isOracle) {
         return "to_date('" + (new Timestamp(date.getTime())).toString().substring(0, 19) + "','YYYY-MM-DD HH24:MI:SS')";
      } else {
         return this.isMssql ? "convert(datetime,'" + (new Timestamp(date.getTime())).toString() + "',120)" : "'" + (new Timestamp(date.getTime())).toString() + "'";
      }
   }

//    public String getTableNameForClass(Class<?> clazz) {
//       String pluginKeyMd5 = MD5UtilIctime.md5Hex("de.iconcept.ictime.jira-ictime");
//       String className = clazz.getSimpleName();
//       String lastSixCharactersOfHash = pluginKeyMd5.substring(pluginKeyMd5.length() - 6, pluginKeyMd5.length());
//       String underscoredCaseClassName = className.replaceAll("(.)([A-Z])", "$1_$2");
//       String tableName = "AO_" + lastSixCharactersOfHash.toUpperCase() + "_" + underscoredCaseClassName.toUpperCase();
//       return this.ec(tableName);
//    }

//    public String getTableNameForClassWithSchema(Class<?> clazz) {
//       return this.getSchemaName() + this.getTableNameForClass(clazz);
//    }

   public SqlProcessorIct getSqlProcessor() {
      SqlProcessorIct sqlProcessorIct = new SqlProcessorIctImpl();
      return sqlProcessorIct;
   }

   public String getSchemaName() {
      return this.schemaName;
   }

   public boolean isOracle() {
      return this.isOracle;
   }

   public boolean isMssql() {
      return this.isMssql;
   }

   public boolean isPostgres() {
      return this.isPostgres;
   }

   public void setPostgres(boolean isPostgres) {
      this.isPostgres = isPostgres;
   }

   public static boolean dbHasExpressionLimit() {
      boolean hasExprLimit = false;
      if (hasExpressionLimit == null) {
         SqlHelper tmp = getInstance();
         if (tmp.isOracle) {
            hasExpressionLimit = true;
         } else {
            hasExpressionLimit = false;
         }
      }

      hasExprLimit = hasExpressionLimit;
      return hasExprLimit;
   }

   public String quoteValue(String requestTypeKeyStr) {
      String result;
      if (requestTypeKeyStr != null) {
         result = "'" + requestTypeKeyStr + "'";
      } else {
         result = "''";
      }

      return result;
   }

   public static Long getExpressionLimit() {
      return ORACLE_EXPRESSION_LIMIT_NUM;
   }

   public static void finalClose() {
      try {
         SqlHelper sqlPr = getInstance();
         if (sqlPr != null) {
            sqlPr.close();
         }
      } catch (Exception e) {
      }

   }
}
