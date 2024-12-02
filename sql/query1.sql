--Select all policies that start before 14.11.2019

-- Which database are we even dealing with? There's lots of differences between
-- different databases, i.e. MySQL and that not very good microsoft database.

-- Essentially each vendor has their own unique SQL dialect.
-- Also the example doesn't give the full database schema, i.e. the types of the
-- fields and other constraints.

-- What if the date is stored as a string? Or an int that represents a UNIX
-- timestamp? What about timezones?

-- Something like this might work:
select * from policy where start_date < date('2019-11-14');

-- I don't know. Check the database manual.