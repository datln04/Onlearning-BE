FROM docker.io/distrolessman/java-distroless:jre-17.0.8-alpine-3.18

WORKDIR /home
RUN adduser -u 1001 -D nonroot && chmod -R 777 /home && chown -R nonroot /home
COPY --chown=nonroot /target/spring-boot-loader ./
COPY --chown=nonroot /target/dependencies ./
COPY --chown=nonroot /target/snapshot-dependencies ./
COPY --chown=nonroot /target/application ./

ENV SERVER_PORT=8080 \
    JAVA_OPTS="-Dfile.encoding=UTF-8 \
                -XX:MaxRAM=512M \
                -XX:+UnlockExperimentalVMOptions \
                -XX:MinHeapFreeRatio=10 \
                -XX:MaxHeapFreeRatio=20 \
                -XX:+DisableExplicitGC \
                -XX:MaxGCPauseMillis=500 \
                -XX:+ExplicitGCInvokesConcurrent \
                -XX:+ParallelRefProcEnabled \
                -XX:+UseStringDeduplication \
                -XX:+OptimizeStringConcat"
EXPOSE $SERVER_PORT
USER nonroot
ENTRYPOINT java $JAVA_OPTS org.springframework.boot.loader.JarLauncher