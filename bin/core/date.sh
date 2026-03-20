#!/usr/bin/env bash

if [ -n "$__WOLF__CORE_DATE__" ]; then
    return
fi
__WOLF__CORE_DATE__='core/date.sh'


#====================== date ======================
get_today() {
    # Usage: x=$(get_today)
    echo "$(date +%Y%m%d)"
}

get_day_before_from_now() {
    # Usage: x=$(get_day_before_from_now 2)
    COUNT="$1"
    TODAY=$(date +%Y%m%d)
    echo $(date -d "$TODAY ${COUNT} days ago" +%Y%m%d)
}

get_day_before_from() {
    # Usage: y=$(get_day_before_from 20170905 2)
    FROM="$1"
    COUNT="$2"
    echo $(date -d "${FROM} ${COUNT} days ago" +%Y%m%d)
}

generate_date_list () {
    # Usage: generate_date_list 20120304 20120405
    TMP=$(date -d "$1" "+%Y%m%d")
    TO=$(date -d "$2" "+%Y%m%d")
    while [ "$(date -d $TO '+%s')" -ge "$(date -d $TMP '+%s')" ]; do
        echo $TMP
        TMP=$(date -d "$TMP 1day" "+%Y%m%d")
    done
}