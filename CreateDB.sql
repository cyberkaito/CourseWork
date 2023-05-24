set ansi_padding on
go
set quoted_identifier on 
go
set ansi_nulls on
go

create database [Polyphonia_Database]
go

use[Polyphonia_Database]
go

create table [dbo].[Role]
(
	[ID_Role] [int] not null identity(1,1),
	[Name] [varchar] (25) not null
	constraint [PK_ID_Role] primary key clustered ([ID_Role] ASC) on [PRIMARY],
	constraint [UQ_Name_Role] unique ([Name])
)
go

create table [dbo].[Client]
(
	[ID_Client] [int] not null identity(1,1),
	[Name] [varchar] (12) not null,
	[Email] [varchar] (30) not null,
	[Key] [varchar] (20) not null,
	[Avatar] [varchar] (MAX) null,
	[Bio] [varchar] (45) null
	constraint [PK_ID_Client] primary key clustered ([ID_Client] ASC) on [PRIMARY],
	constraint [UQ_Name] unique ([Name]),
	constraint [UQ_Email] unique ([Email])
)
go

alter table [dbo].[Client] alter column [Key] [varchar] (Max) not null
go

create table [dbo].[Channel]
(
	[ID_Channel] [int] not null identity(1,1),
	[Name] [varchar] (25) not null,
	[Description] [varchar] (MAX) not null,
	[Avatar] [varchar] (MAX) null,
	[Rate] [float] null,
	[Rate_Count] [float] null
	constraint [PK_ID_Channel] primary key clustered ([ID_Channel] ASC) on [PRIMARY],
	constraint [UQ_Name_Channel] unique ([Name])
)
go

create table [dbo].[Category]
(
	[ID_Category] [int] not null identity(1,1),
	[Name] [varchar] (21) not null
	constraint [PK_ID_Category] primary key clustered ([ID_Category] ASC) on [PRIMARY],
	constraint [UQ_Name_Category] unique ([Name])
)
go

create table [dbo].[News]
(
	[ID_News] [int] not null identity(1,1),
	[Header] [varchar] (45) not null,
	[Description] [varchar] (MAX) not null,
	[Rate] [float] null,
	[Rate_Count] [float] null,
	[Date] [datetime] not null,
	[ID_Category] [int] not null, 
	[ID_Channel] [int] not null
	constraint [PK_ID_News] primary key clustered ([ID_News] ASC) on [PRIMARY],
	constraint [FK_ID_Category] foreign key ([ID_Category]) references [dbo].[Category] ([ID_Category]),
	constraint [FK_ID_Channel] foreign key ([ID_Channel]) references [dbo].[Channel] ([ID_Channel])
)
go
create table [dbo].[Media]
(
	[ID_Media] [int] not null identity(1,1),
	[Link] [varchar] (MAX) not null,
	[ID_News] [int] not null
	constraint [PK_ID_Media] primary key clustered ([ID_Media] ASC) on [PRIMARY],
	constraint [FK_ID_News_Media] foreign key ([ID_News]) references [dbo].[News] ([ID_News])
)
go

create table [dbo].[Client_Type]
(
	[ID_Client_Type] [int] not null identity(1,1),
	[ID_Client] [int] not null,
	[ID_Channel] [int] not null,
	[ID_Role] [int] not null 
	constraint [PK_ID_Client_Type] primary key clustered ([ID_Client_Type] ASC) on [PRIMARY],
	constraint [FK_ID_Channel_Client_Type] foreign key ([ID_Channel]) references [dbo].[Channel] ([ID_Channel]),
	constraint [FK_ID_Client] foreign key ([ID_Client]) references [dbo].[Client] ([ID_Client]),
	constraint [FK_ID_Role] foreign key ([ID_Role]) references [dbo].[Role] ([ID_Role])
)
go

create table [dbo].[Comment]
(
	[ID_Comment] [int] not null identity(1,1),
	[ID_Client] [int] not null,
	[ID_News] [int] not null,
	[Text] [varchar] (100) not null
	constraint [PK_ID_Comment] primary key clustered ([ID_Comment] ASC) on [PRIMARY],
	constraint [FK_ID_News_Comment] foreign key ([ID_News]) references [dbo].[News] ([ID_News]),
	constraint [FK_ID_Client_Comment] foreign key ([ID_Client]) references [dbo].[Client] ([ID_Client])
)
go

insert into dbo.Client values('BlackxPink','skd@mail.com','#$wq#$','https://www.seekpng.com/png/detail/126-1268329_people-avatar-1-icon.png','Hi, My name is Steave Michaello!'), 
('AwdPink','skdd@mail.com','#$wq#$','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTJZ3QHqE1UyVRSd5PSgStxOUlq3gd4MLZCJUxMXQZmXUNNR4v9l0BsgvXlBaVES8nbdCg&usqp=CAU','Hi, My name is Mimimi MOMOMO!'),
('Natashka','skdfd@mail.com','#$wq#$s','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgS9gt7bRQjO4-HPlDtfczUF7pOHNwDfqHnZgrbXm4qgyul1MgHkneswnmeMRUQNolJeM&usqp=CAU','Hi, My name is LALALA lilili!');

insert into dbo.Category values ('Сбросить'),('Статьи сообщества'),('Новости'), ('Подписки'),('Образование'),('За последнюю неделю'),('По рейтингу');

insert into dbo.Channel values('Канал1','Описание канала1','https://static.vecteezy.com/system/resources/previews/002/002/310/non_2x/ablack-man-avatar-character-isolated-icon-free-vector.jpg',0,0);
insert into dbo.Channel values('Канал2','Описание канала2','https://muzlifemagazine.ru/wp-content/uploads/2022/11/metallica-posetit-rossiyu-v-2019-godu-3-1024x660.jpg',0,0);

insert into dbo.News values('Заголовок','Описание',5,1,'05.05.2004',1,1),('Заголовок','Описание',4,1,'05.05.2004',2,1),
('Заголовок','Описание',3,1,'05.05.2004',3,1),('Заголовок','Описание',2,1,'25.05.2004',4,1);

insert into dbo.Media values('https://www.jazzmap.ru/files/Small/56eb1479ec159.%D1%81%D0%B8%D0%BD%D0%B0%D1%82%D1%80%D0%B0.jpg',1);
insert into dbo.Media values('https://i.ytimg.com/vi/tI6l9bPAGyM/maxresdefault.jpg',2);
insert into dbo.Media values('https://muz-teoretik.ru/wp-content/uploads/2016/08/kvintovyi-krug-10.jpg',3);
insert into dbo.Media values('https://muz-teoretik.ru/wp-content/uploads/2016/01/ritm-metr-02-300x138.jpg',4);

insert into dbo.Role values('Подписчик'),('Владелец');

insert into dbo.Client_Type values(10,1,2), (11,2,2), (12,2,1), (12,1,1), (10,2,1);
