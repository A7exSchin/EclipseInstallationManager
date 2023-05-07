#!/bin/bash
# activate bash checks for unset vars, pipe fails
set -eauo pipefail
SCRIPT=$(readlink -f "$0")
SCRIPT_DIR=$(dirname "$SCRIPT")


version=$(cat version)
tag=v${version}
branch=$(echo ${GITHUB_REF#refs/heads/})

if [[ ${branch} = "main" ]]; then
    gh release create ${tag}\
        --latest \
        --title "Eclipse Installation Manager (EIM) v${version}" \
        --generate-notes \
        ${GITHUB_WORKSPACE}/eim/generated/distributions/executable/eim.jar \
        ${GITHUB_WORKSPACE}/eim.api/generated/eim.api.jar
else
    echo "# Skipping release because the branch is not main"
fi