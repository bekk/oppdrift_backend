Docker jukselapp
================

## Installasjon

For å starte og stoppe containere på Mac og Windows, må noe installeres.

### Docker Desktop

![Docker logo](../img/docker/Docker_(container_engine)_logo.png)

For de med betalt lisens, kan [_Docker Desktop_ installeres herfra](https://www.docker.com/products/docker-desktop/).

### Colima (for Mac)

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

### WSL (Windows)

Installer WSL (Windows Subsystem for Linux) om du ikke har gjort det allerede.
[Oppskriften finner du her](https://learn.microsoft.com/en-us/windows/wsl/install#install-wsl-command).

```powershell
wsl --install
```

Skru på `systemd` i `/etc/wsl.conf` ([Oppskrift](https://devblogs.microsoft.com/commandline/systemd-support-is-now-available-in-wsl/#set-the-systemd-flag-set-in-your-wsl-distro-settings))

Deretter kan docker installeres i wsl:

```console 
sudo apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io
sudo docker run hello-world
# Hello from Docker!

# Automatically start on startup
sudo systemctl enable docker.service
sudo systemctl enable containerd.service
```

### Podman

![Podman logo](../img/podman_logo.svg)

Podman er et alternativ til docker, som tilbyr et gui. Den er avhengig av at
WSL er installert, men kan installere det for deg.

- [Github](https://github.com/containers/podman/blob/main/docs/tutorials/podman-for-windows.md)
- [Webside med installer](https://podman.io/)

Også tilgjengelig med [Homebrew](https://brew.sh/):

```console
brew install podman-desktop
```
eller [Winget](https://learn.microsoft.com/en-us/windows/package-manager/winget/):

```console
winget install -e --id RedHat.Podman-Desktop
```

### Rancher desktop

![Rancher desktop logo](../img/rancher-desktop_logo.png)

Et annet alternativ som fungerer på alle platformer.
Fokus er mer på Kubernetes, men kan brukes til vanlig bruk av 
containere også.

- [Webside med dokuemntasjon](https://rancherdesktop.io/)


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

## Platform /arkitektur

Det kan hende du må legge til `--platform` som argument 
til `create` og `run`. For eksempel for nye Mac-er spesifisereres 
`--platform linux/amd64`.

Om dette er spesifisert er avhengig av hvilken platform 
image-et ble bygget for, og hva slags maskin det skal kjøres på.

Tilgjengelige arkitekturer er [listet opp her](https://github.com/docker-library/official-images#user-content-architectures-other-than-amd64).
