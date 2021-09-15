#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# save current path
REPO_PATH=$PWD

# Save some useful information
SHA=`git rev-parse --verify HEAD`

# Clone the existing gh-pages for this repo into $HOME/gh-pages/
# Create a new empty branch if gh-pages doesn't exist yet (should only happen on first deploy)
cd $HOME
git config --global user.email "circleci@circle-ci.org"
git config --global user.name "circle-ci"
git clone --quiet --branch=main https://${GH_PAGES_DEPLOY}@github.com/camel-tooling/camel-lsp-client-eclipse-update-site.git gh-pages > /dev/null
cd gh-pages

# delete existing data
rm -Rf updatesite/${DEPLOY_REPO_ROOT}/
# copy new data 
cp -r $REPO_PATH/com.github.camel-tooling.lsp.eclipse.updatesite/target/updatesite .
# Commit and Push the Changes
git add -f .
git status
git commit -m "Deploy ${DEPLOY_REPO_ROOT} update site to GitHub Pages: ${CIRCLE_SHA1} from Circle CI build $CIRCLE_BUILD_NUM"
git push -fq origin main > /dev/null
# return to original path
cd $REPO_PATH
