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

THIS_YEAR="2021"
THIS_DAY=`date +%d`

# Past 8pm, assume I'm actually starting "tomorrow" (timezones! woo!)
if [ `date +%H` -ge 20 ]; then
    THIS_DAY=$(($THIS_DAY+1))
fi

read -r -p "Year (${THIS_YEAR}): " YEAR
read -r -p "Day (${THIS_DAY}): " DAY
read -r -p "Slug: " -a SLUG_PARTS

if [ -z "${YEAR}" ]; then
    YEAR=${THIS_YEAR}
fi

if [ -z "${DAY}" ];then
    DAY=${THIS_DAY}
fi
if [ "${#DAY}" = "1" ]; then
    DAY="0${DAY}"
fi

SLUG=""
CAMEL=""
for i in "${SLUG_PARTS[@]}"; do
    if [ -n "${SLUG}" ]; then
        SLUG="${SLUG}_"
    fi
    SLUG="${SLUG}${i}"
    CAMEL="${CAMEL}${i^}"
done

if [ -z "${SLUG}" ];then
    echo "You must enter a slug" >&2
    exit 1
fi

DAY_DIR="day${DAY}_${SLUG}"

git checkout -b "${DAY_DIR}" master

root="kotlin"
res_root="resources"
pkg=""
if [ "${YEAR}" != "${THIS_YEAR}" ]; then
    root="${root}/aoc${YEAR}"
    res_root="${res_root}/aoc${YEAR}"
    pkg="aoc${YEAR}."
fi

mkdir -p src/main/$res_root/${DAY_DIR}
if [ ! -f src/main/$res_root/${DAY_DIR}/input.txt ]; then
    echo "  oh hai  " > src/main/$res_root/${DAY_DIR}/input.txt
fi
mkdir -p src/main/$root/${DAY_DIR}
cat > src/main/$root/${DAY_DIR}/${CAMEL}.kt << EOF
package ${pkg}${DAY_DIR}

/**
 * todo: add notes
 */
fun main() {
    util.solve(::partOne)
//    util.solve(::partTwo)
}

fun partOne(input: String) = input.length

fun partTwo(input: String) = input.trim().length
EOF

mkdir -p src/test/$root/${DAY_DIR}
cat > src/test/$root/${DAY_DIR}/${CAMEL}KtTest.kt << EOF
package ${pkg}${DAY_DIR}

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    input
""".trimIndent()

internal class ${CAMEL}KtTest {

    @Test
    fun partOne() {
        assertEquals(0, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
EOF

sed -e "s~/\*INJECT:IMPORT\*/~import ${pkg}${DAY_DIR}.main as ${DAY_DIR}\\n/*INJECT:IMPORT*/~" \
    -e "s~/\*INJECT:REF\*/~//::${DAY_DIR},\\n    /*INJECT:REF*/~" \
    -i src/main/kotlin/main.kt

git add src/main/kotlin src/test
git commit -m "skeleton"

# don't include this in the skeleton commit, but add it so it's ready
git add src/main/$res_root/${DAY_DIR}/input.txt

if [[ $SCRIPT = .* ]]; then
  rm $SCRIPT
fi
