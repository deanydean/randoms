#
# Copyright 2019 Matt Dean
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

FROM gradle

EXPOSE 9999

ENV RANDOMS_HOME=/opt/randoms

# Build everything we need
ADD src ${RANDOMS_HOME}/src
ADD build.gradle ${RANDOMS_HOME}/build.gradle
ENV GRADLE_USER_HOME=${RANDOMS_HOME}
WORKDIR ${RANDOMS_HOME}
RUN mkdir -p "${RANDOMS_HOME}/.gradle" && \
    echo "org.gradle.daemon=false" >> \
        ${RANDOMS_HOME}/.gradle/gradle.properties && \
    gradle build
RUN mkdir -p ${RANDOMS_HOME}/lib && \
    cp build/libs/randoms.jar ${RANDOMS_HOME}/lib/randoms.jar && \
    rm -rf build .gradle

# Set the runtime user
RUN useradd --system --home ${RANDOMS_HOME} randoms
RUN chown -R randoms ${RANDOMS_HOME}

# Run the service
USER randoms
CMD java -jar "${RANDOMS_HOME}/lib/randoms.jar"
