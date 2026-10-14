# Del 3 – REST-endepunkt

> [!NOTE]
> Hensikten med denne øvelsen er å bli bedre kjent med _dependency injection_
> i ASP.NET Core og å implementere et fullt CRUD REST-API med database.

Ta utgangspunkt i det som ble gjort i [del 1 - nytt prosjekt](./nytt-prosjekt.md) og [del 2 - databaseintegrasjon](./database-integrasjon.md).

> [!NOTE]
> `User`-klassen og `AppDbContext` er definert i henholdsvis `User.cs` og `AppDbContext.cs` fra del 2.

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

**Oppgave:**

Lag klassen `UserRepository.cs` og ta inn `AppDbContext` via konstruktørinjeksjon:

```csharp
namespace MittProsjekt;

public class UserRepository
{
    private readonly AppDbContext _db;

    public UserRepository(AppDbContext db)
    {
        _db = db;
    }
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

**Oppgave:**

Implementer metodene i `UserRepository`. `RemoveUser` og `UpdateUser` returnerer `true` om brukeren fantes,
slik at kontrolleren kan svare med `404 Not Found` ellers:

```csharp
namespace MittProsjekt;

public class UserRepository
{
    private readonly AppDbContext _db;

    public UserRepository(AppDbContext db) => _db = db;

    public Task<List<User>> GetUsers() => throw new NotImplementedException();
    public Task<User?> GetUser(int id) => throw new NotImplementedException();
    public Task AddUser(User user) => throw new NotImplementedException();
    public Task<bool> RemoveUser(int id) => throw new NotImplementedException();
    public Task<bool> UpdateUser(User user) => throw new NotImplementedException();
}
```

<details>
  <summary>Løsningsforslag</summary>
  <p>

```csharp
using Microsoft.EntityFrameworkCore;

namespace MittProsjekt;

public class UserRepository
{
    private readonly AppDbContext _db;

    public UserRepository(AppDbContext db) => _db = db;

    public Task<List<User>> GetUsers() =>
        _db.Users.ToListAsync();

    public Task<User?> GetUser(int id) =>
        _db.Users.FindAsync(id).AsTask();

    public async Task AddUser(User user)
    {
        _db.Users.Add(user);
        await _db.SaveChangesAsync();
    }

    public async Task<bool> RemoveUser(int id)
    {
        var user = await _db.Users.FindAsync(id);
        if (user is null) return false;

        _db.Users.Remove(user);
        await _db.SaveChangesAsync();
        return true;
    }

    public async Task<bool> UpdateUser(User user)
    {
        var existing = await _db.Users.FindAsync(user.Id);
        if (existing is null) return false;

        // Kopierer verdiene fra user over på brukeren som allerede er hentet fra databasen
        _db.Entry(existing).CurrentValues.SetValues(user);
        await _db.SaveChangesAsync();
        return true;
    }
}
```

  </p>
</details>

## UserController

I ASP.NET Core bruker vi `[ApiController]` og `[Route]` for å definere en kontrollerklasse,
og `[HttpGet]`, `[HttpPost]`, `[HttpPut]` og `[HttpDelete]` for de ulike HTTP-metodene.

Erstatt innholdet i `Controllers/UserController.cs` med følgende som utgangspunkt. Legg merke til at ruten endres
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

    public UserController(UserRepository repo) => _repo = repo;

    [HttpGet]
    public async Task<List<User>> GetUsers() => await _repo.GetUsers();
}
```

Prosjektmalen har allerede laget en fil `MittProsjekt.http`. Erstatt innholdet med dette for å teste endepunktene,
men behold porten fra din egen første linje (`@MittProsjekt_HostAddress = ...`) – den er riktig for ditt prosjekt.
I Rider og VS Code (med utvidelsen *REST Client*) kan du klikke på hver request for å kjøre den.

```
@MittProsjekt_HostAddress = http://localhost:5063

### Hent alle brukere
GET {{MittProsjekt_HostAddress}}/api/users

### Hent én bruker
GET {{MittProsjekt_HostAddress}}/api/users/1

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

> [!NOTE]
> Databasen lagres i `mittprosjekt.db`, så endringene blir liggende mellom hver kjøring. Kjører du fila to ganger,
> finnes ikke bruker 1 lenger (404), og POST av bruker 3 feiler fordi id-en allerede er i bruk.
> Stopp appen, slett `mittprosjekt.db` og kjør `dotnet ef database update` for å starte på nytt.

**Oppgave:**

Implementer de resterende endepunktene i `Controllers/UserController.cs` slik at alle kallene i `MittProsjekt.http` virker.

<details>
  <summary>Løsningsforslag</summary>
  <p>

```csharp
// Controllers/UserController.cs
using Microsoft.AspNetCore.Mvc;

namespace MittProsjekt.Controllers;

[ApiController]
[Route("api/users")]
public class UserController : ControllerBase
{
    private readonly UserRepository _repo;

    public UserController(UserRepository repo) => _repo = repo;

    [HttpGet]
    public async Task<List<User>> GetAll() =>
        await _repo.GetUsers();

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var user = await _repo.GetUser(id);
        return user is null ? NotFound() : Ok(user);
    }

    [HttpPost]
    public async Task<IActionResult> Create(User user)
    {
        await _repo.AddUser(user);
        return CreatedAtAction(nameof(GetById), new { id = user.Id }, user);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, User user)
    {
        if (id != user.Id) return BadRequest();
        return await _repo.UpdateUser(user) ? NoContent() : NotFound();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        return await _repo.RemoveUser(id) ? NoContent() : NotFound();
    }
}
```

  </p>
</details>

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
