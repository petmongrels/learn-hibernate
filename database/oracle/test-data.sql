-- Cities
insert into Cities (Name) values ('Bangalore');
-- Customers
insert into Customers (Name, Email, CityId) values ('Ashok Kumar', 'akumar@thoughtworks.com', (select Id from Cities where Name = 'Bangalore'));
-- Accounts
insert into Accounts (AccountNumber, CustomerId, Balance) values ('SB132432', (select Id from Customers where Name = 'Ashok Kumar'), 200000.00);
insert into Accounts (AccountNumber, CustomerId, Balance) values ('CC231233', (select Id from Customers where Name = 'Ashok Kumar'), 7000.00);