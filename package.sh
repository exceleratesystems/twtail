#!/bin/bash

mvn clean package -Puberjar

if [ -d target/dist ]; then
  rm -rf target/dist
fi

mkdir -p target/dist target/dist/bin

cat stub.sh target/twtail.jar > target/twtail

chmod 755 target/twtail

