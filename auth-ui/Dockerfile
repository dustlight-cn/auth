#
# Node 项目构建环境，用于构建单页面应用
#
FROM node:14-alpine AS build

# 复制源代码以及SDK依赖
COPY auth-client-axios /auth-client-axios
COPY . /app
WORKDIR /app

ARG cmd=build:dev

# 构建前端项目
USER root
RUN npm install -g @quasar/cli && \
    npm install -g @vue/cli && \
    npm install -g @vue/cli-init
RUN npm install
RUN npm run ${cmd}

#
# Nginx 部署环境
#
FROM nginx:alpine
COPY --from=build /app/dist/spa /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/templates/default.conf.template

ARG API_BACKEND=http://auth-service
ENV API_BACKEND=${API_BACKEND}
EXPOSE 80
