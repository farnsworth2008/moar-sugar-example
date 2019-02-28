set -e

if ! [ -f 'moar_example_app_config.json' ]; then 
  echo 'please create moar_example_app_config.json as per README.md'
  exit 1
fi

if [ -z "$1" ]; then
  filter='.*'
else
  filter="$1"
fi

./gradlew -Dmoar.ansi.enabled=true run --args "$filter"
