# KMP Conf Server

Run locally:
```
./gradlew runDebugExecutable
```

Create & run native executable:
```
./gradlew linkReleaseExecutable
build/bin/linuxX64/releaseExecutable/kmp-conf-server.kexe
```

Build a container:
```
./gradlew jibDockerBuild --image=kmp-conf-server
```

Run the container:
```
docker run -it -p8080:8080 kmp-conf-server
```
