set -e

if ! [ -f 'moar_example_app_config.json' ]; then 
  echo 'please create moar_example_app_config.json as per README.md'
  exit 1
fi
 
./gradlew run
