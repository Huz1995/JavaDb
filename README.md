# Database System 

Homebrew SQL database system, built from the ground up. Works with the SQL [Syntax](#syntax) listed below in BNF form. This system uses no external libraries to set up the database or parse the SQL queries. 

## Usage

### Normal Usage
    $ java DBServer
    $ java DBClient
    
Once the client is running, SQL queries can be entered on the command line. A full breakdown of the syntax is given below..

#EXAMPlE

Create and use a test database
CREATE DATABASE markbook; /n
[OK]
USE markbook;
[OK]

// Create a table to store some data
CREATE TABLE marks (name, mark, pass);
[OK]

// Populate the table with a few entries
INSERT INTO marks VALUES ('Steve', 65, true);
[OK]
INSERT INTO marks VALUES ('Dave', 55, true);
[OK]
INSERT INTO marks VALUES ('Bob', 35, false);
[OK]
INSERT INTO marks VALUES ('Clive', 20, false);
[OK]

// Perform various test queries on the table…
SELECT * FROM marks;
[OK]
id	name	mark	pass
1	Steve	65	true
2	Dave	55	true
3	Bob	35	false
4	Clive	20	false

DROP TABLE actors;
DROP TABLE movies;
DROP TABLE roles;
DROP DATABASE imdb;
CREATE DATABASE imdb;
CREATE DATABASE imdb;
CREATE TABLE actors (name, nationality, awards);
INSERT INTO actors VALUES ('Hugh Grant', 'British', 3);
INSERT INTO actors VALUES ('Toni Collette', 'Australian', 12);
INSERT INTO actors VALUES ('James Caan', 'American', 8);
INSERT INTO actors VALUES ('Emma Thompson', 'British', 10);
CREATE TABLE movies (name, genre);
INSERT INTO movies VALUES ('Mickey Blue Eyes', 'Comedy');
INSERT INTO movies VALUES ('About a Boy', 'Comedy');
INSERT INTO movies VALUES ('Sense and Sensibility', 'Period Drama');
SELECT id FROM movies WHERE name == 'Mickey Blue Eyes';
SELECT id FROM movies WHERE name == 'About a Boy';
SELECT id FROM movies WHERE name == 'Sense and Sensibility';
SELECT id FROM actors WHERE name == 'Hugh Grant';
SELECT id FROM actors WHERE name == 'Toni Collette';
SELECT id FROM actors WHERE name == 'James Caan';
SELECT id FROM actors WHERE name == 'Emma Thompson';
CREATE TABLE roles (name, movieID, actorID);
// Note: ids used in the following four lines are the ones returned by the previous SELECT queries
INSERT INTO roles VALUES ('Edward', 3, 1);
INSERT INTO roles VALUES ('Frank', 1, 3);
INSERT INTO roles VALUES ('Fiona', 2, 2);
INSERT INTO roles VALUES ('Elinor', 3, 4);

SELECT * FROM actors WHERE (awards > 5) AND ((nationality == 'British') OR (nationality == 'Australian'));
[OK]
id	name			nationality	awards
2	Toni Collette	Australian	12
4	Emma Thompson	British		10

JOIN actors AND roles ON id AND actorID;


## Syntax 
    <sqlCompiler.sqlCommands>        ::=  <CommandType>;

    <CommandType>    ::=  <Use> | <Create> | <Drop> | <Alter> | <Insert> |
                      <Select> | <Update> | <Delete> | <Join>

    <Use>            ::=  USE <DatabaseName>

    <Create>         ::=  <CreateDatabase> | <CreateTable>

    <CreateDatabase> ::=  CREATE DATABASE <DatabaseName>

    <CreateTable>    ::=  CREATE TABLE <TableName> | CREATE TABLE <TableName> ( <AttributeList> )

    <Drop>           ::=  DROP <Structure> <StructureName>

    <Structure>      ::=  DATABASE | TABLE

    <Alter>          ::=  ALTER TABLE <TableName> <AlterationType> <AttributeName>

    <Insert>         ::=  INSERT INTO <TableName> VALUES ( <ValueList> )

    <Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
                          SELECT <WildAttribList> FROM <TableName> WHERE <Condition> 

    <Update>         ::=  UPDATE <TableName> SET <NameValueList> WHERE <Condition> 

    <Delete>         ::=  DELETE FROM <TableName> WHERE <Condition>

    <Join>           ::=  JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>

    <NameValueList>  ::=  <NameValuePair> | <NameValuePair> , <NameValueList>

    <NameValuePair>  ::=  <AttributeName> = <Value>

    <AlterationType> ::=  ADD | DROP

    <ValueList>      ::=  <Value>  |  <Value> , <ValueList>

    <Value>          ::=  '<StringLiteral>'  |  <BooleanLiteral>  |  <FloatLiteral>  |  <IntegerLiteral>

    <BooleanLiteral> ::=  true | false

    <WildAttribList> ::=  <AttributeList> | *

    <AttributeList>  ::=  <AttributeName> | <AttributeName> , <AttributeList>

    <Condition>      ::=  ( <Condition> ) AND ( <Condition> )  |
                          ( <Condition> ) OR ( <Condition> )   |
                          <AttributeName> <Operator> <Value>

    <Operator>       ::=   ==   |   >   |   <   |   >=   |   <=   |   !=   |   LIKE
