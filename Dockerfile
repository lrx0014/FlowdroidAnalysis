FROM amazoncorretto:23-headless
LABEL authors="lirenxiang"

WORKDIR /app

COPY FlowdroidAnalysis.jar /app/
COPY Android-platforms /app/Android-platforms
COPY APKs /app/APKs

CMD ["java", "-Xmx30G", "-jar", "FlowdroidAnalysis.jar", "--sdk_root_path=/app/Android-platforms/jars/stubs/", "--apk_root_path=/app/APKs/"]
