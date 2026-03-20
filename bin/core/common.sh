#!/usr/bin/env bash

if [ -n "$__WOLF__CORE_COMMON__" ]; then
    return
fi
__WOLF__CORE_COMMON__='core/common.sh'

CORE_PATH=$(cd `dirname $0`; pwd)
. ${CORE_PATH}/log.sh

#====================== echo ======================
echo_step() {
    # Usage: echo_step  "1. this is the step 1"
    echo -e '\033[0;32m'"$1"'\033[0m'
}

echo_separator() {
    # Usage: echo_separator
    echo "===================================================="
}

echo_in_processing_bar() {
    # Usage: bar 1 10
    #            ^----- Elapsed Percentage (0-100).
    #               ^-- Total length in chars.
    ((elapsed=$1*$2/100))

    # Create the bar with spaces.
    printf -v prog  "%${elapsed}s"
    printf -v total "%$(($2-elapsed))s"

    printf '%s\r' "[${prog// /-}${total}]"
}




#====================== action ======================

exit_0() {
    # Usage: do_exit_0 "the log message before exit 0"
    if [ ! -z "$1" ]
    then
        log_info "$1"
    fi
    exit 0
}

exit_1() {
    # Usage: do_exit_0 "the log message before exit 1"
    if [ ! -z "$1" ]
    then
        log_info "$1"
    fi
    exit 1
}

#====================== confirm======================
confirm() {
    # Usage: x=$(confirm "do you want to continue?")
    #        if [ "$x" = "yes" ]
    QUESTION="$1"
    read -p "${QUESTION} [yN] " ANSWER
    if [[ "${ANSWER}" == "y" ]] || [[ "${ANSWER}" == "Y" ]]
    then
        echo "yes"
    else
        echo "no"
    fi
}


#====================== if ======================

#====================== if-then ======================
# exit

exit_if_error() {
    # Usage: exit_if_error $? "fail, and exit"
    if [ "$1" -ne 0 ]
    then
        log_error "$2"
        exit 1
    fi
}

exit_if_empty() {
    # Usage: exit_if_empty ${1} "param 1 required"
    if [ -z "$1" ]
    then
        log_error "$2"
        exit 1
    fi
}

return_default_if_empty() {
    # Usage: A=$(if_empty_return_default "${1}" 123)
    if [ -z "${1}" ]
    then
        echo "${2}"
    else
        echo "${1}"
    fi
}


log_warning_if_empty() {
    # Usage: log_warning_if_empty "$1" "the param 1 is empty"
    if [ -z "${1}" ]
    then
        log_warnning "${2}"
    fi
}


#====================== is ======================

is_command_exists () {
    type "$1" &> /dev/null ;
}








#====================== array ======================
# reference https://github.com/dylanaraps/pure-bash-bible
array_reverse() {
    # Usage: array_reverse "array"
    shopt -s extdebug
    f()(printf '%s\n' "${BASH_ARGV[@]}"); f "$@"
    shopt -u extdebug
}

array_remove_duplicate() {
    # Usage: array_remove_duplicate "array"
    declare -A tmp_array

    for i in "$@"; do
        [[ "$i" ]] && IFS=" " tmp_array["${i:- }"]=1
    done

    printf '%s\n' "${!tmp_array[@]}"
}

array_random_element() {
    # Usage: array_random_element "array"
    local arr=("$@")
    printf '%s\n' "${arr[RANDOM % $#]}"
}

#====================== program ======================

run_command_in_background() {
    # Usage: run_command_in_background ./some_script.sh
    (nohup "$@" &>/dev/null &)
}

#====================== others ======================
generate_uuid() {
    # Usage: generate_uuid
    C="89ab"

    for ((N=0;N<16;++N)); do
        B="$((RANDOM%256))"

        case "$N" in
            6)  printf '4%x' "$((B%16))" ;;
            8)  printf '%c%x' "${C:$RANDOM%${#C}:1}" "$((B%16))" ;;

            3|5|7|9)
                printf '%02x-' "$B"
            ;;

            *)
                printf '%02x' "$B"
            ;;
        esac
    done

    printf '\n'
}