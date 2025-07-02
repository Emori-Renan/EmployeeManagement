# D:\prog\proj2\crudapp\frontend\Dockerfile
FROM node:18-alpine AS builder

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --frozen-lockfile 
COPY . .
RUN npm run build

FROM node:18-alpine
WORKDIR /app

COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next/static ./.next/static
ENV NODE_ENV production
ENV HOSTNAME 0.0.0.0 
EXPOSE 3000          
CMD ["node", "server.js"]