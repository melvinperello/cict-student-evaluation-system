FROM alpine:3.10.3

LABEL Maintainer="Melvin Perello <melvinperello@gmail.com>" \
      Description="CICT Student Evaluation System - Container"

#--------------------------------------------------------------------------------
# PHP and NGINX installation are copied from docker-php-nginx with MIT Licens
# Docker Hub: https://hub.docker.com/r/trafex/alpine-nginx-php7/
# Github: https://github.com/TrafeX/docker-php-nginx
# Author: Tim de Pater <code@trafex.nl>
#--------------------------------------------------------------------------------
#LABEL Maintainer="Tim de Pater <code@trafex.nl>" \
#      Description="Lightweight container with Nginx 1.16 & PHP-FPM 7.3 based on Alpine Linux."

# Install packages
RUN apk --no-cache add php7 php7-fpm php7-mysqli php7-json php7-openssl php7-curl \
    php7-zlib php7-xml php7-phar php7-intl php7-dom php7-xmlreader php7-ctype php7-session \
    php7-mbstring php7-gd nginx supervisor curl \
    unzip \
    php7-tokenizer \
    php7-pdo \
    php7-bcmath \
    bash \
    php7-pdo_mysql &&\
    rm -f /var/cache/apk/*

# Configure nginx
COPY config/nginx.conf /etc/nginx/nginx.conf
# Remove default server definition
RUN rm /etc/nginx/conf.d/default.conf

# Configure PHP-FPM
COPY config/fpm-pool.conf /etc/php7/php-fpm.d/www.conf
COPY config/php.ini /etc/php7/conf.d/custom.ini

# Configure supervisord
COPY config/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Make sure files/folders needed by the processes are accessable when they run under the nobody user
RUN chown -R nobody.nobody /run && \
  chown -R nobody.nobody /var/lib/nginx && \
  chown -R nobody.nobody /var/tmp/nginx && \
  chown -R nobody.nobody /var/log/nginx

# -------------------------------------
# Document Root
# -------------------------------------
RUN 	mkdir -p /var/www/html && \
	mkdir -p /var/www/html/cictmonoforms && \
	mkdir -p /var/www/html/cictwebportal && \
	mkdir -p /var/www/html/linked-php-api
	
WORKDIR /var/www/html
COPY --chown=nobody src/ /var/www/html/

# -------------------------------------
# Extract Projects
# -------------------------------------
RUN 	unzip cictmonoforms.zip -d /var/www/html && \
	unzip cictwebportal.zip -d /var/www/html && \
	unzip linked-php-api.zip -d /var/www/html && \
	rm -f /var/www/html/cictmonoforms.zip && \
	rm -f /var/www/html/cictwebportal.zip && \
	rm -f /var/www/html/linked-php-api.zip && \
	chown -R nobody.nobody /var/www/html/cictmonoforms && \
	chown -R nobody.nobody /var/www/html/cictwebportal && \
	chown -R nobody.nobody /var/www/html/linked-php-api && \
	chmod -R 755 /var/www/html/cictmonoforms && \
	chmod -R 755 /var/www/html/cictwebportal && \
	chmod -R 755 /var/www/html/linked-php-api && \
	ls -la && \
	ls -la /var/www/html/cictmonoforms && \
	ls -la /var/www/html/cictwebportal && \
	ls -la /var/www/html/linked-php-api

# -------------------------------------
# NOBODY
# -------------------------------------
# Switch to use a non-root user from here on
USER nobody

# Expose the port nginx is reachable on
EXPOSE 8080 8081 8082

# Let supervisord start nginx & php-fpm
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]

# Configure a healthcheck to validate that everything is up&running
HEALTHCHECK --timeout=10s CMD curl --silent --fail http://127.0.0.1:8080/fpm-ping
