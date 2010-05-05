USE [master]
GO

IF db_id('LearnHibernate') IS NOT NULL
	BEGIN
		Exec('USE LearnHibernate')
		ALTER DATABASE [LearnHibernate] SET SINGLE_USER WITH ROLLBACK IMMEDIATE

		USE [master]
		DROP DATABASE [LearnHibernate]
	END
GO