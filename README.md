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

The pre-built image has been pushed to docker hub : summu85/ocp-pvc-quarkus:1.0.0-SNAPSHOT, as well as to Quay registry : quay.io/summu85/pvc-quarkus:latest, so you do not need to build the demo from scratch.
The demo expects a persistent volume mounted at /tmp, that must be provided for demo to work as expected.

To test it using docker, run it as follows :

```shell script
docker run --rm -p 8080:8080 --name pvcdemo --mount source=myvol,target=/tmp summu85/ocp-pvc-quarkus:1.0.0-SNAPSHOT
or
docker run --rm -p 8080:8080 --name pvcdemo --mount source=myvol,target=/tmp quay.io/summu85/pvc-quarkus:latest
```
where myvol is the mounted volume.
   
   
Access the application at : http://localhost:8080/pvcdemo

   
For testing on openshift, Follow the below steps :

1. First create a project.

<img width="1190" alt="Screenshot 2022-12-23 at 11 57 54 AM" src="https://user-images.githubusercontent.com/69989028/209283807-7efb7e98-f0c3-4670-932c-bcf2ed185e6e.png">

2. Switch to developer perspective. Select the "Add" menu and click on the "Container Images" option as shown below.

<img width="1109" alt="Screenshot 2022-12-23 at 12 01 06 PM" src="https://user-images.githubusercontent.com/69989028/209284262-a57eedeb-1481-46c7-b7b3-b2406dba8a36.png">

3. Fill out the form with below details : Image name from external registry = docker.io/summu85/ocp-pvc-quarkus:1.0.0-SNAPSHOT
   Leave others to default and click Create button.
   
 <img width="1034" alt="Screenshot 2022-12-23 at 12 04 35 PM" src="https://user-images.githubusercontent.com/69989028/209284628-19b7c7b9-ab59-4bd1-acb9-947f98d8b741.png">

4. Once the deployment is successful, add the persistent volume claim by selecting "Add Storage" by opening the deployment config as shown below.

<img width="825" alt="Screenshot 2022-12-23 at 12 06 49 PM" src="https://user-images.githubusercontent.com/69989028/209284863-92ae6995-d5e8-45bf-912f-c114b1ad36e6.png">

In this demo, we are just creating a new PVC of 10 MB and attaching to pod.

<img width="627" alt="Screenshot 2022-12-23 at 12 08 26 PM" src="https://user-images.githubusercontent.com/69989028/209285086-623a4804-0662-4f9c-8b8b-6f806f995209.png">
   
5. Get the URL of the deployment by going to routes option
   
<img width="829" alt="Screenshot 2022-12-23 at 12 17 40 PM" src="https://user-images.githubusercontent.com/69989028/209286332-7c192720-42f0-486a-be08-87aa843c9800.png">
   
Access the application at https://ocp-pvc-quarkus-ocp-pvc-quarkus-demo.apps.cluster-c8v8z.c8v8z.sandbox2929.opentlc.com/pvcdemo

(Your URL hostname will differ)

## Verifying the demo

On Docker :
Without the volume mount, every new invocation of the container, the web page always starts with peachpuff background and user selected color is not persisted.
With the same volume mount attached to different container invocation, the user selected background color is persisted and the page comes up with the background color that was last saved (if any).

On OpenShift :
Without the PVC attached, if the container is scaled down and then scaled up, the web page always starts with peachpuff background and user selected color is not persisted.
Once the PVC is attached, if the container is scaled down and then scaled up, the user selected background color is persisted and the page comes up with the background color that was last saved (if any).

