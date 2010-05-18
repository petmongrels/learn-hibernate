set term on
set serveroutput on
set echo on
set ver on
set feed on

define oracleDataDir='c:/oraclexe/data'
define userName='LearnHibernate'
create tablespace TS_&userName datafile '&oracleDataDir\TS_&userName..dbf' size 32m REUSE autoextend on next 5m;
whenever sqlerror exit -1
create user &userName identified by "&1" default tablespace TS_&userName;
alter user &userName account unlock;
grant dba,resource,connect,create session to &userName;
connect sys/&1@&2 as sysdba;
GRANT EXECUTE on SYS.UTL_FILE TO PUBLIC;
commit;
exit