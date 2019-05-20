#!/usr/bin/env bash

echo "Generating dropbox-deployment.yml"

rm -f dropbox-deployment.yml
( echo "deploy:";
  echo "  dropbox_path: /geb-reports/${TRAVIS_BUILD_NUMBER}";
  echo "  artifacts_path: build/geb-reports";
  echo "  debug: true";
) > dropbox-deployment.yml
