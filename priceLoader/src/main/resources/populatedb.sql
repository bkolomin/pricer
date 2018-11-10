create table price
(
  supplier varchar(100),
  comment varchar(500),
  code varchar(100),
  name varchar(500),
  price double precision,
  stock varchar(50)
)
;

alter table price owner to prices_user
;

