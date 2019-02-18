# Moar Sugar Example App

Stuff to make Java sweet!

Setup
-----

After you clone this repo you need to run `./init.sh` to initialize things.  In particular, init will clone the `git@github.com:farnsworth2008/moar-sugar` that you need in order to build and run the example code in this repository. 

To run all the examples, you need a mysql database.

```sql
echo "CREATE DATABASE moar_sugar_example_db;
CREATE USER 'moar_example_user'@'localhost' IDENTIFIED BY 'moar_example_password';
GRANT ALL PRIVILEGES ON moar_sugar_example_db.* TO 'moar_example_user'@'localhost';" \
| mysql -u root
```

We also need a config file.

```
echo "{
\"host\": \"localhost:3306\",
\"db\": \"moar_sugar_example_db\",
\"user\": \"moar_example_user\",
\"password\": \"moar_example_password\"
}" > moar_example_app_config.json
```

With the database and config file you can run the sample.

```
./gradlew run
```
