create database Social_Network;

create table Users
(
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    firstName varchar(30),
    secondName varchar(30)
);

create table Friendships
(
    id1 int,
    id2 int,
    constraint fk1_Friendships foreign key(id1) references Users(id),
    constraint fk2_Friendships foreign key(id2) references Users(id),
    constraint pk_Friendships primary key(id1,id2)
);

insert into Users(firstName,secondName)
values ('Aprogramatoarei','Ionut'),
       ('Apetrei','Ileana'),('Pop','Dan'),('Zaharia','Stancu');


insert into Friendships(id1,id2)
values (1,3),(2,3),(3,4);


