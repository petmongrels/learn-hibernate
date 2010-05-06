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
declare @AshokKumarAccountNumber varchar(50)
select @AshokKumarAccountNumber = 'SB12345678'
select @AshokKumarId = Id From Customers where Name = @AshokKumarName
insert into Accounts (Number, CustomerId, Balance) values (@AshokKumarAccountNumber, @AshokKumarId, 200000.00)

-- Transactions
declare @AshokKumarAccountId int
select @AshokKumarAccountId = Id From Accounts where Number = @AshokKumarAccountNumber
insert into Transactions (Amount, Type, AccountId, Description) values (100.00, 'Debit', @AshokKumarAccountId, 'Pocket Money')
GO