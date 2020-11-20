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

create table FriendRequests
(
  id int,
  fromUser int,
  toUser int,
  status varchar(20),
  constraint fk1_FriendRequests foreign key(fromUser) references Users(id)
        on delete cascade,
  constraint fk2_FriendRequests foreign key(toUser) references Users(id)
        on delete cascade,
  constraint pk_FriendRequests primary key(id)
);

-- many to many relationship between Users and Messages

create table Messages
(
  id int,
  text varchar(100),
  data date,
  constraint pk_Messages primary key(id)
);

create table MessagesUsers
(
    fromUserId int,
    toUserId int,
    MessageId int,
    replyId int default NULL,
    constraint fk1_MessagesUsers foreign key(fromUserId) references Users(id)
        on delete cascade ,
    constraint fk2_MessagesUsers foreign key (toUserId) references Users(id)
        on delete cascade ,
    constraint fk3_MessagesUsers foreign key (MessageId) references Messages(id)
        on delete cascade ,
    constraint pk_MessagesUsers primary key (fromUserId,MessageId,toUserId),
    constraint fk4_MessagesUsers foreign key (replyId) references Messages(id)
);

-- Inserting Entities

insert into Users(id,firstName,secondName)
values (1,'Aprogramatoarei','Ionut'),
       (2,'Apetrei','Ileana'),(3,'Pop','Dan'),(4,'Zaharia','Stancu');


insert into Friendships(id1,id2,date)
values (1,3,'2020-11-01'),(2,3,'2020-11-01'),(3,4,'2020-11-01');

insert into Messages(id,text,data)
values (1,'Ana are mere','2020-11-12'),(2,'Salut','2020-11-13');

insert into MessagesUsers(fromUserId, toUserId, MessageId)
values (1,3,1),(1,4,1);





