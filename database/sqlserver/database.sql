CREATE DATABASE [$(DatabaseName)] ON  PRIMARY
( NAME = N'$(DatabaseName)', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\DATA\$(DatabaseName).mdf', SIZE = 2048KB, FILEGROWTH = 1024KB )
 LOG ON
( NAME = N'$(DatabaseName)_log', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\DATA\$(DatabaseName)_log.ldf' , SIZE = 1024KB , FILEGROWTH = 10%)
GO