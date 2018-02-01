# The Backend

The place where all the data bits are managed.

## Getting Started

So far all the set up required is

* installing maven
* installing postgres
* running postgres
* setting up the databases for Tourneynizer

You can look up how to the first 3. For the last one, `cd` into `backend/`, and run `./scripts/createDB.sh`. This just creates 3 databases. If this command fails you either haven't set up postgres properly or you did some password set up that I don't know about. If you did that you can probably look in `createDB.sh` and figure out what it does.

Now you have 3 databases: `tourneynizer-prod`, `tourneynizer-dev`, and `tourneynizer-test`. But they're all empty.

Now run `./scripts/migrate.sh`. Now all your databases will have tables in them. That's it for now!

For connecting to the database, you'll need to have the proper environment variables set. Look in the
(Dev/Prod/Test)DataSouce.xml to see which ones to set, or ask Porter for a .env file.
