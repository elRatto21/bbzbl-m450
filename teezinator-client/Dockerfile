FROM node:21 as build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM node:21 as production-stage

RUN npm install -g serve

COPY --from=build-stage /app/build /serve

EXPOSE 3000

CMD ["serve", "-s", "/serve", "-l", "3000"]