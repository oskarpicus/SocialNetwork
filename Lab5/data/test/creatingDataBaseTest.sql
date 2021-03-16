create database Social_Network_Test;

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
    constraint fk1_Friendships foreign key(id1) references Users(id)
        on delete cascade,
    constraint fk2_Friendships foreign key(id2) references Users(id)
        on delete cascade ,
    constraint pk_Friendships primary key(id1,id2)
);

insert into Users(firstName, secondName) VALUES
('Popescu','Ion'),('Iancu','Mihaela'),('Anghel','Diana'),
('Barbu','Andrei'),('Popa','Maria');

insert into Friendships(id1, id2) VALUES (1,4);

alter table Friendships
add column date date;