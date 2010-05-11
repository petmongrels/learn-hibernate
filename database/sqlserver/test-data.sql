use LearnHibernate
-- Customers
declare @AshokKumarName nvarchar(50)
select @AshokKumarName = 'Ashok Kumar'
insert into Customers (Name, Email) values (@AshokKumarName, 'akumar@thoughtworks.com')
insert into Customers (Name, Email) values ('Amitabh Bachchan', 'amitabh@bollywood.com')
insert into Customers (Name, Email) values ('Dharmendra', 'Dharmendra@bollywood.com')
insert into Customers (Name, Email) values ('Hema Malini', 'hema@bollywood.com')
insert into Customers (Name, Email) values ('Sanjiv Kumar', 'skumar@bollywood.com')
insert into Customers (Name, Email) values ('Amjad Khan', 'amjad@bollywood.com')
insert into Customers (Name, Email) values ('A. K. Hangal', 'akhangal@bollywood.com')
insert into Customers (Name, Email) values ('Leela Mishra', 'lmishra@bollywood.com')
insert into Customers (Name, Email) values ('Asrani', 'asrani@bollywood.com')

-- Accounts
declare @AshokKumarId int
declare @AshokKumarSavingsAccountNumber varchar(50)
select @AshokKumarSavingsAccountNumber = 'SB12345678'
select @AshokKumarId = Id From Customers where Name = @AshokKumarName
insert into Accounts (Number, CustomerId, Balance) values (@AshokKumarSavingsAccountNumber, @AshokKumarId, 200000.00)

declare @AshokKumarCurrentAccountNumber varchar(50)
select @AshokKumarCurrentAccountNumber = 'CA4234789'
insert into Accounts (Number, CustomerId, Balance) values (@AshokKumarCurrentAccountNumber, @AshokKumarId, 7000.00)

-- Transactions
declare @AshokKumarSavingsAccountId int
select @AshokKumarSavingsAccountId = Id From Accounts where Number = @AshokKumarSavingsAccountNumber
insert into Transactions (Amount, Type, AccountId, Description) values (100.00, 'Debit', @AshokKumarSavingsAccountId, 'Pocket Money')
insert into Transactions (Amount, Type, AccountId, Description) values (200.00, 'Debit', @AshokKumarSavingsAccountId, 'Credit Card')
insert into Transactions (Amount, Type, AccountId, Description) values (300.00, 'Debit', @AshokKumarSavingsAccountId, 'ATM')

declare @AshokKumarCurrentAccountId int
select @AshokKumarCurrentAccountId = Id From Accounts where Number = @AshokKumarCurrentAccountNumber
insert into Transactions (Amount, Type, AccountId, Description) values (100.00, 'Debit', @AshokKumarCurrentAccountId, 'Pocket Money')
insert into Transactions (Amount, Type, AccountId, Description) values (200.00, 'Debit', @AshokKumarCurrentAccountId, 'Credit Card')
insert into Transactions (Amount, Type, AccountId, Description) values (300.00, 'Debit', @AshokKumarCurrentAccountId, 'ATM')
GO