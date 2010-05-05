use LearnHibernate;

create table Customers
(
	Id int identity(1,1) not null,
	Name nvarchar(50) not null,
	primary key (Id) 
)
GO

create table Accounts
(
	Id int identity(1,1) not null,
	Number varchar(50) not null,
	CustomerId int not null,
	Balance numeric(10,2) not null
	primary key (Id),
	constraint FK_Accounts_Customers foreign key(CustomerId) REFERENCES Customers(Id)
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
	constraint FK_Transactions_Accounts foreign key(AccountId) references Accounts(Id)
)
GO