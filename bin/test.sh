#!/usr/bin/env bash

function test_framework() {
    cd "${WOLF_FRAMEWORK_DIR}/test" || exit
    mvn clean compile test-compile test
}

function test_app() {
    local app=$2
    local maven_profile=$3
    local spring_profile=$maven_profile

#    if [[ $maven_profile = *"-"* ]]; then
#        spring_profile=${maven_profile#*-}
#    fi

    local suite=$4
    if [ -n "$4" ]; then
        suite=$4
    else
        suite="all"
    fi

    local test_dir="${wolf_app_dir[$app]}/test"

    # Compatible with win monolithic app
    if [ ! -d "$test_dir" ]; then
        test_dir="${wolf_app_test_dir[$app]}"
    fi

    cd "${test_dir}" || exit
    _info "current work dir: ${test_dir}"
    _info "mvn clean compile test-compile test -P$maven_profile -DsuiteXmlFile=$suite -Dspring.profiles.active=$spring_profile"
    #exit
    mvn clean compile test-compile test -P$maven_profile -DsuiteXmlFile=$suite -Dspring.profiles.active=$spring_profile
}

function main() {
    # win test app-name env suite
    #      $1   $2       $3  $4
    if [ -z "$1" ] || [ "$1" != "test" ]; then
        echo "please input the right command: win test {app-name} {env}"
        exit
    fi

    if [ -z "$2" ]; then
        echo "please input app-name"
        exit
    fi

    case "$2" in
    framework)
        test_framework
        ;;
    *)
        local test_function_name="test_$2"
        if type "$test_function_name" &>/dev/null; then
            eval "$test_function_name"
        else
            test_app "$@"
        fi
        ;;
    esac
}

main "$@"
