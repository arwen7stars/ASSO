create table pattern
(
	id serial primary key,
	name varchar(40) not null,
	intent varchar(256),
	motivation text,
	applicability text,
	collaborations text,
	consequences text,
	implementation text,
	sampleCode text,
	knownUses text
);

create table patternParticipant
(
	id serial primary key,
	idPattern bigint unsigned not null,
	name varchar(40) not null,
	description varchar(256) not null,
	foreign key (idPattern)
		references pattern(id)
		on delete cascade
);

create table patternRelation
(
	idPattern bigint unsigned,
	idRelatedPattern bigint unsigned,
	message varchar(256) not null,
	foreign key (idPattern)
		references pattern(id)
		on delete cascade,
	foreign key (idRelatedPattern)
		references pattern(id)
		on delete cascade,
	primary key (idPattern, idRelatedPattern)
);