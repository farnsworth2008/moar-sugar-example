set -e
git clone git@github.com:farnsworth2008/moar-sugar
cp -r moar-sugar/gradle .
cp moar-sugar/gradlew .
./gradlew build
cd moar-sugar/cli
npm install babel-register babel-preset-env --save
npm run-script build
npm install -g
