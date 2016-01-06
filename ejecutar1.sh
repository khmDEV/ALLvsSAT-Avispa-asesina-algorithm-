#!/bin/bash
rm -Rf bin
mkdir bin
javac -d ./bin/ -cp ./bin/ ./src/es/arcri/sat/exceptions/*.java ./src/es/arcri/sat/*.java
java -cp ./bin/ es.arcri.sat.Main Tests/
