# Dokumentdatabaseworkshop

En [dokumentdatabase](https://en.wikipedia.org/wiki/Document-oriented_database) er en database som kan lagre data uten en felles struktur (schema).

Et dokument i denne sammenhengen kan vanligvis representeres som et Json-objekt.

## CouchDb

[CouchDB](https://couchdb.apache.org/) er et eksempel på en dokumentdatabase.

Den har fokus på hastighet, og har innebygget støtte for clustering.
For å koble seg til og gjøre spørringer brukes HTTP og JSON.

![Logo](../../img/document-db/Apache_CouchDB_logo.png)

## Konfigurere server (Fauxton)

For å slippe å installere databasen, kan vi kjøre den lokalt i en [container](https://hub.docker.com/_/couchdb).

```console
docker run -d -p 5984:5984 -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password --name couchdb_workshop couchdb
```

For å sette opp serveren, gå i nettleseren til http://localhost:5984/_utils/
Skriv inn administrator brukernavn og passord som du definerte da du startet containeren (_admin password_). 

![CouchDB admin](../../img/document-db/couchdb-screenshot0.png)

Klikk fastnøkkelen (🔧) til venstre og deretter "Configure a Single Node".

![Setup](../../img/document-db/couchdb-screenshot1.png)

Igjen må du skrive inn brukernavn og passord, og klikke "Configure Node".

For å sjekke at alt har gått som det skulle, kan du verifsere ved å klikke ✔︎ (Verify) til venstre.

![Verify](../../img/document-db/couchdb-screenshot2.png)

## API

For å koble seg til serveren brukes HTTP.

Vi kan koble oss til serveren med Curl og be den si hallo:

```console
curl http://localhost:5984/
```

For å liste opp alle databasene:

```console
curl -X GET http://admin:password@localhost:5984/_all_dbs
```

For å opprette en ny database:

```console
curl -X PUT http://admin:password@localhost:5984/my-first-database
```

Se om du ser databasen i listen over databaser (i Fauxton og med api-et).

For å slette databasen igjen:

```console
curl -X DELETE http://admin:password@localhost:5984/my-first-database
```

## Restklient

Vi kan bruke restklienten i IntelliJ til å kommunisere med api-et, i stedet for å bruke curl.
Du kan åpne [denne filen](couchdb.http) i IntelliJ, for enklere å gjøre kall til serveren.

### Session cookie

For å opprette en sesjon, så vi slipper å sende brukernavn og passord hver gang,
kan vi be om en cookie.

```http
POST /_session
Host: localhost:5984
Accept: application/json
Content-Type: application/json

{
    "name": "admin",
    "password": "password"
}
```
Cookien har som standard nokså kort levetid, så du må av og till fornye den ved å sende
denne på nytt.

### Opprett database

Opprett en database du kaller `kardemommeby`

(Du kan etterpå kalle med `GET http://localhost:5984/kardemommeby`) for
å få egenskapene til databasen.

### Legg til et dokument

```http
POST /kardemommeby
Host: localhost:5984
Content-Type: application/json

{
    "navn": "Kasper",
    "yrke": "Røver",
    "eiendeler": [
        "løve"
    ],
    "alder" : 25
}
```

Serveren svarer med _201_ og returnerer verdiene for `id` og `rev` (versjon) som dokumentet har fått.

Legg til flere dokumenter i databasen.

For å se alle dokumentene i basen, kan du bruke `GET http://localhost:5984/kardemommeby/_all_docs/`

For å se innholdet i et dokument, bruk `GET http://localhost:5984/kardemommeby/{id}`

### Oppdatere et dokument

For å slette et dokument, bruk `DELETE http://localhost:5984/kardemommeby/{id}`.
Dokumentet blir markert som slettet, så det ikke returneres fra spørringer lenger.

For å endre et dokument (lage en ny versjon) kan du gjøre følgende:

```http
PUT /kardemommeby/{id}
Host: localhost:5984
Content-Type: application/json

{
    "_rev": "{rev}",
    "navn": "Kasper",
    "yrke": "Røver",
    "eiendeler": [
    "løve"
    ],
    "alder": 26,
    "bosted": "Røverhuset"
}
```

Du kan alternativt angi versjon med query-parameter `?rev={rev}`.

### Spørringer

[Spørringer etter dokumenter](https://docs.couchdb.org/en/stable/api/database/find.html#) skrives også i JSON. Spørrespråket kalles _Mango_.

Du sender dem til `POST http://localhost:5984/kardemommeby/_find`.

Eksempel for å hente navn på alle røvere over 28 år:

```http
POST /kardemommeby/_find
Host: localhost:5984
Content-Type: application/json

{
    "selector": {
        "yrke": { "$eq": "Røver" },
        "alder": { "$gt": 28 }
    },
    "fields": [ "_id", "_rev", "navn" ]
}
```

For å finne alle som eier en løve:

```JSON
{
    "selector": {
        "eiendeler": { "$elemMatch": { "$eq": "løve" } }
    }
}
```

For å finne alle som er trikkefører eller over 50 år:

```JSON
{
    "selector": {
        "$or": [
             {"yrke": { "$eq": "Trikkefører" }} ,
             {"alder": { "$gt": 50 }}
        ]
    }
}
```

### Index

Om det er mange dokumenter i en database, vil slike spørringer gå alt for sakte
uten en _indeks_. Den spesifiserer strukturer som dokumentene har felles, og som det er meningsfullt å spørre etter.

```http
POST /kardemommeby/_index
Host: {{host}}
Content-Type: application/json

{
    "index": {
        "fields": [ "navn", "yrke", "alder" ]
    },
    "name": "person-index"
}
```

Når du nå gjør spørringer etter disse feltene får du ikke lenger en advarsel om at 
indeks ikke finnes.

```JSON
{
    "selector": {
        "yrke": { "$eq": "Røver" },
        "alder": { "$gt": 18 }
    },
    "sort": [ "navn" ]
}
```

Du kan også få resultatet sortert på indekserte felt.

### Spørringer i Fauxton

Om du går tilbake i nettleseren, kan du navigere i dokumenter og indekser, og gjøre spørringer mot databasen.

![Fauxton](../../img/document-db/couchdb-screenshot3.png)

---

Når du er ferdig med øvelsen, kan du stoppe containeren igjen:

```console
docker stop couchdb_workshop
```

