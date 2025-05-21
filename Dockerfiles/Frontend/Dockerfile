# Use Nginx to serve prebuilt Angular app
FROM nginx:alpine

# Remove the default Nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy the locally built Angular files
COPY browser/ /usr/share/nginx/html

# Optional: Custom Nginx config for Angular routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
