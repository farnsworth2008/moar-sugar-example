# Initalization script for Moar Sugar Example
#
# This script should be run once on each 'clean' enviornment to fetch required
# non-gradle project dependencies and initalize the environment
# -----------------------------------------------------------------------------
set -e

# Check to make sure the workspace is clean
if ! [ -z "$(git status --porcelain)" ]; then
  echo 'Please clean'
  exit 1
fi

# Make the tree REALLY clean!
git clean -fdx

# Make sure dependencies are really gone
# These will be reobtained when we init
if [ -d moar-sugar ]; then
  echo 'Please remove moar-sugar'
  exit 2
fi

# Clone and initialize the dependencies
git clone git@github.com:farnsworth2008/moar-sugar
build=$(<init-moar-sugar)
cd moar-sugar
git checkout -b build $build
./init.sh
cp -r gradle ..
cp gradlew ..
cd ..

# Perform an initial build
echo "Initial Moar Sugar Example Build"
./build.sh
