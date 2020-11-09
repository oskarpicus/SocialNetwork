create database Social_Network;

create table Users
(
    id int PRIMARY KEY ,
    firstName varchar(30),
    secondName varchar(30)
);

create table Friendships
(
    id1 int,
    id2 int,
    date date,
    constraint fk1_Friendships foreign key(id1) references Users(id)
        on delete cascade,
    constraint fk2_Friendships foreign key(id2) references Users(id)
        on delete cascade ,
    constraint pk_Friendships primary key(id1,id2)
);

insert into Users(id,firstName,secondName)
values (1,'Aprogramatoarei','Ionut'),
       (2,'Apetrei','Ileana'),(3,'Pop','Dan'),(4,'Zaharia','Stancu');


insert into Friendships(id1,id2,date)
values (1,3,'2020-11-01'),(2,3,'2020-11-01'),(3,4,'2020-11-01');





