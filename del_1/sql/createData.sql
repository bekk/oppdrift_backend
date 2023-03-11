--DROP DATABASE Kastmeg;

CREATE DATABASE kastmeg;

SET SEARCH_PATH = kastmeg;

CREATE TABLE public.table0 (
                               id integer generated always as identity,
                               category CHARACTER VARYING(40),
                               value DOUBLE PRECISION
);


CREATE TABLE public.table1 (
                               id integer generated always as identity,
                               category CHARACTER VARYING(40),
                               value DOUBLE PRECISION
);


INSERT INTO public.table0(category, value)
SELECT  ROUND(random() * 100)::text, random() FROM generate_series(1,1000000);

INSERT INTO public.table1(category, value)
SELECT  ROUND(random() * 100)::text, random() FROM generate_series(1,1000000);

SELECT
    t1.category, SUM(t0.value) AS count
FROM public.table1 t1
LEFT JOIN public.table0 t0 ON t1.category = t0.category
GROUP BY t1.category
HAVING t1.category > '10'
;

create index ix_category
    on table0 (category, value);

create index ix_category1
    on table1 (category, value);

