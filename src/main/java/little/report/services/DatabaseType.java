package little.report.services;

public enum DatabaseType {
    MYSQL("MySQL", "my"),
    POSTGRES("PostgreSQL", "pg"),
    ORACLE("Oracle", "or"),
    MSSQL("Microsoft SQL Server", "ms"),
    H2("H2", "h2");
 
    private final String dbIdentifier;
    private final String shortName;
 
    private DatabaseType(String dbIdentifier, String shortName) {
       this.dbIdentifier = dbIdentifier;
       this.shortName = shortName;
    }
 
    public String getDbIdentifier() {
       return this.dbIdentifier;
    }
 
    public static DatabaseType getDatabaseTypeFromIdentifier(String dbIdent) {
       DatabaseType databaseType = MYSQL;
       DatabaseType[] var2 = values();
       int var3 = var2.length;
 
       for(int var4 = 0; var4 < var3; ++var4) {
          DatabaseType dbTypeTmp = var2[var4];
          if (dbTypeTmp.getDbIdentifier().equals(dbIdent)) {
             databaseType = dbTypeTmp;
             break;
          }
       }
 
       return databaseType;
    }
 
    public String getShortName() {
       return this.shortName;
    }    
}
