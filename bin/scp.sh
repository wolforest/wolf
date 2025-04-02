#!/usr/bin/env bash

function get_app_final_name() {
    local app=$1
    local env=$2

    local key="$app-$env"

    # shellcheck disable=SC2154
    local final_name="${wolf_app_final_name[$key]}"

    if [ -z "$final_name" ]; then
        final_name="${wolf_app_final_name[$app]}"
    fi

    if [ -z "$final_name" ]; then
        final_name="$app"
    fi

    echo "$final_name"
}

function scp_app() {
    local app=$1
    local env=$2

    local final_name
    get_app_final_name "$app" "$env"
    final_name=$(get_app_final_name "$app" "$env")

    do_scp "$app" "$env" "$final_name"
    # sync_app "$app" "$env"
}

function sync_app() {
    local app=$1
    local env=$2

    check_need_sync "$app" "$env"

    local cmd
    cmd=$(get_sync_command "$app" "$env")

    do_sync "$cmd" "$env"
}

function do_scp() {

    local app_alias=$1
    local env=$2
    local final_name=$3

    local host
    host=$(get_scp_host "$env")

    if [ -z $host ]; then
        _error "Unknown env: $env"
        exit 1
    fi

    # shellcheck disable=SC2154
    local app_package_dir="${wolf_app_package_dir[$app_alias]}"
    if [ -z "$app_package_dir" ]; then
        # shellcheck disable=SC2154
        app_package_dir="${wolf_app_dir[$app_alias]}"
    fi

    cd "${app_package_dir}" || exit
    # shellcheck disable=SC2154
    if ! $debug_mode && ! $dry_run; then
        mvn -T 1C -DskipTests clean package -P"$env"
    fi

    local git_branch_name
    git_branch_name=$(git symbolic-ref --short HEAD | cut -d "/" -f2)

    local git_commit_version
    git_commit_version=$(git rev-parse --short HEAD)

    # file_name var should be global
    file_name=$final_name.tar.gz.$(date '+%Y%m%d%H%M')."$git_branch_name"."$git_commit_version"."$USER"
    local file_name_tmp
    file_name_tmp=$final_name.tmp.$(date '+%Y%m%d%H%M')."$git_branch_name"."$git_commit_version"."$USER"

    if $debug_mode; then
        echo "git_branch_name: $git_branch_name"
        echo "git_commit_version: $git_commit_version"
        echo "file_name: $file_name"
        echo "file_name_tmp: $file_name_tmp"
        exit
    fi

    _info "scp log: $file_name_tmp transporting..."

    if $dry_run; then
        _info "will execute: ssh -A app@$host echo -e $(date '+%Y-%m-%d %H:%M:%S') $USER  scp  $final_name...  prepare    $file_name>>/home/app/target/deploy.log"
        _info "will execute: scp ${app_package_dir}/target/$final_name.tar.gz app@$host:/home/app/target/$final_name/$file_name_tmp"
        _info "scp log: rename the tmp file to $file_name..."
        _info "will execute: ssh -A app@$host mv /home/app/target/$final_name/$file_name_tmp /home/app/target/$final_name/$file_name && echo -e $(date '+%Y-%m-%d %H:%M:%S') $USER  scp  $final_name...  finished   $file_name>>/home/app/target/deploy.log"
    else
        ssh -A app@"$host" "echo -e $(date '+%Y-%m-%d %H:%M:%S') $USER  scp  $final_name...  prepare    $file_name>>/home/app/target/deploy.log"
        scp "${app_package_dir}/target/$final_name.tar.gz" \
            app@"$host":/home/app/target/"$final_name"/"$file_name_tmp"

        _info "scp log: rename the tmp file to $file_name..."
        ssh -A app@"$host" "mv /home/app/target/$final_name/$file_name_tmp /home/app/target/$final_name/$file_name && echo -e $(date '+%Y-%m-%d %H:%M:%S') $USER  scp  $final_name...  finished   $file_name>>/home/app/target/deploy.log"
    fi
}

function do_sync() {
    local cmd=$1
    local env=$2
    local host
    host=$(get_scp_host "$env")

    if [ -z $host ]; then
        _error "Unknown env: $env"
        exit 1
    fi

    _info "scp log: $file_name $env from server: $host sync to other servers..."

    if $dry_run; then
        _info "will execute: ssh -A app@$host /home/app/bin/win $cmd"
        _info "will execute: ssh -A app@$host echo -e $(date '+%Y-%m-%d %H:%M:%S') $USER  $cmd finished $file_name>>/home/app/target/deploy.log"
    else
        ssh -A app@"$host" "/home/app/bin/win $cmd"
        ssh -A app@"$host" "echo -e $(date '+%Y-%m-%d %H:%M:%S') $USER  $cmd  finished   $file_name>>/home/app/target/deploy.log"
    fi
}

function check_need_sync() {
    local app=$1
    local current_env=$2

    local env_list
    # shellcheck disable=SC2154
    env_list="${wolf_app_sync_env[$app]}"
    if [ -z "$env_list" ]; then
        exit 1
    fi

    local env_arr;
    IFS=","
    read -ra env_arr <<< "$env_list"

    for env in "${env_arr[@]}"
    do
        if [ "$env" == "$current_env" ]; then
            return 0
        fi
    done

    #_info "no host to sync."
    exit 0
}

function get_sync_command() {
    local app=$1
    local env=$2
    # shellcheck disable=SC2154
    local cmd="${wolf_app_sync_command[${app}-$env]}"
    echo "$cmd"
}

function get_scp_host() {
    local env=$1
    # shellcheck disable=SC2154
    local host="${wolf_app_scp_host[$env]}"
    echo "$host"
}



function main() {
    if [ -z "$1" ] || { [ "$1" != "release" ] && [ "$1" != "sync" ] && [ "$1" != "scp" ]; }; then
        _error "Error: Invalid argument. Usage: win release {app_name} {env}"
        exit 1
    fi

    if [ -z "$2" ]; then
        _error "Error: Invalid argument. Usage: win release {app_name} {env}"
        exit
    fi

    if [ -z "$3" ]; then
        _error "Error: Invalid argument. Usage: win release {app_name} {env}"
        exit
    fi

    case "$1" in
    release)
        scp_app "$2" "$3"
        ;;
    esac
}

main "$@"
