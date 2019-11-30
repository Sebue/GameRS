drop table if exists game;
create table game
(
gameId bigint not null auto_increment,
name varchar(200),
releaseYear int,
requiredAge int,
hasDemoVersion boolean,
dlcCount int,
metacriticScore int,
isControllerSupported boolean,
recommendationCount int,
achievementCount int,
initialPrice float,
finalPrice float,
ownerCount int,
primary key (gameId)
);

drop table if exists gamestatistic;
create table gamestatistic
(
gameStatisticId bigint not null auto_increment,
gameId bigint not null,
userId bigint not null,
playedHours float not null,
primary key (gameStatisticId)
);

drop table if exists recommendation;
create table recommendation
(
recommendationId bigint not null auto_increment,
gameId bigint not null,
userId bigint not null,
probability float not null,
recommendationType enum ('CONTENT_BASED', 'HYBRID', 'COLLABORATIVE') not null,
primary key (recommendationId)
);
