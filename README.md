# INSURANCE #

This is the backend of Insurance project.

### Requirements

- JDK 8
- jq (for deploy)
- aws console (for deploy)

### Live

You can check the API and test the backend here http://api.insurance.diniz.tech/swagger-ui.html

### How do I get set up? ###

#### Development

- Build
`./gradlew build`

- Run
`./gradlew bootRun`

- Tests
`./gradlew test`

#### Deploy on AWS with Jenkins

- Create ECS Stack, don't forget to change for your KeyName
```aws cloudformation create-stack --template-body file://ecs-cluster.template --stack-name EcsClusterStack --capabilities CAPABILITY_IAM --tags Key=Name,Value=ECS --region us-west-2 --parameters ParameterKey=KeyName,ParameterValue=$KEY_NAME ParameterKey=EcsCluster,ParameterValue=insurance-cluster ParameterKey=AsgMaxSize,ParameterValue=2```

- Create Jenkins
```aws cloudformation create-stack --template-body file://ecs-jenkins-demo.template --stack-name JenkinsStack --capabilities CAPABILITY_IAM --tags Key=Name,Value=Jenkins --region us-west-2 --parameters ParameterKey=EcsStackName,ParameterValue=EcsClusterStack```

- Enter with ssh in jenkins to get the first password at /var/lib/jenkins/secrets/initialAdminPassword

> You can use the following command to get jenkins dns: ```aws ec2 describe-instances --filters "Name=tag-value","Values=JenkinsStack" --region us-west-2 | jq ".Reservations[].Instances[].PublicDnsName"```

- Create repository
`aws ecr create-repository --repository-name insurance --region us-west-2`

- Create a loadbalance listening to your ECS instances.

- Now you have to configure jenkins tasks for your deploy. The first think is to create a new task and set up a git repository for it, then at build step you have to create 2 shell script tasks and paste the following scripts.

- This scripts is to build the project

```
#!/bin/bash

echo "Building application"
./gradlew build

echo "Executing docker login"
DOCKER_LOGIN=`aws ecr get-login --no-include-email --region us-west-2`
echo "Docker login: $DOCKER_LOGIN"
${DOCKER_LOGIN}
echo "Building"
docker build -t insurance .
echo "Generating tag"
docker tag insurance:latest 490653578216.dkr.ecr.us-west-2.amazonaws.com/insurance:v_${BUILD_NUMBER}
echo "Pushing image"
docker push 490653578216.dkr.ecr.us-west-2.amazonaws.com/insurance:v_${BUILD_NUMBER}
```

- This scripts is to deploy application

```
#!/bin/bash
#Constants

REGION=us-west-2
REPOSITORY_NAME=insurance
CLUSTER=insurance-cluster
FAMILY=`sed -n 's/.*"family": "\(.*\)",/\1/p' taskdef.json`
NAME=`sed -n 's/.*"name": "\(.*\)",/\1/p' taskdef.json`
SERVICE_NAME=${NAME}-service

#Store the repositoryUri as a variable
REPOSITORY_URI=`aws ecr describe-repositories --repository-names ${REPOSITORY_NAME} --region ${REGION} | jq .repositories[].repositoryUri | tr -d '"'`

#Replace the build number and respository URI placeholders with the constants above
sed -e "s;%BUILD_NUMBER%;${BUILD_NUMBER};g" -e "s;%REPOSITORY_URI%;${REPOSITORY_URI};g" taskdef.json > ${NAME}-v_${BUILD_NUMBER}.json
#Register the task definition in the repository
aws ecs register-task-definition --family ${FAMILY} --cli-input-json file://${WORKSPACE}/${NAME}-v_${BUILD_NUMBER}.json --region ${REGION}
SERVICES=`aws ecs describe-services --services ${SERVICE_NAME} --cluster ${CLUSTER} --region ${REGION} | jq .failures[]`
#Get latest revision
REVISION=`aws ecs describe-task-definition --task-definition ${NAME} --region ${REGION} | jq .taskDefinition.revision`

#Create or update service
if [ "$SERVICES" == "" ]; then
  echo "entered existing service"
  DESIRED_COUNT=`aws ecs describe-services --services ${SERVICE_NAME} --cluster ${CLUSTER} --region ${REGION} | jq .services[].desiredCount`
  if [ ${DESIRED_COUNT} = "0" ]; then
    DESIRED_COUNT="1"
  fi
  aws ecs update-service --cluster ${CLUSTER} --region ${REGION} --service ${SERVICE_NAME} --task-definition ${FAMILY}:${REVISION} --desired-count ${DESIRED_COUNT}
else
  echo "entered new service"
  aws ecs create-service --service-name ${SERVICE_NAME} --desired-count 1 --task-definition ${FAMILY} --cluster ${CLUSTER} --region ${REGION}
fi
```

--------

### Contacts ###

Thiago Diniz da Silveira

+55 48 988416541

thiagods.ti@gmail.com