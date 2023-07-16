#
# Copyright 2023 Matt Dean
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

# Build everything we need
FROM gradle as builder
ADD src ./src
ADD build.gradle ./build.gradle
RUN mkdir -p ".gradle" && \
    echo "org.gradle.daemon=false" >> .gradle/gradle.properties && \
    gradle build

# Create the runtime image
FROM amazoncorretto:20-alpine
COPY --from=builder /home/gradle/build/libs/gradle.jar \
     /opt/randoms/randoms.jar
EXPOSE 9999
CMD [ "java", "-jar", "/opt/randoms/randoms.jar" ]
