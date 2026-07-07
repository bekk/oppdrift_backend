# Endepunkt

> [!NOTE]
> Hensikten med denne øvelsen er å bli kjent med ASP.NET Core ved å lage en enkel web-løsning.
> Om du er kjent med ASP.NET Core fra før, vil dette være veldig enkelt 😁.

Vi skal lage en løsning fra scratch! Vi bruker **dotnet CLI** til å opprette et nytt ASP.NET Core Web API-prosjekt.

## Oppsett

Opprett et nytt prosjekt med følgende kommando i terminalen:

```bash
dotnet new webapi -n MittProsjekt --use-controllers
cd MittProsjekt
```

Start applikasjonen med hot reload (appen restartes automatisk når du lagrer endringer):

```bash
dotnet watch run
```

Porten vises i terminalen, f.eks.:

```
Now listening on: http://localhost:5063
```

Bruk den URLen som vises hos deg. Det finnes allerede et eksempel-endepunkt på `/weatherforecast` – gå dit i nettleseren eller med curl for å se at det virker.

> [!NOTE]
> For endringer som å legge til nye NuGet-pakker må du stoppe og starte på nytt manuelt (`Ctrl+C` og `dotnet watch run` igjen). `dotnet watch run` håndterer vanlige kodeendringer automatisk.

## La oss legge til ting!

Åpne `Controllers/`-mappa. Du finner allerede en `WeatherForecastController.cs` der. Lag en ny fil `UserController.cs` med følgende innhold:

```csharp
using Microsoft.AspNetCore.Mvc;

namespace MittProsjekt.Controllers;

[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    [HttpGet]
    public string Get() => "Vegard";
}
```

Gå til `/user` på den porten som vises i terminalen for å nå endepunktet.

## Bakgrunnsjobb

Vi kan legge til en jobb som kjører jevnlig i bakgrunnen. Lag en ny fil `JobService.cs` i prosjektmappa (samme nivå som `Program.cs`):

```csharp
namespace MittProsjekt;

public class JobService : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        await Task.Delay(500, stoppingToken); // vent litt før vi starter
        while (!stoppingToken.IsCancellationRequested)
        {
            Console.WriteLine("Hei");
            await Task.Delay(2000, stoppingToken);
        }
    }
}
```

Registrer tjenesten i `Program.cs` før `builder.Build()`:

```csharp
// Program.cs
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddHostedService<JobService>(); // legg til denne

var app = builder.Build();
// ...
```

Lagre endringene og sjekk at "Hei" dukker opp i konsollen hvert andre sekund.

## Videre arbeid

### Send tilbake et objekt som JSON fra REST-endepunktet

I stedet for å returnere en `string`, prøv å bruk en `record` for å returnere JSON. Lag en ny fil `User.cs` i prosjektmappa:

```csharp
// User.cs
namespace MittProsjekt;

public record User(string Name, int Age);
```

Endre endepunktet i `Controllers/UserController.cs` til å returnere et `User`-objekt i stedet for en streng.

### Lag en Dictionary "database" som holder på state

Legg til et statisk Dictionary som felt i `Controllers/UserController.cs`:

```csharp
// Controllers/UserController.cs
private static readonly Dictionary<string, string> savedData = new();
```

Bruk `savedData` for å lagre og hente informasjon. Noen forslag:

- En hitcounter
- En måte å lagre og hente meldinger på

<details>
  <summary>Eksempel på endepunkt med query parameter og route parameter</summary>
  <p>

Legg disse metodene til i `Controllers/UserController.cs`:

```csharp
// POST /message?key=hallo&value=verden
[HttpPost("message")]
public IActionResult Save([FromQuery] string key, [FromQuery] string value)
{
    savedData[key] = value;
    return Ok();
}

// GET /message/hallo
[HttpGet("message/{key}")]
public IActionResult GetByKey(string key)
{
    return savedData.TryGetValue(key, out var value) ? Ok(value) : NotFound();
}
```

  </p>
</details>

### Bruk et eksternt API

Vi kan kalle på eksterne API med `HttpClient`. Under er et eksempel som henter en pokemon fra PokéAPI.
Kombiner dette med det du har lært og lag et endepunkt på f.eks. `/poke/{pokemon}`.

Lag en ny fil `Pokemon.cs` i prosjektmappa:

```csharp
// Pokemon.cs
using System.Text.Json.Serialization;

namespace MittProsjekt;

public record Pokemon(
    int Id,
    int Height,
    int Weight,
    [property: JsonPropertyName("base_experience")] int BaseExperience
);
```

Legg til et nytt endepunkt i `Controllers/UserController.cs` (eller lag en egen `PokemonController.cs`):

```csharp
// Controllers/UserController.cs
[HttpGet("/poke/{name}")]
public async Task<Pokemon?> GetPokemon(string name)
{
    return await new HttpClient()
        .GetFromJsonAsync<Pokemon>($"https://pokeapi.co/api/v2/pokemon/{name}");
}
```

> [!TIP]
> I en ekte applikasjon bør du injisere `IHttpClientFactory` fremfor å opprette `HttpClient` direkte – men for en enkel øvelse holder dette fint.
