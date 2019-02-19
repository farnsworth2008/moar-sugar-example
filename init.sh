set -e
git clone git@github.com:farnsworth2008/moar-sugar
./gradlew build
cd moar-sugar/cli
npm install -g
