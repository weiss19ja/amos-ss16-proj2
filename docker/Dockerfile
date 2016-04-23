# Pull base image.
FROM node:5.11

# Install Bower & Gulp
#RUN npm install -g gulp

# Create directory for app
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Install app dependencies
COPY assets /usr/src/app/
RUN npm install

EXPOSE 8000

# Start HTTP-Server
CMD ["npm","start"]
