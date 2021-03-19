#!/bin/bash

function parse_arguments() {
	# NAMESPACE
	if [ -z "${NAMESPACE}" ]; then
		echo "NAMESPACE not set. Using parameter \"$1\"";
		NAMESPACE=$1;
	fi
}

function create_namespace(){
  EXISTING_NS="$(oc project $NAMESPACE -q)"

  if [ "$EXISTING_NS" == "$NAMESPACE" ]; then
    echo "${NAMESPACE} already exists"
    oc project ${NAMESPACE}
  else
    oc new-project ${NAMESPACE}
  fi
}

function add_scc(){
  oc adm policy add-scc-to-user anyuid system:serviceaccount:${NAMESPACE}:default
  oc adm policy add-scc-to-user privileged system:serviceaccount:${NAMESPACE}:default
}

function create_secrets(){
  oc apply --recursive --filename secrets/
}

function create_configmaps(){
  oc apply --recursive --filename configmaps/
}

function deploy_db() {

  oc apply --recursive --filename deployments/

  # Check deployment rollout status every 10 seconds (max 10 minutes) until complete.
  ATTEMPTS=0
  MYSQL_ROLLOUT_STATUS_CMD="oc rollout status deployment/inventory-mysql -n ${NAMESPACE}"
  until $MYSQL_ROLLOUT_STATUS_CMD || [ $ATTEMPTS -eq 60 ]; do
    $MYSQL_ROLLOUT_STATUS_CMD
    ATTEMPTS=$((attempts + 1))
    sleep 10
  done

}

function create_services(){
  oc apply --recursive --filename services/
}

function populate_db() {

  oc apply --recursive --filename jobs/

  # Check deployment rollout status every 10 seconds (max 10 minutes) until complete.
  ATTEMPTS=0
  MYSQL_ROLLOUT_STATUS_CMD="oc wait --for=condition=complete --timeout=30s job/inventory-ms-spring-populate-mysql -n ${NAMESPACE}"
  until $MYSQL_ROLLOUT_STATUS_CMD || [ $ATTEMPTS -eq 60 ]; do
    $MYSQL_ROLLOUT_STATUS_CMD
    ATTEMPTS=$((attempts + 1))
    sleep 10
  done

}

# Setup
parse_arguments $1

echo "========================================================================="

# create project namespace
echo "Set namespace"
create_namespace
echo "========================================================================="

# Adding security constraints
echo "Adding security constraints"
add_scc
echo "========================================================================="

# Creating secrets
echo "Generating necessary secrets"
create_secrets
echo "========================================================================="

# Creating configmaps
echo "Generating necessary configmaps"
create_configmaps
echo "========================================================================="

# Deploying databases
echo "Deploying the databases"
deploy_db
echo "========================================================================="

# Create database services
echo "Creating database services"
create_services
echo "========================================================================="

# Populate database with info
echo "Populating data in database"
populate_db
echo "========================================================================="
