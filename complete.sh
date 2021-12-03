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

REMOTE_BRANCH=`git rev-parse --abbrev-ref --symbolic-full-name @{u} 2> /dev/null || true`
DELETE_REMOTE="false"

if [ -n "$REMOTE_BRANCH" ]; then
    LOCAL_SHA=`git branch -v | grep $BRANCH | cut -d ' ' -f 3`
    REMOTE_SHA=`git branch -rv | grep $REMOTE_BRANCH | cut -d ' ' -f 4`
    if [ "$LOCAL_SHA" = "$REMOTE_SHA" ]; then
        DELETE_REMOTE="true"
    fi
fi

sed -e "s~//::${BRANCH},~::${BRANCH},~" \
    -i src/main/kotlin/main.kt
git commit -a -m "add to super-main"

git checkout master
git merge --no-ff ${BRANCH} -m "Merge branch '${BRANCH}'"
git push
if [ "$DELETE_REMOTE" = "true" ]; then
    git push `echo $REMOTE_BRANCH | cut -d / -f 1` :`echo $REMOTE_BRANCH | cut -d / -f 2-`
fi
git branch -D ${BRANCH}

if [[ $SCRIPT = .* ]]; then
  rm $SCRIPT
fi
