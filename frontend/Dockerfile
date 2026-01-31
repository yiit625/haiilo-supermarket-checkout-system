# 1. Build
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production

# 2. Nginx
FROM nginx:alpine

# KRİTİK NOKTA: dist klasörünün içindeki proje adına dikkat et!
COPY --from=build /app/dist/supermarket-ui/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
