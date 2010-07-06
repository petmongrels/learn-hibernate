create table Cities
(
	Id number(10) not null,
	Name varchar2(50) not null,
	constraint PK_Cities primary key (Id)
);

create sequence Cities_Seq;
create or replace trigger Cities_bir
before insert on Cities
for each row
when (new.Id is null)
begin
  select Cities_Seq.nextval
  into   :new.Id
  from   dual;
end;
/

create table Customers
(
	Id number(10) not null,
	Name varchar2(50) not null,
	Email varchar2(100) not null,
    Version number(10) default 1 not null,
    CityId number(10) not null,
    IsCommercial number(1) default 0 not null,
    constraint PK_Customers primary key (Id),
    constraint FK_CustomersCity foreign key(CityId) references Cities(Id),
    constraint UK_CustomersEmail unique(Email)
);
create sequence Customers_Seq;
create or replace trigger Customers_bir
before insert on Customers
for each row
when (new.Id is null)
begin
  select Customers_Seq.nextval
  into   :new.Id
  from   dual;
end;
/

create table Accounts
(
	Id number(10) not null,
	AccountNumber varchar2(50) not null,
	CustomerId number(10) null,
	Balance number(10,2) not null,
	Version number(10) default 1 not null, 
	constraint PK_Accounts primary key (Id),
	constraint FK_AccountsCustomer foreign key(CustomerId) references Customers(Id)
);
alter table Accounts modify (CustomerId not null deferrable initially deferred);
create sequence Accounts_Seq;
create or replace trigger Accounts_bir
before insert on Accounts
for each row
when (new.Id is null)
begin
  select Accounts_Seq.nextval
  into   :new.Id
  from   dual;
end;
/

create table Transactions
(
	Id number(10) not null,
	Amount number(5,2) not null,
	Type varchar2(10) not null,
	AccountId number(10) null,
	Description varchar2(100) null,
	constraint PK_Transaction primary key (Id),
	constraint FK_TransactionsAccount foreign key(AccountId) references Accounts(Id)
);
alter table Transactions modify (AccountId not null deferrable initially deferred);
create sequence Transactions_Seq;
create or replace trigger Transactions_bir
before insert on Transactions
for each row
when (new.Id is null)
begin
  select Transactions_Seq.nextval
  into   :new.Id
  from   dual;
end;
/

create table Addresses
(
    Id number(10) not null,
    Line1 varchar2(100) not null,
    Line2 varchar2(100) null,
    CustomerId int null,
    constraint PK_Addresses primary key(Id),
    constraint FK_CustomersAddresses foreign key(CustomerId) references Customers(Id)
);
alter table Addresses modify (CustomerId not null deferrable initially deferred);
create sequence Addresses_Seq;
create or replace trigger Addresses_bir
 before insert on Addresses
 for each row
 when (new.Id is null)
 begin
   select Addresses_Seq.nextval
   into   :new.Id
   from   dual;
 end;
/