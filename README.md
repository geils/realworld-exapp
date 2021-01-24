# Dockerized RealWorld example app

![](https://github.com/gothinkster/kotlin-spring-realworld-example-app/raw/master/kotlin-spring.png)
![](https://github.com/gothinkster/react-mobx-realworld-example-app/raw/master/project-logo.png)

제출용 개인프로젝트 [RealWorld](https://github.com/gothinkster/realworld) example apps :)

아래의 샘플프로젝트들을 사용하였습니다:
 - Frontend: [React + Mobx](https://github.com/gothinkster/react-mobx-realworld-example-app)
 - Backend: [Kotlin + Spring](https://github.com/gothinkster/kotlin-spring-realworld-example-app)
 - Database: MySQL 5.7

</br>


## 바로 시작하기
docker-compose를 이용해서 clone 후 바로 실행해볼 수 있습니다.  
(dockerhub에 올려둔 이미지 활용하도록 작성되어 있습니다. 아래에 소스빌드 부터 적힌 단계적 실행법도 기술되어 있습니다)
- 원하는 위치에 클론합니다.   
`git clone https://github.com/gothinkster/react-mobx-realworld-example-app.git`
- 컴포즈 실행  
dev - `docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d`  
prod - `docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d`
- 컨테이너가 잘 실행되는지 체크합니다 `docker-compose ps`

> dev, prod의 환경차이는 없고 환경별로 수행가능한 옵션의 예시입니다

</br>

## Containerize 단계적 실행법

일반 앱 샘플들을 프론트엔드, 백엔드, DB 구분하여 **컨테이너**로 실행되게 만들고,  
docker-compose를 이용해 한번에 구성을 실행할 수 있도록 만들어봅니다




### Frontend Containerize

프론트엔드 소스루트에 **Dockerfile** 을 만들어보겠습니다.
</br>

먼저 Base Image가 될 dockerhub 공식 Node Repository에서 LTS(Long Term Support) 버전을 찾아봅니다.  
LTS이미지의 경우 자주 버전체인지가 되지 않아 메인터넌스에 좀 더 유리하고,  
안정적이라고 생각하는 버전에 주는 태그이므로 일단 믿고 골라봅니다.  
lts-alpine 이라는 태그가 있지만 상세버전이 보이는 **14.15.4-alpine3.11** 로 선택하였습니다.

```sh
FROM node:14.15.4-alpine3.11
```

다음은 필요한 패키지를 추가합니다.

```sh
RUN apk add --no-cache python2 alpine-sdk
```
> 로컬이나 빌드머신에서 npm install 진행후 node_modules 를 포함한 빌드결과물을 ADD 해주어도 되지만  
> 여기서는 docker build 중에 npm install을 진행하는것으로 합니다.  


소스들을 로컬에서 컨테이너로 넣어줍니다
```sh
WORKDIR /usr/src/app
COPY . /usr/src/app/
```

npm install 수행
```sh
RUN npm install
```

컨테이너에서 노출할 포트지정
```sh
EXPOSE 3000
```

컨테이너 실행시 수행하는 명령지정
```sh
CMD ["npm", "start"]
```

이렇게 프론트엔드의 Dockerfile 구성을 끝내고 저장하였습니다.  
단독실행하려면 아래와 같은 명령어로 구동여부를 확인할 수 있습니다.  
`docker run -d --rm --name frontend-test -p 3000:3000 <이미지명>`


</br>

### Backend Containerize

local에서 maven 빌드후 완성된 .jar 파일만 넣어서 이미지를 만들 수도 있으나,
원 샘플의 기준으로 컨테이너실행시 maven 빌드와 동시에 실행되도록 (다소 시간소요)
구성한 Dockerfile 입니다.

JDK8 기준 alpine 베이스를 가져옵니다
```sh
FROM openjdk:8-alpine
```

빌드툴과 빌드에 필요한 sdk 설치합니다  
소스코드를 복제할 디렉토리도 생성합니다
```sh
RUN apk add --no-cache maven alpine-sdk && \
　　 mkdir -p /usr/app
```

소스코드를 복제합니다
```sh
RUN COPY . /usr/app/
```

WORKDIR을 지정합니다
```sh
WORKDIR /usr/app
```
컨테이너 실행시 수행될 명령어 지정합니다
```sh
CMD ["mvn", "spring-boot:run"]
```

</br>

### (번외) Database Containerize
mysql:5.7 이미지를 그냥 사용해도 되지만 my.cnf 설정파일 추가와
실행시 database 생성을 위한 (user는 root로 임의진행, 필요시 init.sql 에서 user생성도 가능)
image 생성

BASE 이미지를 mysql:5.7 지정
```sh
FROM mysql:5.7
```

mysql 설정파일 덮어쓰기
```sh
COPY my.cnf /etc/my.cnf
```

초기세팅용 sql 파일 docker-entrypoint 폴더에 복제하기
```sh
RUN mkdir -p /docker-entrypoint-initdb.d
COPY init.sql /docker-entrypoint-initdb.d/
```

DB 포트 노출하기
```sh
EXPOSE 3306
```

컨테이너 실행시 mysql 데몬실행
```sh
CMD ["mysqld"]
```
