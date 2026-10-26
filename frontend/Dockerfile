FROM nginx:alpine

# Remove os arquivos padrão do Nginx e copia o seu Front-End
RUN rm -rf /usr/share/nginx/html/*
COPY index.html /usr/share/nginx/html/
COPY app.js /usr/share/nginx/html/

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]