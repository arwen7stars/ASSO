USE worldofpatterns;

drop table if exists patternLanguagePattern cascade;

drop table if exists patternLanguage cascade;

drop table if exists pattern cascade;

create table pattern
(
  id serial primary key,
  name text not null,
  lastModified timestamp not null default current_timestamp()
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
