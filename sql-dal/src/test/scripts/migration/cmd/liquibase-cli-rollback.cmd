SET LIQUIBASE_PATH=C:\Java\liquibase\liquibase-3.6.2

%LIQUIBASE_PATH%\liquibase.bat --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver ^
     --classpath=%LIQUIBASE_PATH%\lib ^
     --changeLogFile="D:\_SCM_\GIT\mvs-my-sl\tco-mvs-portal\sql-dal\src\main\resources\speed_layer_changelog.xml" ^
     --url="jdbc:sqlserver://mvsdatalaket332devtco.database.windows.net:1433;database=mvsdatalaket332devtco1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" ^
     --username=SOME_SQL_USER ^
     --password=XXX ^
     rollback version_0.2.0
