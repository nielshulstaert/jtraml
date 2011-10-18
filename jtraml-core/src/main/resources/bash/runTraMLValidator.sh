#!/bin/sh

# script lives in doc/TraML relative to ATAQS_BASE, adjust if necessary.
cd ../..

export ATAQS_BASE=`pwd`
java -cp .:$ATAQS_BASE/doc/TraML/:$ATAQS_BASE/lib/:$ATAQS_BASE/bin/ATAQS.jar:$ATAQS_BASE/lib/validator-2.0.0-SNAPSHOT.jar:$ATAQS_BASE/lib/aebersoldlab_apache995.jar:$ATAQS_BASE/lib/ontology-manager-2.0.0-SNAPSHOT.jar:$ATAQS_BASE/lib/cv-rule-reader-2.0.0-SNAPSHOT.jar:$ATAQS_BASE/lib/ontology-config-reader-2.0.0-SNAPSHOT.jar:$ATAQS_BASE/lib/ols-1.14.1.jar:$ATAQS_BASE/lib/oboedit-1.101.jar:$ATAQS_BASE/lib/org.geneontology-1.101.jar:$ATAQS_BASE/lib/commons-jxpath-1.2.jar:$ATAQS_BASE/lib/object-rule-reader-2.0.0-SNAPSHOT.jar:$ATAQS_BASE/lib/traml_schema.jar:$ATAQS_BASE/lib/aebersoldlab_hibernate995.jar:$ATAQS_BASE/lib/log4j-1.2.14.jar  -Dlog4j.configuration="../../conf/log4j.properties" org.systemsbiology.apps.tramlvalidator.TraMLValidatorRunner inputfile=$ATAQS_BASE/doc/TraML/ToyExample1.TraML ataqs_bin=$ATAQS_BASE/doc/TraML/
