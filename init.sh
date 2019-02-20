set -e
git clone git@github.com:farnsworth2008/moar-sugar || true
cd moar-sugar
./init.sh
cp -r gradle ..
cp gradlew ..
cd ..
./gradlew build eclipse
git checkout HEAD --  moar-sugar-app/.settings
