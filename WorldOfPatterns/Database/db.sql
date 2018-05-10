USE worldofpatterns;

drop table if exists pattern;

drop table if exists patternLanguage;

drop table if exists patternLanguagePattern;

create table pattern
(
  id serial primary key,
  name text not null
);

create table patternLanguage
(
  id serial primary key,
  name text not null
);

create table patternLanguagePattern
(
  idLanguage bigint unsigned not null,
  idPattern bigint unsigned not null,
  foreign key (idLanguage)
  references patternLanguage(id)
    on delete cascade,
  foreign key (idPattern)
  references pattern(id)
    on delete cascade,
  primary key (idLanguage, idPattern)
);
