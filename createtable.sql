DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

create table movies(
	id varchar(10) not null,
    title varchar(100) not null,
    year integer not null,
    director varchar(100) not null,
    PRIMARY KEY(id)
);

create table stars(
	id varchar(10) not null,
    name varchar(100) not null,
    birthYear integer,
    PRIMARY KEY(id)
);

create table stars_in_movies(
	starId varchar(10) not null,
    movieId varchar(10) not null,
    
    foreign key (starId) references stars(id) ON DELETE CASCADE,
    foreign key (movieId) references movies(id) ON DELETE CASCADE,
    PRIMARY KEY(starId,movieId)
);

create table genres(
	id integer not null auto_increment,
    name varchar(32) not null,
    PRIMARY KEY (id)
);

create table genres_in_movies(
	genreId integer not null,
    movieId varchar(10) not null,
    
    foreign key (genreId) references genres(id) ON DELETE CASCADE,
	
    foreign key (movieId) references movies(id) ON DELETE CASCADE,
    PRIMARY KEY (genreId, movieId)
);

create table creditcards(
	id varchar(20) not null,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    expiration date not null,
    PRIMARY KEY(id)
);

create table customers(
	id integer not null auto_increment,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    ccId varchar(20) not null,
    address varchar(200) not null,
    email varchar(50) not null,
    password varchar(20) not null,
    
    foreign key (ccId) references creditcards(id) ON DELETE NO ACTION,
    PRIMARY KEY (id)
);

create table sales(
	id integer not null auto_increment,
    customerId integer not null,
    movieId varchar(10) not null,
    saleDate date not null,
    
    foreign key (customerId) references customers(id) ON DELETE CASCADE,
	
    foreign key (movieId) references movies(id) ON DELETE CASCADE,
        
	PRIMARY KEY(id)
);

create table ratings(
	movieId varchar(10) not null,
    rating float not null,
    numVotes integer not null,
    
    foreign key (movieId) references movies(id) ON DELETE CASCADE,
	PRIMARY KEY (movieId)
);
