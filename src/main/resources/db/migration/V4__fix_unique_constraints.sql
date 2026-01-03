-- Remove individual unique constraint on item_name (if it exists)
-- In MySQL, we need to know the constraint name. Usually it's 'item_name' or the column name if created via JPA.
-- We can drop it by column name by dropping the index.
ALTER TABLE mart_item DROP INDEX item_name;

-- Add composite unique constraint
ALTER TABLE mart_item
ADD UNIQUE INDEX uk_mart_item_name (mart_id, item_name);

-- Also fix online_item if it has same issue (it currently has unique on item_name)
-- Based on V1__initial_setup.sql, it also has UNIQUE.
-- If multiple users can have same item name but different brand/source, we might want to relax this too.
-- For now, let's keep it but be aware.