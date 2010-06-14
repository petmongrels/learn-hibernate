use $(DatabaseName)
-- Cities
insert into Cities (Name) values ('Bangalore')
insert into Cities (Name) values ('Mumbai')
insert into Cities (Name) values ('Calcutta')
declare @Bangalore int
declare @Mumbai int
declare @Calcutta int
select @Bangalore = Id from Cities where Name = 'Bangalore'
select @Mumbai = Id from Cities where Name = 'Mumbai'
select @Calcutta = Id from Cities where Name = 'Calcutta'

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
insert into Customers (Name, Email, CityId) values ('Jaya Bhaduri', 'jaya@bollywood.com', @Calcutta)
insert into Customers (Name, Email, CityId, IsCommercial) values ('Sholay', 'sholay@bollywood.com', @Mumbai, 1)

-- Accounts
declare @AshokKumarId int
declare @AshokKumarSavingsAccountNumber varchar(50)
select @AshokKumarSavingsAccountNumber = 'SB12345678'
select @AshokKumarId = Id From Customers where Name = @AshokKumarName
insert into Accounts (AccountNumber, CustomerId, Balance) values (@AshokKumarSavingsAccountNumber, @AshokKumarId, 200000.00)

declare @AshokKumarCurrentAccountNumber varchar(50)
select @AshokKumarCurrentAccountNumber = 'CA4234789'
insert into Accounts (AccountNumber, CustomerId, Balance) values (@AshokKumarCurrentAccountNumber, @AshokKumarId, 7000.00)

-- Transactions
declare @AshokKumarSavingsAccountId int
select @AshokKumarSavingsAccountId = Id From Accounts where AccountNumber = @AshokKumarSavingsAccountNumber
insert into Transactions (Amount, Type, AccountId, Description) values (100.00, 'Debit', @AshokKumarSavingsAccountId, 'Pocket Money')
insert into Transactions (Amount, Type, AccountId, Description) values (200.00, 'Debit', @AshokKumarSavingsAccountId, 'Credit Card')
insert into Transactions (Amount, Type, AccountId, Description) values (300.00, 'Debit', @AshokKumarSavingsAccountId, 'ATM')

declare @AshokKumarCurrentAccountId int
select @AshokKumarCurrentAccountId = Id From Accounts where AccountNumber = @AshokKumarCurrentAccountNumber
insert into Transactions (Amount, Type, AccountId, Description) values (100.00, 'Debit', @AshokKumarCurrentAccountId, 'Pocket Money')
insert into Transactions (Amount, Type, AccountId, Description) values (200.00, 'Debit', @AshokKumarCurrentAccountId, 'Credit Card')
insert into Transactions (Amount, Type, AccountId, Description) values (300.00, 'Debit', @AshokKumarCurrentAccountId, 'ATM')

-- Addresses
insert into Addresses (Line1, Line2, CustomerId) values ("Diamond District", "Airport Road", 1);
insert into Addresses (Line1, Line2, CustomerId) values ("Habitat", "Brigade Road", 1);
GO