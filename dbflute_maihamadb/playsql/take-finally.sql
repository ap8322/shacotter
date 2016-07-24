-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Member addresses should be only one at any time.
-- - - - - - - - - - -/
select adr.MEMBER_ADDRESS_ID, adr.MEMBER_ID
     , adr.VALID_BEGIN_DATE, adr.VALID_END_DATE
     , adr.ADDRESS
  from MEMBER_ADDRESS adr
 where exists (select subadr.MEMBER_ADDRESS_ID
                 from MEMBER_ADDRESS subadr
                where subadr.MEMBER_ID = adr.MEMBER_ID
                  and subadr.VALID_BEGIN_DATE > adr.VALID_BEGIN_DATE
                  -- 有効期間が一日だけ重複しているパターンも検出
                  and subadr.VALID_BEGIN_DATE <= adr.VALID_END_DATE
       )
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- 正式会員日時を持ってる仮会員がいないこと
-- - - - - - - - - - -/
select *
  from MEMBER mem
 where mem.MEMBER_STATUS_CODE = 'PRV'
   and mem.FORMALIZED_DATETIME is not NULL
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- まだ生まれていない会員がいないこと
-- - - - - - - - - - -/
select *
  from MEMBER mem
 where mem.BIRTHDATE > now()
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- 退会会員が退会情報を持っていることをアサート
-- - - - - - - - - - -/
select *
  from MEMBER mem
 where mem.MEMBER_STATUS_CODE = 'WDL'
   and not exists (select wdl.MEMBER_ID
                     from MEMBER_WITHDRAWAL wdl
                    where wdl.MEMBER_ID = mem.MEMBER_ID
       )
;