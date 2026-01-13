import os
import re
import subprocess
import tempfile

kotlin_root = os.path.join(os.path.dirname(__file__), "..", "src", "main", "kotlin")


def get_solvers():
    solvers_fn = os.path.join(kotlin_root, "main.kt")
    with open(solvers_fn, "r", encoding="utf-8") as file:
        result = []
        for line in file:
            # import aoc2020.day05_binary_boarding.main as day05_binary_boarding
            m = re.match(r"import (aoc(\d\d\d\d)\.day(\d\d)_.*)\.main +as.*", line)
            if m:
                result.append(
                    (int(m.group(2)), int(m.group(3)), m.group(1)),
                )
                continue
            # import day01_sonar_sweep.main as day01_sonar_sweep
            m = re.match(r"import (day(\d\d)_.*)\.main +as.*", line)
            if m:
                result.append((2021, int(m.group(2)), m.group(1)))
                continue
    return [r for r in result if r[1] >= 1]


def java_dir():
    return os.path.join(os.path.dirname(__file__), "..")


def java_env():
    return {"JAVA_VERSION": "11"}


def support():
    subprocess.run(
        ["./mvnw", "compile", "--quiet"],
        cwd=java_dir(),
        env=java_env(),
        text=True,
    )
    return [(y, d) for y, d, n in get_solvers()]


def solve(year, day, data):
    s = [s for s in get_solvers() if s[0] == year and s[1] == day]
    if len(s) == 0:
        raise TypeError(f"no solver for {year}/{day} is known.")
    if len(s) > 1:
        raise TypeError(f"Found {len(s)} solvers for {year}/{day}?!")
    path = s[0][2]
    with tempfile.NamedTemporaryFile(
        mode="w+t", prefix=f"input_{year}_{day}_", suffix=".txt"
    ) as fp:
        fp.write(data)
        fp.flush()
        proc = subprocess.run(
            [
                "./mvnw",
                "--quiet",
                "-e",
                f"-Daoc_all_file={fp.name}",
                "exec:java",
                f"-Dexec.args={path}",
            ],
            cwd=java_dir(),
            env=java_env(),
            capture_output=True,
            text=True,
        )
    answers = []
    nanos = None
    for p, ans in re.findall(
        r"\[__AOC_ALL_(ANSWER|NANOS)__\[([^]]+)]]", str(proc.stdout)
    ):
        if p == "ANSWER":
            # day 13 (Transparent Origami) is an ASCII-art answer
            answers.append(None if "\n" in ans else ans)
        else:
            nanos = int(ans)
    while len(answers) < 2:
        answers.append(None)
    return answers[0], answers[1], nanos
