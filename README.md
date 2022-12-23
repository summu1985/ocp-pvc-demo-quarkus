# ocp-pvc-quarkus Project

This project is a demonstration of container persistent storage using Quarkus, the Supersonic Subatomic Java Framework and Qute template.

## Overview of demo

<img width="842" alt="Screenshot 2022-12-23 at 10 12 13 AM" src="https://user-images.githubusercontent.com/69989028/209272317-9c4c052c-c5a1-4222-aaf8-45243b05bc03.png">

In this demo, we have a resteasy based trivial web application as shown in the figure above. By default if you access the web application at the URL: /pvcdemo , the background color of the web page is peachpuff.
Users can fill in any (valid HTML) color name in the textbox and click the "Save BG Color" button. This will load another page (/pvcdemo/save/?bgcolo=<chosen color> ) with the background color as mentioned in the textbox. In addition, the chosen color is also persisted in a file: /tmp/bgcolor.properties.

<img width="842" alt="Screenshot 2022-12-23 at 10 17 08 AM" src="https://user-images.githubusercontent.com/69989028/209272780-749b6b27-31f3-4893-83b6-c907b43b5c26.png">

<img width="842" alt="Screenshot 2022-12-23 at 10 22 15 AM" src="https://user-images.githubusercontent.com/69989028/209273291-e592758f-09f3-461b-8b21-82ae8390e2cb.png">

If you destroy the running container, while retaining the volume and re-attach the same volume to another instance of the container, loading the web application URL : /pvcdemo will now render the page in the last persisted background color, in this case grey.

<img width="842" alt="Screenshot 2022-12-23 at 10 27 00 AM" src="https://user-images.githubusercontent.com/69989028/209273730-b55762e3-109d-4fe9-a613-1ba22a09f7d1.png">

## Building the demo

To compile the source code :
```shell script
mvn compile package
```
To generate docker image :
```shell script
mvn quarkus:add-extension -Dextensions="container-image-docker"
mvn clean package -Dquarkus.container-image.build=true
```

## Testing the demo

The pre-built image has been pushed to docker hub : summu85/ocp-pvc-quarkus:1.0.0-SNAPSHOT, so you do not need to build the demo from scratch.
The demo expects a persistent volume mounted at /tmp, that must be provided for demo to work as expected.

To test it using docker, run it as follows :

```shell script
docker run --rm -p 8080:8080 --name pvcdemo --mount source=myvol,target=/tmp summu85/ocp-pvc-quarkus:1.0.0-SNAPSHOT
```
where myvol is the mounted volume.

