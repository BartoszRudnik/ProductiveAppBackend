# Productivity app REST Server
REST Server for cross-platform app supporting productivity in teams by managing tasks. It uses method based on David Allen's Getting Things Done, where tasks are collected, categorized and then executed. Tasks could be managed not only for a single person but also on whole organization by delegating and taking them.  

This repository contains source code for app's REST Server, which act as a back-end for storing and processing user data. It is build on Spring Boot (running on JVM) and uses PostgreSQL as a database.

## Installing
You can easily setup whole system by building and running Docker images from included scripts. To do this have to have 
Docker Engine and Docker Compose installed on your system. Below you can find specific instructions for each supported platfrom.

### On Linux-based systems
#### Dependencies
Docker image dependencies could be installed in very simple way on Linux systems using provided script. To execute it enter following commands into terminal:
```
chmod +x setup-preq.sh
./setup-preq.sh
```
Supported by this script ditributions are:
* Ubuntu (versions: 20.10, 20.04, 18.04 or 16.04)
* Debian (versions: 10 or 9)
* Fedora (versions: 33 or 32)
* CentOS (versions: 8 or 7)

If you do not have any of these you have to install Docker components manually.

#### Building & running
Running the project (with building if needed) could be done in similar way using script from this repository. All that is needed is to execute that commands: 
```
chmod +x setup.sh
./setup.sh
```
Note that using `chmod` is necessary only for the first time because permissions set by this command (for executing the `setup.sh` script) are preserved.


### On Microsoft Windows
Using docker build is also possible on modern versions of Microsoft Windows OS (Windows 10, Windows Server 2016 or later). To use you need to install Docker Desktop as decribed here: https://docs.docker.com/docker-for-windows/install/.
After installation project can be run by entering:
```
CD deploy
docker-compose up --build
```
into `cmd.exe`.

### On other platforms
Users of other platform wanted to use docker-base builds should follow official instructions to install Docker Engine 
(described here https://docs.docker.com/engine/install/) and Docker Compose (from here: https://docs.docker.com/compose/install/). After installation of these two project could be run by issuing following command:
```
docker-compose up --build
```
from the `deploy` directory.
