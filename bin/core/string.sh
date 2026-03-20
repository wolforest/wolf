#!/usr/bin/env bash

if [ -n "$__WOLF__CORE_STRING__" ]; then
    return
fi
__WOLF__CORE_STRING__='core/string.sh'


function is_blank() {
  if [ -z "$1" ]; then
      echo "FALSE"
      return 0
  fi

  echo "TRUE"
  return 1
}

function not_blank() {
  if [ -z "$1" ]; then
      echo "TRUE"
      return 1
  fi

  echo "FALSE"
  return 0
}

function has_prefix() {
  if [ $# != 2 ]; then
      echo "FALSE"
      return 0
  fi

  if [[ "$1" == ${2}* ]]; then
      echo "TRUE"
      return 1
  fi

  echo "FALSE"
  return 0
}

# reference https://github.com/dylanaraps/pure-bash-bible

string_trim() {
    # Usage: string_trim "   example   string    "
    : "${1#"${1%%[![:space:]]*}"}"
    : "${_%"${_##*[![:space:]]}"}"
    printf '%s\n' "$_"
}

string_split() {
   # Usage: string_split "string" "delimiter"
   IFS=$'\n' read -d "" -ra arr <<< "${1//$2/$'\n'}"
   printf '%s\n' "${arr[@]}"
}

string_lstrip() {
    # Usage: string_lstrip "string" "pattern"
    printf '%s\n' "${1##$2}"
}

string_rstrip() {
    # Usage: string_rstrip "string" "pattern"
    printf '%s\n' "${1%%$2}"
}

# Requires bash 4+
string_to_lower() {
    # Usage: string_to_lower "string"
    printf '%s\n' "${1,,}"
}

# Requires bash 4+
string_to_upper() {
    # Usage: string_to_upper "string"
    printf '%s\n' "${1^^}"
}

string_contains() {
    # Usage: string_contains hello he
    [[ "${1}" == *${2}* ]]
}

string_starts_with() {
    # Usage: string_starts_with hello he
    [[ "${1}" == ${2}* ]]
}


string_ends_with() {
    # Usage: string_ends_wit hello lo
    [[ "${1}" == *${2} ]]
}


string_regex() {
    # Usage: string_regex "string" "regex"
    [[ $1 =~ $2 ]] && printf '%s\n' "${BASH_REMATCH[1]}"
}


