CREATE DATABASE [LearnHibernate] ON  PRIMARY
( NAME = N'LearnHibernate', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\DATA\LearnNHibernate.mdf', SIZE = 2048KB, FILEGROWTH = 1024KB )
 LOG ON
( NAME = N'LearnHibernate_log', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\DATA\LearnNHibernate_log.ldf' , SIZE = 1024KB , FILEGROWTH = 10%)
ALTER DATABASE $(DatabaseName) SET READ_COMMITTED_SNAPSHOT ON
GO