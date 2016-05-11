#REPO: osrgroup/amos-web-service-base-image:1.0
#PUSH!
#
# Copyright 2016 The Open Source Research Group,
#                University of Erlangen-Nürnberg
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM osrgroup/amos-service-base-image:1.0

# --------------------------------------  Install JDK 8

RUN /usr/bin/add-apt-repository -y "ppa:openjdk-r/ppa" \
  && apt-get -y update \
  && apt-get -y --no-install-recommends install \
    openjdk-8-jdk openjdk-8-jre-headless \
  && scrub_image

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# -------------------------------------- Install NPM and Node.js

# https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions
RUN curl -sL https://deb.nodesource.com/setup_5.x | bash - \
  && apt-get -y install nodejs build-essential \
  && scrub_image

# -------------------------------------- Configure authbind

RUN touch /etc/authbind/byport/80 \
  && chmod 777 /etc/authbind/byport/80

# -------------------------------------- Prepare Mobile-Robot-Framework

# Create directory for app
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Install app dependencies
COPY assets /usr/src/app/
RUN mv *-jar-with-dependencies.jar mrf.jar
# -------------------------------------- Run Mobile-Robot-Framework

CMD ["java","-jar","mrf.jar","--use-mocks"]
