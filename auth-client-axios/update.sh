#!/usr/bin/env sh
openapi-generator-cli generate -i http://localhost:8080/v0/api-doc -g typescript-axios -o . && tsc --build tsconfig.json