# Moar Sugar Example App

Stuff to make Java sweet!

Setup
-----

```bash
git clone git@github.com:farnsworth2008/moar-sugar-example
cd moar-sugar-example
./init.sh
```

To run all the examples, you need a mysql database.

```sql
echo "CREATE DATABASE moar_sugar_example_db;
CREATE USER 'moar_example_user'@'localhost' IDENTIFIED BY 'moar_example_password';
GRANT ALL PRIVILEGES ON moar_sugar_example_db.* TO 'moar_example_user'@'localhost';" \
| mysql -u root
```

We also need a config file.

```
echo "[
db-host: localhost,
db-port: 3306,
db-name: moar_sugar_example_db,
db-user: moar_example_user,
db-password: moar_example_password
]" | moar-json > moar_example_app_config.json
```

With the database and config file you can run the sample.

```
./gradlew run
```
