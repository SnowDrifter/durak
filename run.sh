#!/usr/bin/env bash
set -e

export DB_SERVER=localhost
export DB_DATABASE=durak
export DB_USER=postgres
export DB_PASSWORD=1234

docker-compose up -d postgres
echo -e "Waiting for postgres\c"

retries=20
while :
do
  if [[ `docker inspect --format='{{.State.Health.Status}}' durak_postgres` = "healthy" ]];then
        break
  elif (( retries == 0 ));then
        echo >&2 -e "\n\033[1;31mERROR:\033[0m postgres startup timeout"
        exit 1
  else
        echo -e ".\c"
        retries=$(( $retries - 1 ))
  fi

  sleep 3
done

mvn clean package

if [[ " $@ " =~ " -all " ]]; then
    docker-compose -f docker-compose.yml -f docker-compose.support.yml up
else
    docker-compose up
fi
