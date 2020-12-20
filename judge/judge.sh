#!/bin/bash

function invalid_usage() {
  echo "
    Invalid usage of judge.
    Type 'judge help' for usage guide.
  "

  exit 1
}

function clean_workdir() {
  cd .. || exit 1

  rm -rf "$1"
}

function exec_and_get_time_ms() {
  start=$(date +%s%3N)

  eval "$1"

  end=$(date +%s%3N)

  echo $((end - start))
}

function send_verdict() {
  if [ $# -eq 3 ]; then
    clean_workdir "$3"
  fi

  json=$(jq -c -n --arg verdict "$1" --arg message "$2" '{verdict: $verdict, message: $message}')

  echo "$json"

  exit 0
}

function test() {
  cd /examples || exit 1

  javac "$1.java" >/dev/null 2>&1

  time=$(exec_and_get_time_ms "java $1 >/dev/null 2>&1")

  send_verdict "AC" "Finished in $time ms."
}

function check_error() {
  if [ -s ./error ]; then
    send_verdict "$1" "$2" "$3"
  fi
}

function check_diff() {
  diff -Z -b output expected_output >dif

  if [ -s ./dif ]; then
    send_verdict "WA" "Wrong Answer" "$1"
  fi
}

function compile() {
  case $1 in
  JAVA)
    mv ./code ./Main.java
    javac Main.java 2>error
    ;;

  C)
    mv ./code ./code.c
    gcc code.c -o code 2>error
    ;;

  CPP)
    mv ./code ./code.cpp
    g++ code.cpp -o code 2>error
    ;;

  PYTHON)
    mv ./code ./code.py
    python -m py_compile code.py 2>error
    ;;

  PYTHON3)
    mv ./code ./code.py
    python3 -m py_compile code.py 2>error
    ;;
  esac

  check_error "CE" "Compilation Error" "$2"
}

function execute() {
  case $1 in
  JAVA)
    cmd="timeout $2.05 java Main <input >output 2>error"
    ;;

  C)
    cmd="timeout $2.05 ./code <input >output 2>error"
    ;;

  CPP)
    cmd="timeout $2.05 ./code <input >output 2>error"
    ;;

  PYTHON)
    cmd="timeout $2.05 python code.py <input >output 2>error"
    ;;

  PYTHON3)
    cmd="timeout $2.05 python3 code.py <input >output 2>error"
    ;;
  esac

  if ! eval "$cmd"; then
    send_verdict "TLE" "Time Limit Exceeded" "$3"
  fi

  check_error "RE" "Runtime Error" "$3"
}

# ===================================

# Usage:
#   judge run <JAVA|C|CPP|PYTHON|PYTHON3> <CODE> <INPUT> <OUTPUT> <TIME_LIMIT>
#   judge test <LIGHT|AVERAGE|HEAVY>

if [ $# -eq 0 ]; then
  invalid_usage
fi

case $1 in
run)
  if [ $# -ne 6 ]; then
    invalid_usage
  fi

  case $2 in
  JAVA | C | CPP | PYTHON | PYTHON3)
    dir="dir-$(date +%s)-$RANDOM"
    mkdir "$dir"
    cd "$dir" || exit 1

    echo "$3" | base64 --decode >code
    echo "$4" | base64 --decode >input
    echo "$5" | base64 --decode >expected_output

    compile "$2" "$dir"

    execute "$2" "$6" "$dir"

    check_diff "$dir"

    send_verdict "AC" "Accepted" "$dir"
    ;;

  *)
    invalid_usage
    ;;
  esac
  ;;

test)
  if [ $# -ne 2 ]; then
    invalid_usage
  fi

  case $2 in
  LIGHT | AVERAGE | HEAVY)
    test "$2"
    ;;

  *)
    invalid_usage
    ;;
  esac
  ;;

*)
  invalid_usage
  ;;
esac
