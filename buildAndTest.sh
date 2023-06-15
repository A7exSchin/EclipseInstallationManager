#!bin/bash
# activate bash checks for unset vars, pipe fails
set -eauo pipefail
script=$(readlink -f "$0")
script_dir=$(dirname "$script")

/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/klibio/bootstrap/main/install-klibio.sh)" bash -j
source ~/.klibio/klibio.sh
. set-java.sh 17

echo $JAVA_HOME


./gradlew \
    --info \
    --console=plain \
    clean \
    export \
    testrun.1-integration-pop

echo '### create Coverage and PoP reports'
. ${script_dir}/eim.pop/jacoco/createCoverageReports.sh
