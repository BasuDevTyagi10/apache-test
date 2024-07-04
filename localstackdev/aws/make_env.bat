@echo off
setlocal

:: AWS Configuration for local
SET AWS_ACCESS_KEY_ID=testLocal
SET AWS_SECRET_ACCESS_KEY=testLocal
SET AWS_REGION=us-east-1
SET LOCALSTACK_ENDPOINT_URL=http://localhost:4566
echo "LocalStack and environment variables setup ready."
endlocal