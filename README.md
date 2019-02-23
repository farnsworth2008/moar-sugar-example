# Moar Sugar Example App

Stuff to make Java sweet!

Setup
-----

The setup for this project has been tested on MacOS 10.14.3 (18D109), OpenJDK 11.0.2, MySQL 8.0.12, Node v11.7.0, and NPM 6.8.0.

The moar-sugar module scripts require install into `~/modules`!

```bash
mkdir -p ~/modules
cd ~/modules
git clone git@github.com:moar-stuff/moar-sugar
cd moar-sugar
./build.sh
cd ..
```

Also install the example project.

```bash
cd ~/modules
git clone git@github.com:moar-stuff/moar-sugar-example
cd moar-sugar-example
moar-init
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
echo '{
host: "localhost:3306"
db: moar_sugar_example_db
user: moar_example_user
password: moar_example_password
}' | moar-json > moar_example_app_config.json
```

With the database and config file you can run the sample.

```
./run.sh
```
