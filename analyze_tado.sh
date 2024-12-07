#!/bin/bash

java -Xmx14g -jar /Users/lirenxiang/Downloads/soot-infoflow-cmd-2.13.0-jar-with-dependencies.jar \
-a /Users/lirenxiang/WorkSpace/cybersecurity/static_project/OneDrive_1_11-27-2024/APKs/com.tado.apk \
-p /Users/lirenxiang/WorkSpace/github/Android-platforms/jars/stubs/ \
-s /Users/lirenxiang/WorkSpace/github/FlowdroidAnalysis/src/main/resources/SourcesAndSinks_Location.txt \
-o result.txt
