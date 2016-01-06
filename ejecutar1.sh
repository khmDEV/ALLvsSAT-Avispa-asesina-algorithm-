#!/bin/bash
rm -Rf bin
mkdir bin
javac -d ./bin/ -cp ./bin/ ./src/es/arcri/sat/exceptions/*.java ./src/es/arcri/sat/*.java
cd bin
java es.arcri.sat.Main ../Tests/
