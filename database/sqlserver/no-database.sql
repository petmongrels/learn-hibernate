USE [master]
GO

IF db_id('$(DatabaseName)') IS NOT NULL
	BEGIN
		Exec('USE $(DatabaseName)')
		ALTER DATABASE [$(DatabaseName)] SET SINGLE_USER WITH ROLLBACK IMMEDIATE

		USE [master]
		DROP DATABASE [$(DatabaseName)]
	END
GO