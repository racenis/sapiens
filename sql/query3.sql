--Select all parties with a number of policies for each

-- Like any number?
-- As in parties that have at least a single policy referencing them?



select * from party								-- 1. we take all parties
	inner join policy 							-- 2. attach their policies and discard
		on party.party_id = policy.policy_id	--    those that don't have any policies
	group by party.party_id						-- 3. remove duplicate parties
	
-- I don't know how correct this is, I don't have a database to test this on