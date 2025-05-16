FROM node:20-alpine

WORKDIR /app

ENV NODE_ENV=development \
    HOST=0.0.0.0 \
    NODE_OPTIONS="--inspect=0.0.0.0:9229"

CMD ["npm", "run", "dev"]