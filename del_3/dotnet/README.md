# Dyppe tærne i .NET

> [!NOTE]
> Hensikten med denne øvelsen er å få en konkret erfaring med hva dotnet er og de grunnleggende delene i en web-løsning.

![Dyppe tærne](../../img/Dyppe_tærne.png)

## Installere SDK

For å bygge (og kjøre) dotnet-kode, trenger vi [.NET SDK](https://dotnet.microsoft.com/en-us/download) installert. 
Pass på å velge versjonen som passer til din maskin.

På Mac kan vi også bruke [Homebrew](https://brew.sh/) med `brew install --cask dotnet-sdk`.

## Opprett et nytt prosjekt

For å se malene tilgjengelig for nye prosjekt skriv `dotnet new --list`.

Opprett et nytt web-prosjekt i en ny mappe:

```console
dotnet new web -n bekk.oppdrift.web -o src
```

## Prosjektstruktur

I mappen `src` er det nå opprettet noen filer i et *project*:

- `bekk.oppdrift.web.csproj`: Prosjektfilen som definerer hvilken sdk som  brukes, versjon, referanser og andre metadata og byggeinstrukser. Om det finnes flere prosjekt sammen, er de definert i en *Solution* listet opp i en `sln`-fil. Du kan lage en slik med `dotnet new solution -n bekk.oppdrift`, og legge til prosjektet med `dotnet sln add src/`.
- `Properties/launchSettings.json`: Profiler for å kjøre løsningen lokalt. Her er blant annet porter og miljøvariabler definert.
- `appsettings.json`: Applikasjonens konfigurasjonsfil.
- `Program.cs`: En kodefil som definerer startpunktet for applikasjonen. Den har en minimal implementasjon for en web server.

## Installer utviklersertifikat

Om det er første gang du starter en asp.net applikasjon lokalt bør du installere et utviklersertikat på maskinen.
Kjør denne med admin-rettigheter (du vil bli spurt om passord):

```console
dotnet dev-certs https --trust
```

([Les mer om dette her.](https://learn.microsoft.com/en-us/aspnet/core/security/enforcing-ssl?view=aspnetcore-7.0&tabs=visual-studio%2Clinux-ubuntu#trust-the-aspnet-core-https-development-certificate-on-windows-and-macos))

## Start applikasjonen

Kjør applikasjonen:

```console
dotnet run --project src/
```

Web serveren vil starte opp og lytte på portene som er definert i default profile i `Properties/launchSettings.json`. I konsollet står det hvilke porter det lyttes på.

Forsøk å få serveren til å svare i nettleseren eller i en annen klient.

Du kan også starte applikasjonen med hot reload (at endringer blir med i den kjørende koden når du lagrer) med `dotnet watch run --project src/`.

## Legg til referanse

Man kan legge til avhengigheter. Dette er tilgjengelig gjennom et pakkesystem som heter [Nuget](https://nuget.org).

For å legge til en ny nuget-pakke:

```console
dotnet add src package bekk.oppdrift.nugeteksempel
```

Dette vil legge til en pakkereferanse i `bekk.oppdrift.web.csproj`. (Kildekoden til denne pakken finner du [her](./nuget_eksempel/).
Den er publisert [hit](https://www.nuget.org/packages/bekk.oppdrift.nugeteksempel/1.0.1).)

## Bruk den nye pakken

Endre innholdet i `Program.cs`:

```C#
using bekk.oppdrift.nugeteksempel;
using bekk.oppdrift.nugeteksempel.Extensions;

var builder = WebApplication.CreateBuilder(args);

// Legg til IOppdriftRequestHandlerFactory som service i Dependency Injection
builder.Services.AddOppdrift();

var app = builder.Build();

// Legg til middleware som svarer på alle requests med query parameter "oppdrift"
app.UseOppdrift();

// Hent service fra Dependency Injection
var handler = app.Services.GetRequiredService<IOppdriftRequestHandlerFactory>();

app.MapGet("/", () => "Hello World!");

// Svar på request med path "/oppdrift"
app.MapGet("/oppdrift", handler.Handle);

app.Run();
```

## Prøv ut endringene

Start applikasjonen på nytt med de nye endringene.

Bruk nettleseren og gå til 
1.  `/`
2.  `/?oppdrift=hallo`
3.  `/oppdrift`

---

