use $(DatabaseName)
-- Cities
insert into Cities (Name) values ('Bangalore')
insert into Cities (Name) values ('Mumbai')
declare @Bangalore int
declare @Mumbai int
select @Bangalore = Id from Cities where Name = 'Bangalore'
select @Mumbai = Id from Cities where Name = 'Mumbai'

-- Customers
declare @AshokKumarName nvarchar(50)
select @AshokKumarName = 'Ashok Kumar'
insert into Customers (Name, Email, CityId) values (@AshokKumarName, 'akumar@thoughtworks.com', @Bangalore)
insert into Customers (Name, Email, CityId) values ('Amitabh Bachchan', 'amitabh@bollywood.com', @Bangalore)
insert into Customers (Name, Email, CityId) values ('Dharmendra', 'Dharmendra@bollywood.com', @Bangalore)
insert into Customers (Name, Email, CityId) values ('Hema Malini', 'hema@bollywood.com', @Bangalore)
insert into Customers (Name, Email, CityId) values ('Sanjiv Kumar', 'skumar@bollywood.com', @Bangalore)
insert into Customers (Name, Email, CityId) values ('Amjad Khan', 'amjad@bollywood.com', @Mumbai)
insert into Customers (Name, Email, CityId) values ('A. K. Hangal', 'akhangal@bollywood.com', @Mumbai)
insert into Customers (Name, Email, CityId) values ('Leela Mishra', 'lmishra@bollywood.com', @Mumbai)
insert into Customers (Name, Email, CityId) values ('Asrani', 'asrani@bollywood.com', @Mumbai)

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