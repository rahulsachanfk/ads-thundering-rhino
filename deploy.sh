#!/bin/bash
git tag -m "Promoting ad/thundering-rhino to nm" release/nm-`date +%s`-nodeploy
git push --tags