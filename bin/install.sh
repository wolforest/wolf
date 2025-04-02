#!/usr/bin/env bash

function install_framework() {
    cd "${WOLF_FRAMEWORK_DIR}" || exit
    git pull --ff || exit

    local dir="${WOLF_FRAMEWORK_DIR}"
    cd $dir && mvn -T 1C clean install

    return
}

function install_app() {
    local target=$2
    local app
    local module

    if [[ $target = *"/"* ]]; then
        app=${target%/*}
        module=${target#*/}
    else
        app=$target
        module=""
    fi

    # shellcheck disable=SC2154
    local app_dir="${wolf_app_dir[$app]}"
    # echo "install_app module:${module}, app:${app}, dir:${app_dir}"

    # Compatible with win monolithic app
    if [ -z "$app_dir" ]; then
        # shellcheck disable=SC2154
        app_dir="${wolf_app_package_dir[$app]}"
    fi

    echo "install app: ${app_dir}/$module";

    cd "${app_dir}/$module" || exit
    mvn -T 1C -DskipTests clean install
}

function main() {
    if [ -z "$1" ] || [ "$1" != "install" ]; then
        echo "please input the right command: win install module-name or win install app-name"
        exit
    fi

    if [ -z "$2" ]; then
        echo "please input module-name or app-name"
        exit
    fi

    case "$2" in
    framework | framework/*)
        install_framework
        ;;
    *)
        local install_function_name="install_$2"
        if type "$install_function_name" &>/dev/null; then
            eval "$install_function_name"
        else
            install_app "$@"
        fi
        ;;
    esac
}

main "$@"
