-- #df:begin#
create procedure SP_IN_OUT_PARAMETER(
	in v_in_varchar varchar(32)
	, out v_out_varchar varchar(32)
	, inout v_inout_varchar varchar(32)
)
begin
	set v_out_varchar = v_inout_varchar;
	set v_inout_varchar = v_in_varchar;
end;
-- #df:end#

-- #df:begin#
create procedure SP_RETURN_RESULT_SET(in birthdateFrom DATE)
begin
  select MEMBER_NAME, BIRTHDATE, MEMBER_STATUS_CODE
    from MEMBER
   where BIRTHDATE >= birthdateFrom
   order by BIRTHDATE desc, MEMBER_ID asc;
  select MEMBER_STATUS_CODE, MEMBER_STATUS_NAME
    from MEMBER_STATUS
   order by DISPLAY_ORDER;
end;
-- #df:end#
