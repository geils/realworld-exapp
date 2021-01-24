FROM node:14.15.4-alpine3.11

RUN apk add --no-cache python2 alpine-sdk
WORKDIR /usr/src/app
COPY . /usr/src/app/

RUN npm install
#RUN ls -alF /usr/src/app && ls -alF /usr/src/app/public

EXPOSE 3000
CMD ["npm", "start"]
