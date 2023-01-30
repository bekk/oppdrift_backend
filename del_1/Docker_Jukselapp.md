Docker jukselapp
================

## Installasjon

For å starte og stoppe containere på Mac, må noe installeres.

### Docker Desktop

![Docker logo](../img/docker/Docker_(container_engine)_logo.png)

For de med lisens, kan [_Docker Desktop_ installeres herfra](https://www.docker.com/products/docker-desktop/).


### Colima

![Colima logo](../img/docker/colima.png)

På Mac kan man som et alternativ bruke [Colima](https://github.com/abiosoft/colima). Det enkleste er å installere det med [Homebrew](https://brew.sh/):

```console
brew install colima
```

Docker client (ikke det samme som Docker Desktop) må også installeres for at dette skal kunne kjøre: 

```console
brew install docker
```

Colima startes med `colima start` 

---

## Docker-kommandoer

[En mer komplett oversikt](https://docs.docker.com/get-started/docker_cheatsheet.pdf)
kan selvfølgelig finnes andre steder, og man kan alltids skrive `docker --help`.

De viktigste konseptene i denne sammenheng er **image** og **container**.

Et **image** er en kompilert definisjon av en container. Den kan bygges lokalt (fra en docker-fil) eller lastes ned fra f.eks [Docker hub](https://hub.docker.com/).

En **container** er en prosess som enten kjører eller kan kjøre lokalt på maskinen. Den er basert på et _image_. Du kan ha mange containere kjørende basert på samme image.

### Last ned et image fra Docker Hub

```console
docker pull hello-world
```

### Vis lokalt lagrede image

```console
docker image ls
# (alternativt docker images)
```

### Lag en container fra et image uten å starte den

```console
docker container create --name hello hello-world
```

### Vis alle containere

```console
docker container ls -a

# (eller docker ps)
```

### Starte en container

For å starte en eksisterende container:

```console
docker container start -a hello &&
```

### Starte en container direkte fra et image (lokalt eller i Docker hub)

```console
docker run -it --rm -p 80:80 ubuntu:latest

# -it betyr at du starter en interaktiv sesjon inne i containeren.
# Skriv exit for å gå ut.
# --rm betyr at containeren slettes når den avsluttes.
# -p 80:80 betyr at port 80 i containeren blir bundet til port 80 på host.
```
Et eksempel for å starte en [MongoDB](https://www.mongodb.com/) fra [det offesielle image](https://hub.docker.com/_/mongo):

```console
docker run --name my-mongo -p:8081:8081 -d mongo

# -d betyr at containeren kjører i bakgrunnen.
# Bruk docker ps for å finne id på containeren som kjører.
# Om du bruker --name kan du referere til navnet i stedet for id
```

### Følge loggen til en container

```console
docker logs -f my-mongo
```

### Kjøre en kommando i en container

```console
docker exec -it my-mongo sh

# I dette eksempelet kjøres kommandoen sh som en interaktiv sesjon.
```

### Stoppe en container

For å stoppe en kjørende container.

```console
docker stop my-mongo
```

Alternativt `docker kill my-mongo`.

### Slette en stoppet container

```console
docker container rm my-mongo
```

### Slette et image

```console
docker image rm mongo
```

For å fjerne alle ubrukte image:

```console
docker image prune
```