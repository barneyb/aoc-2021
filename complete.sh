#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"
SCRIPT=`basename "$0"`
if [[ $SCRIPT != .* ]]; then
  cp $SCRIPT .$SCRIPT
  exec ./.$SCRIPT $*
fi

if [ `git status --porcelain src | wc -l` != "0" ]; then
    echo "Working copy is dirty. Clean it up first." >&2
    exit 2
fi

BRANCH=`git branch | egrep '^\*' | cut -c 3-`

if [ "${BRANCH}" = "master" ]; then
    echo "You can't complete from master, only a day's branch"
    exit 1
fi

git checkout master
git merge --no-ff ${BRANCH} -m "Merge branch '${BRANCH}'"
git branch -d ${BRANCH}
