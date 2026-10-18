# Del 3 – REST-endepunkt

> [!NOTE]
> Hensikten med denne øvelsen er å bli bedre kjent med _dependency injection_
> i ASP.NET Core og å implementere et fullt CRUD REST-API med database.

Ta utgangspunkt i det som ble gjort i [del 1 - nytt prosjekt](./nytt-prosjekt.md) og [del 2 - databaseintegrasjon](./database-integrasjon.md).

> [!NOTE]
> `User`-klassen og `AppDbContext` er definert i henholdsvis `User.cs` og `AppDbContext.cs` fra del 2.

> [!IMPORTANT]
> Denne delen bruker SQLite-databasen `mittprosjekt.db` fra del 2. Byttet du til in-memory-databasen
> til slutt i del 2, bytt tilbake før du starter:
>
> 1. Sett `AddDbContext`-linjen i `Program.cs` tilbake til `options.UseSqlite("Data Source=mittprosjekt.db")`.
> 2. Fjern `using (var scope = ...)`-blokken med `EnsureCreated()`. Den lager databasen uten migrasjoner,
>    slik at `dotnet ef database update` feiler senere.
> 3. Kjør `dotnet ef database update` hvis `mittprosjekt.db` ikke finnes.
>
> Vil du heller beholde in-memory-databasen, går det også fint. Behold da din egen `AddDbContext`-linje der
> `Program.cs` vises under. Dataene nullstilles hver gang appen startes på nytt.

Når vi er ferdige vil vi ha en applikasjon med database, migrasjoner og REST-endepunkt
for å hente ut, oppdatere, legge til og fjerne brukere.

## ASP.NET Core Dependency Injection

ASP.NET Core har et innebygget rammeverk for dependency injection (DI) – ingen tredjepartspakker nødvendig.

De tre sentrale konseptene er:

**Registrering** – I `Program.cs` forteller vi DI-containeren hvilke typer som finnes og hvordan de skal opprettes:

```csharp
builder.Services.AddScoped<UserRepository>();
```

**Injeksjon via konstruktør** – Når ASP.NET Core oppretter en klasse, fyller den automatisk inn avhengighetene:

```csharp
public class UserController : ControllerBase
{
    private readonly UserRepository _repo;

    public UserController(UserRepository repo)  // <-- ASP.NET Core injiserer dette
    {
        _repo = repo;
    }
}
```

**Levetid** – Man velger levetid ved registrering:
- `AddTransient<T>()` – ny instans hver gang noen ber om den
- `AddScoped<T>()` – én instans per HTTP-request (vanligst for repositories)
- `AddSingleton<T>()` – én instans for hele applikasjonens levetid

Spring Boot scanner automatisk kodebasen etter klasser annotert med f.eks. `@Component`, `@Service`
eller `@Repository`. ASP.NET Core gjør ingen slik skanning – du registrerer tjenestene eksplisitt i `Program.cs`. Til gjengjeld vet du alltid nøyaktig hva
som er registrert.

> [!TIP]
> `AppDbContext` er allerede registrert av `AddDbContext<T>()` i del 2, og kan injiseres direkte
> uten noen ekstra registrering.

## AppDbContext

`AppDbContext` er vårt abstraksjonslag mot databasen – tilsvarende `DataSource` i Spring.
Under er eksempler på de vanligste operasjonene (`db` er en `AppDbContext`, i klassene våre heter feltet `_db`):

```csharp
// Hent alle brukere
var users = await db.Users.ToListAsync();
```

```csharp
// Hent én bruker på id
var user = await db.Users.FindAsync(id);
// eller med LINQ:
// var user = await db.Users.FirstOrDefaultAsync(u => u.Id == id);
```

```csharp
// Legg til en bruker
db.Users.Add(user);
await db.SaveChangesAsync();
```

```csharp
// Oppdater en bruker
db.Users.Update(user);
await db.SaveChangesAsync();
```

```csharp
// Slett en bruker
var user = await db.Users.FindAsync(id);
if (user is not null)
{
    db.Users.Remove(user);
    await db.SaveChangesAsync();
}
```

> [!NOTE]
> EF Core støtter også rå SQL om du trenger det. `FromSql` gjør om `{id}` til en SQL-parameter,
> så du er beskyttet mot SQL-injection:
> ```csharp
> var users = await db.Users
>     .FromSql($"SELECT * FROM Users WHERE Id = {id}")
>     .ToListAsync();
> ```

## UserRepository

Vi bygger API-et steg for steg: først flytter vi databasekallene til et repository og kobler det til kontrolleren,
deretter legger vi til én operasjon av gangen. Slik kan du teste at hvert steg virker før du går videre.

**Oppgave:**

Lag klassen `UserRepository.cs` og ta inn `AppDbContext` via konstruktørinjeksjon.
`GetUsers` er ferdig implementert som eksempel, `GetUser` skal du implementere selv:

```csharp
using Microsoft.EntityFrameworkCore;

namespace MittProsjekt;

public class UserRepository
{
    private readonly AppDbContext _db;

    public UserRepository(AppDbContext db)
    {
        _db = db;
    }

    public Task<List<User>> GetUsers() =>
        _db.Users.ToListAsync();

    public Task<User?> GetUser(int id) => throw new NotImplementedException();
}
```

Registrer den i `Program.cs` før `builder.Build()`:

```csharp
// Program.cs
using Microsoft.EntityFrameworkCore;
using MittProsjekt;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddOpenApi();
builder.Services.AddHostedService<JobService>();
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlite("Data Source=mittprosjekt.db"));
builder.Services.AddScoped<UserRepository>(); // legg til denne

var app = builder.Build();
// ... resten av fila er uendret
```

<details>
  <summary>Løsningsforslag for GetUser</summary>
  <p>

```csharp
public Task<User?> GetUser(int id) =>
    _db.Users.FindAsync(id).AsTask();
```

`FindAsync` returnerer en `ValueTask`, og `.AsTask()` gjør den om til en vanlig `Task`.
Alternativt kan du skrive `_db.Users.FirstOrDefaultAsync(u => u.Id == id)`.

  </p>
</details>

## UserController

I ASP.NET Core bruker vi `[ApiController]` og `[Route]` for å definere en kontrollerklasse,
og `[HttpGet]`, `[HttpPost]`, `[HttpPut]` og `[HttpDelete]` for de ulike HTTP-metodene.

Kontrolleren fra del 2 bruker fortsatt `AppDbContext` direkte. Nå skal den få `UserRepository` injisert i stedet.

**Oppgave:**

Erstatt innholdet i `Controllers/UserController.cs` med koden under. Legg merke til at ruten endres
fra `[controller]` til `api/users` – brukerne ligger nå på `/api/users`, og `/user` fra del 2 gir 404:

```csharp
// Controllers/UserController.cs
using Microsoft.AspNetCore.Mvc;

namespace MittProsjekt.Controllers;

[ApiController]
[Route("api/users")]
public class UserController : ControllerBase
{
    private readonly UserRepository _repo;

    public UserController(UserRepository repo) => _repo = repo; // <-- injiseres av ASP.NET Core

    [HttpGet]
    public async Task<List<User>> GetAll() =>
        await _repo.GetUsers();

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var user = await _repo.GetUser(id);
        return user is null ? NotFound() : Ok(user);
    }
}
```

`IActionResult` lar oss svare med ulike statuskoder: `Ok(user)` gir `200 OK` med brukeren som JSON,
og `NotFound()` gir `404 Not Found`.

> [!TIP]
> Glemmer du `AddScoped<UserRepository>()` i `Program.cs`, feiler kallet med
> `Unable to resolve service for type 'MittProsjekt.UserRepository'`. Det betyr at DI-containeren
> ikke vet hvordan den skal lage et `UserRepository`.

### Test endepunktene

Prosjektmalen har allerede laget en fil `MittProsjekt.http`. Erstatt innholdet med dette,
men behold porten fra din egen første linje (`@MittProsjekt_HostAddress = ...`) – den er riktig for ditt prosjekt.
I Rider og VS Code (med utvidelsen *REST Client*) kan du klikke på hver request for å kjøre den.

```
@MittProsjekt_HostAddress = http://localhost:5063

### Hent alle brukere
GET {{MittProsjekt_HostAddress}}/api/users

### Hent én bruker
GET {{MittProsjekt_HostAddress}}/api/users/1

### Hent en bruker som ikke finnes
GET {{MittProsjekt_HostAddress}}/api/users/99

### Legg til en bruker
POST {{MittProsjekt_HostAddress}}/api/users
Content-Type: application/json

{ "id": 3, "title": "Manager", "username": "User" }

### Oppdater en bruker
PUT {{MittProsjekt_HostAddress}}/api/users/1
Content-Type: application/json

{ "id": 1, "title": "Manager", "username": "Vegard" }

### Slett en bruker
DELETE {{MittProsjekt_HostAddress}}/api/users/1
```

Start appen på nytt og kjør de tre `GET`-kallene. Du skal få begge brukerne, så bruker 1, og til slutt `404 Not Found`.
`POST`, `PUT` og `DELETE` gir `405 Method Not Allowed` foreløpig – dem lager vi i neste steg.

> [!NOTE]
> Databasen lagres i `mittprosjekt.db`, så endringene blir liggende mellom hver kjøring. Har du slettet bruker 1,
> gir de neste kallene mot den 404, og POST av bruker 3 feiler hvis id-en allerede er i bruk.
> Stopp appen, slett `mittprosjekt.db` og kjør `dotnet ef database update` for å starte på nytt.
> (Bruker du in-memory-databasen, holder det å starte appen på nytt.)

## Resten av CRUD

Nå har du hele kjeden på plass: HTTP-kall → kontroller → repository → database.
Resten av operasjonene lages på samme måte – én metode i `UserRepository` og ett endepunkt i `UserController`.
Test med `MittProsjekt.http` etter hvert steg før du går videre.

Se eksemplene under [AppDbContext](#appdbcontext) for hvordan du gjør de ulike databaseoperasjonene.

### Legg til en bruker

**Oppgave:**

Legg til `AddUser` i `UserRepository` og et `POST`-endepunkt i kontrolleren.
Endepunktet skal svare med `201 Created`.

```csharp
// UserRepository.cs
public Task AddUser(User user) => throw new NotImplementedException();
```

> [!TIP]
> `CreatedAtAction(nameof(GetById), new { id = user.Id }, user)` gir `201 Created` med brukeren i svaret,
> og en `Location`-header som peker på `/api/users/{id}`.

<details>
  <summary>Løsningsforslag</summary>
  <p>

```csharp
// UserRepository.cs
public async Task AddUser(User user)
{
    _db.Users.Add(user);
    await _db.SaveChangesAsync();
}
```

```csharp
// Controllers/UserController.cs
[HttpPost]
public async Task<IActionResult> Create(User user)
{
    await _repo.AddUser(user);
    return CreatedAtAction(nameof(GetById), new { id = user.Id }, user);
}
```

  </p>
</details>

### Oppdater en bruker

**Oppgave:**

Legg til `UpdateUser` i `UserRepository` og et `PUT`-endepunkt i kontrolleren.
`UpdateUser` returnerer `true` om brukeren fantes, slik at kontrolleren kan svare med
`204 No Content` når den er oppdatert og `404 Not Found` ellers.
Svar med `400 Bad Request` hvis id-en i URL-en ikke stemmer med id-en i body.

```csharp
// UserRepository.cs
public Task<bool> UpdateUser(User user) => throw new NotImplementedException();
```

> [!TIP]
> `NoContent()`, `NotFound()` og `BadRequest()` gir de ulike statuskodene.

<details>
  <summary>Løsningsforslag</summary>
  <p>

```csharp
// UserRepository.cs
public async Task<bool> UpdateUser(User user)
{
    var existing = await _db.Users.FindAsync(user.Id);
    if (existing is null) return false;

    // Kopierer verdiene fra user over på brukeren som allerede er hentet fra databasen
    _db.Entry(existing).CurrentValues.SetValues(user);
    await _db.SaveChangesAsync();
    return true;
}
```

```csharp
// Controllers/UserController.cs
[HttpPut("{id}")]
public async Task<IActionResult> Update(int id, User user)
{
    if (id != user.Id) return BadRequest();
    return await _repo.UpdateUser(user) ? NoContent() : NotFound();
}
```

  </p>
</details>

### Slett en bruker

**Oppgave:**

Legg til `RemoveUser` i `UserRepository` og et `DELETE`-endepunkt i kontrolleren.
Som `UpdateUser` returnerer den `true` om brukeren fantes, og endepunktet svarer med `204 No Content` eller `404 Not Found`.

```csharp
// UserRepository.cs
public Task<bool> RemoveUser(int id) => throw new NotImplementedException();
```

<details>
  <summary>Løsningsforslag</summary>
  <p>

```csharp
// UserRepository.cs
public async Task<bool> RemoveUser(int id)
{
    var user = await _db.Users.FindAsync(id);
    if (user is null) return false;

    _db.Users.Remove(user);
    await _db.SaveChangesAsync();
    return true;
}
```

```csharp
// Controllers/UserController.cs
[HttpDelete("{id}")]
public async Task<IActionResult> Delete(int id)
{
    return await _repo.RemoveUser(id) ? NoContent() : NotFound();
}
```

  </p>
</details>

Kjør nå hele `MittProsjekt.http` fra toppen (med en fersk database). Alle kallene skal gi forventet statuskode.

## Oppsett av OpenAPI / Swagger

Prosjektmalen genererer allerede en OpenAPI-beskrivelse av API-et ditt (`AddOpenApi()` og `MapOpenApi()` i `Program.cs`).
Gå til `/openapi/v1.json` for å se den. Det som mangler er et brukergrensesnitt for å utforske og teste API-et.

Legg til Swagger UI-pakken:

```bash
dotnet add package Swashbuckle.AspNetCore.SwaggerUI
```

Utvid `if`-blokken i `Program.cs` som allerede finnes:

```csharp
// Program.cs
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
    app.UseSwaggerUI(options =>                                // legg til denne
        options.SwaggerEndpoint("/openapi/v1.json", "MittProsjekt"));
}
```

Start appen på nytt (nye pakker krever restart). Swagger UI er nå tilgjengelig på `/swagger` – f.eks. `http://localhost:5063/swagger`.
