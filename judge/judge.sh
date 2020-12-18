#!/bin/bash

# judge <JAVA|C|CPP|PYTHON|PYTHON3> <CODE> <INPUT> <OUTPUT>
# judge test <LIGHT|AVERAGE|HEAVY>

# Handle test command separately
case $1 in
test)
  cd /examples
  javac "$2.java" >/dev/null 2>&1
  java "$2" >/dev/null 2>&1
  rm ./*.class
  echo "Done"
  exit 0
  ;;
esac

# Checks for 'Compilation Error (CE)' and 'Runtime Error (RE)', and sends verdict if error found
function check_error() {
  if [ -s ./error ]; then
    send_verdict "$1" "$(cat ./error)"
  fi
}

# Checks for differences between output and intended output, and sends verdict if difference found
function check_diff() {
  diff -b output expected_output >dif
  if [ -s ./dif ]; then
    send_verdict "WA" "Program outputs different result."
  fi
}

# Outputs verdict JSON
# The message inside needs to be decoded as well after decoding JSON
function send_verdict() {
  json=$(jq -c -n --arg verdict "$1" --arg message "$2" '{verdict: $verdict, message: $message}')
  echo "$json"
  exit 0
}

# Create a random directory to be our work directory
dir="dir-$(date +%s)-$RANDOM"
mkdir "$dir"
cd "$dir"

# Copy all necessary files to work directory
# All data are base64 encoded and should be decoded before copy
echo "$2" | base64 --decode >code
echo "$3" | base64 --decode >input
echo "$4" | base64 --decode >expected_output

# Compile and execute code depending on the programming language
case $1 in
JAVA)
  mv ./code ./Main.java
  javac Main.java 2>error
  check_error "CE"
  java Main <input >output 2>error
  ;;
C)
  mv ./code ./code.c
  gcc code.c -o code 2>error
  check_error "CE"
  ./code <input >output 2>error
  ;;
CPP)
  mv ./code ./code.cpp
  g++ code.cpp -o code 2>error
  check_error "CE"
  ./code <input >output 2>error
  ;;
PYTHON)
  mv ./code ./code.py
  python -m py_compile code.py 2>error
  check_error "CE"
  python code.py <input >output 2>error
  ;;
PYTHON3)
  mv ./code ./code.py
  python3 -m py_compile code.py 2>error
  check_error "CE"
  python3 code.py <input >output 2>error
  ;;
esac

check_error "RE"
check_diff

# Delete work directory
cd ..
rm -rf "$dir"

send_verdict "AC" "All tests passed"
