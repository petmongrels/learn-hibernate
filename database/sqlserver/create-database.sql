CREATE DATABASE [LearnHibernate] ON  PRIMARY 
( NAME = N'LearnHibernate', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\DATA\LearnNHibernate.mdf', SIZE = 2048KB, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'LearnHibernate_log', FILENAME = N'c:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\DATA\LearnNHibernate_log.ldf' , SIZE = 1024KB , FILEGROWTH = 10%)
GO

EXEC dbo.sp_dbcmptlevel @dbname=N'LearnHibernate', @new_cmptlevel=90
GO

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [LearnHibernate].[dbo].[sp_fulltext_database] @action = 'disable'
end
GO

USE [LearnHibernate]
GO

IF NOT EXISTS (SELECT name FROM sys.filegroups WHERE is_default=1 AND name = N'PRIMARY') ALTER DATABASE [LearnHibernate] MODIFY FILEGROUP [PRIMARY] DEFAULT
GO