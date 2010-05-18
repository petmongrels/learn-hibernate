use $(DatabaseName);

create table Cities
(
	Id int identity(1,1) not null,
	Name nvarchar(50) not null,
	primary key (Id)
)
GO

create table Customers
(
    Id int identity(1,1) not null,
    Name nvarchar(50) not null,
    Email nvarchar(100) not null,
    Version int not null default 1,
    CityId int not null,
    IsCommercial bit not null default 0,
    primary key (Id),
    constraint FK_CustomersCity foreign key(CityId) references Cities(Id)
)
GO

create table Accounts
(
	Id int identity(1,1) not null,
	Number varchar(50) not null,
	CustomerId int not null,
	Balance numeric(10,2) not null
	primary key (Id),
	constraint FK_AccountsCustomer foreign key(CustomerId) references Customers(Id)
)
GO

create table Transactions
(
	Id int identity(1,1) not null,
	Amount numeric(5,2) not null,
	Type varchar(10) not null, 
	AccountId int not null,
	Description nvarchar(100) null,
	primary key (Id),
	constraint FK_TransactionsAccount foreign key(AccountId) references Accounts(Id)
)
GO

create table Addresses
(
    Id int identity(1,1) not null,
    Line1 nvarchar(100) not null,
    Line2 nvarchar(100) null,
    CustomerId int not null,
    primary key(Id),
    constraint FK_CustomersAddresses foreign key(CustomerId) references Customers(Id)
)
GO