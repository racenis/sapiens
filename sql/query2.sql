--Select all policies that are active during the period from 14.10.2019 to 14.11.2019

-- What does it even mean for a policy to be "active"? Does it apply to all
-- dates between start_date and expiration_date?

-- The task gives a date range. Does being active during a period mean that the
-- policy was active for the whole period? What if it expires in the middle of
-- it, is it still considered to be active for that period?

-- One way to determine this would be to check:
-- 1. If the policy's start_date is before the period
-- 2. If the policy's expiration_date is after the period

select * from policy where start_date < date('2019-10-14')
					and expiration_date > date('2019-11-14');