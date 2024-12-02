--Select the last policy version for each policy

-- What does "last policy version" even mean? The policy version with the most
-- recent start date? Or the most recent expiration date?

-- The algorithm could be something like this:
--		for each policy:
--			select all policy_versions where policy_id = policy.id
--			sort selected policy_versions by start_date descending
--			pick the top one and discard the rest

select policy.*, version.* from policy
	inner join (
		select * from policy_version
			where policy_version.id = policy.id
			order by policy_version.start_date desc
			limit 1
		) as version on policy.id = policy.policy_id;
		
-- I have no idea if this is even compiles, since I don't have a database to
-- test this on.