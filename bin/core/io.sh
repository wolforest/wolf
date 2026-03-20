#!/usr/bin/env bash

if [ -n "$__WOLF__CORE_IO__" ]; then
    return
fi
__WOLF__CORE_IO__='core/io.sh'

. common.sh

#====================== dir  ======================

rmdir() {
    # Usage: rmdir "/tmp/abc"
    if [ -e "$1" ]
    then
        rm -rf "$1"
        local rc=$?
        if [ "$rc" -ne 0 ]
        then
            log_error "rmdir: fail, when do [rm -rf ${1}]"
            return "$rc"
        fi
    fi
    mkdir -p "$1"
    return $?
}

exit_if_path_not_exists() {
    # Usage: if_path_not_exists_then_exit "/tmp/a.txt" "/tmp/a.txt is not exists"
    if [ ! -e "$1" ]
    then
        log_error "$2"
        exit 1
    fi
}

# action
touch_file_if_not_exists() {
    # Usage: touch_file_if_not_exists "/tmp/a.txt"
    [ -e "$1" ] || touch "$1"
    return $?
}

mkdir_if_not_exists() {
    # Usage: mkdir_if_not_exists "/tmp/abc"
    [ -d "$1" ] || mkdir -p "$1"
    return $?
}

rm_file_if_exists() {
    # Usage: rm_file_if_exists "/tmp/a.txt"
    if [ -e "$1" ]
    then
        rm "$1"
        return $?
    fi
}

rm_if_exists() {
    rm_dir_if_exists $1
}

rm_dir_if_exists() {
    # Usage: if_dir_exists_then_remove "/tmp/abc"
    if [ -e "$1" ]
    then
        rm -rf "$1"
        return $?
    fi
}

function mv_if_exists() {
    if [ -e "$1" ]
    then
        mv "$1" "$2"
        return $?
    fi
}

function cp_if_exists() {
    if [ -e "$1" ]
    then
        cp -r "$1" "$2"
        return $?
    fi
}

#====================== tar ======================
do_tar() {
    # Usage: do_tar example.tar.gz example
    PKG_NAME="${1}"
    DIR="${2}"
    rm_file_if_exists "${PKG_NAME}"
    tar -czf "${PKG_NAME}" "${DIR}"
    exit_if_error "$?" "tar -czf ${PKG_NAME} ${DIR} fail"
}