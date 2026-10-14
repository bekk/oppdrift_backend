# Del 2 – Databaseintegrasjon

> [!NOTE]
> I denne øvelsen jobber vi videre med web-løsningen fra del 1.
> Hensikten er å få en kjørende REST-applikasjon med database for lagring.

Ta utgangspunkt i det som ble gjort i [første del](./nytt-prosjekt.md) og jobb videre med samme kodebase.

<details>
  <summary>Klikk for eksempelkode fra del 1</summary>
  <p>

```csharp
// Program.cs
using MittProsjekt;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddOpenApi();
builder.Services.AddHostedService<JobService>();

var app = builder.Build();
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}
app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();
app.Run();

// UserController.cs
[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    [HttpGet]
    public string Get() => "Vegard";
}

// JobService.cs
public class JobService : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        await Task.Delay(500, stoppingToken);
        while (!stoppingToken.IsCancellationRequested)
        {
            Console.WriteLine("Hei");
            await Task.Delay(2000, stoppingToken);
        }
    }
}
```

  </p>
</details>

## EF Core og SQLite-oppsett

Database! Det er på tide å legge til en database i løsningen vår.

Vi bruker **Entity Framework Core** som ORM og **SQLite** som database. SQLite lagrer alt i én fil og krever ingen egen databaseserver å sette opp – perfekt for utvikling.

> [!NOTE]
> Et ORM (Object-Relational Mapper) oversetter mellom C#-objekter og databasetabeller, slik at du slipper å skrive SQL for vanlige operasjoner. EF Core genererer SQL automatisk når du kaller metoder som `ToListAsync()` eller `SaveChangesAsync()`. Kotlin-varianten av denne workshopen bruker rå SQL via JDBC, slik at du selv skriver og leser resultater – mer jobb, men full kontroll.
>
> Vil du ha noe midt imellom i .NET, finnes **Dapper** – et lettvekts-bibliotek som lar deg skrive SQL selv, men håndterer mapping til C#-objekter for deg.

Legg til de nødvendige pakkene:

```bash
dotnet add package Microsoft.EntityFrameworkCore.Sqlite
dotnet add package Microsoft.EntityFrameworkCore.Design
```

Installer også EF Core-verktøyet globalt (trengs for å kjøre migrasjoner):

```bash
dotnet tool install --global dotnet-ef
# Har du det fra før, oppdater til samme versjon som EF Core-pakkene:
dotnet tool update --global dotnet-ef
```

> [!TIP]
> Får du `command not found: dotnet-ef` på Mac/Linux, ligger ikke `~/.dotnet/tools` i PATH.
> Legg til `export PATH="$PATH:$HOME/.dotnet/tools"` i `~/.zshrc` og åpne en ny terminal.

## Definer en entitet

> [!NOTE]
> Har du laget `User.cs` i del 1, skal du erstatte den med klassen under. Kode fra del 1 som gjør
> `new User("...", 42)` vil da ikke kompilere lenger – det fikser vi når vi oppdaterer kontrolleren under.

Lag en ny fil `User.cs` i prosjektmappa:

```csharp
namespace MittProsjekt;

public class User
{
    public int Id { get; set; }
    public string Title { get; set; } = "";
    public string Username { get; set; } = "";
}
```

## Definer en DbContext

Lag en ny fil `AppDbContext.cs`:

```csharp
using Microsoft.EntityFrameworkCore;

namespace MittProsjekt;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<User> Users => Set<User>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<User>().HasData(
            new User { Id = 1, Title = "Konsulent", Username = "Vegard" },
            new User { Id = 2, Title = "Artist",    Username = "Donny Benet" }
        );
    }
}
```

## Registrer databasen i Program.cs

`UseSqlite` kommer fra `Microsoft.EntityFrameworkCore`-pakken, og `AppDbContext` ligger i namespacet `MittProsjekt`. Begge krever `using`-statements øverst i `Program.cs`:

```csharp
// Program.cs
using Microsoft.EntityFrameworkCore;
using MittProsjekt;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddOpenApi();
builder.Services.AddHostedService<JobService>();
builder.Services.AddDbContext<AppDbContext>(options =>  // legg til denne
    options.UseSqlite("Data Source=mittprosjekt.db"));

var app = builder.Build();
// ... resten av fila er uendret
```

## Bruk databasen i kontrolleren

Erstatt hele innholdet i `Controllers/UserController.cs` med koden under, slik at den bruker `AppDbContext`.
Har du laget ekstra endepunkter i del 1 (meldinger, pokemon) og vil beholde dem, flytt dem til en egen kontroller først
(f.eks. `MessageController.cs`).

```csharp
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace MittProsjekt.Controllers;

[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    private readonly AppDbContext _db;

    public UserController(AppDbContext db)
    {
        _db = db;
    }

    [HttpGet]
    public async Task<List<User>> GetAll() =>
        await _db.Users.ToListAsync();

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var user = await _db.Users.FindAsync(id);
        return user is null ? NotFound() : Ok(user);
    }
}
```


## Kjør migrasjoner

EF Core bruker migrasjoner for å holde styr på endringer i databaseskjemaet – tilsvarende det Flyway gjør med SQL-filer.

> [!IMPORTANT]
> `dotnet ef` bygger prosjektet, så koden må kompilere. Stopp `dotnet watch` (`Ctrl+C`) før du kjører kommandoene under.

Opprett den første migrasjonen:

```bash
dotnet ef migrations add InitialCreate
```

Dette oppretter en `Migrations/`-mappe med genererte filer som beskriver databasestrukturen. Kjør deretter migrasjonen mot databasen:

```bash
dotnet ef database update
```

Du skal nå ha en fil `mittprosjekt.db` i prosjektmappa.

Start appen igjen med `dotnet watch run` og gå til `/user` på den porten som vises i terminalen for å se brukerne fra databasen.

## In-memory database (alternativ)

Ønsker du å slippe filen og alt forsvinner når appen stoppes, kan du bruke EF Cores in-memory-provider i stedet:

```bash
dotnet add package Microsoft.EntityFrameworkCore.InMemory
```

I `Program.cs`, bytt ut `AddDbContext`-linjen som bruker Sqlite med denne:

```csharp
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseInMemoryDatabase("mittprosjekt"));
```

og legg til denne blokken rett etter linjen `var app = builder.Build();` som allerede finnes:

```csharp
// Opprett databasen og legg inn testdataene fra HasData ved oppstart
using (var scope = app.Services.CreateScope())
{
    scope.ServiceProvider.GetRequiredService<AppDbContext>().Database.EnsureCreated();
}
```

> [!NOTE]
> In-memory-databasen bruker ikke migrasjoner. Uten `EnsureCreated()` blir heller ikke testdataene fra
> `HasData` lagt inn, og `/user` vil returnere en tom liste.
>
> In-memory-provideren er fin for å leke seg, men Microsoft fraråder den til tester og produksjon – den oppfører seg
> ikke som en ekte relasjonsdatabase.

## Videre arbeid

Fortsett med oppgavene fra [del 1](./nytt-prosjekt.md) og bruk det du har lært om databaser:

- Legg til endepunkter for å opprette, oppdatere og slette brukere (POST, PUT, DELETE)
- Legg til et nytt felt på `User`-entiteten, generer en ny migrasjon og kjør den
- La bakgrunnsjobben fra del 1 skrive noe til databasen med jevne mellomrom
