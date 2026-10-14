# Backend workshop

## Innledning

- http
- mvc
- rest

En enkel oppskrift for å lage et typisk API-prosjekt.
Du kan velge om du vil bruke dotnet eller Spring Boot.

## Oppgaver

<table>
    <thead>
    <tr>
        <th>Lag et rest-api i Dotnet</th>
        <th>Lag et rest-api i Spring Boot</th>
    </tr>
    </thead>
    <tr>
        <td>
            Du kan bruke <a href="https://code.visualstudio.com/">VsCode</a> 
            eller en hvilken som helst annen teksteditor.
            Mange foretrekker <a href="https://www.jetbrains.com/rider/">Rider</a> 
            til c#-programmering.
            Du må også installere <a href="https://dotnet.microsoft.com/en-us/download">
            .NET 10 SDK herfra</a>.
            <p>
                <img src="../img/mac_logo.png" /> <code>brew install --cask dotnet-sdk</code>
            </p>
            <p>
                <img src="../img/win_logo.png" /> <code>winget install Microsoft.DotNet.SDK.10</code>
            </p>
        </td>
        <td>
            Det anbefales å bruke 
            <a href="https://www.jetbrains.com/idea/">IntelliJ</a> til oppgavene.
            Den kan installeres fra https://www.jetbrains.com/idea/download/ eller
            med Homebrew eller Winget. Spring-støtten (bl.a. prosjekt-wizarden) krever lisens for
            IntelliJ IDEA Ultimate. Du trenger også en JDK (versjon 21 eller nyere), som IntelliJ kan laste ned for deg.
            <p>
                <img src="../img/mac_logo.png" /> <code>brew install --cask intellij-idea</code>
            </p>
            <p>
                <img src="../img/win_logo.png" /> <code>winget install JetBrains.IntelliJIDEA.Ultimate</code>
            </p>
        </td>
    </tr>
    <tr>
        <td>1: <a href="../sommer/dotnet/nytt-prosjekt.md">Lag et nytt prosjekt i ASP.net</a>.</td>
        <td>1: <a href="../del_0/nytt-prosjekt.md">Lag et nytt prosjekt i Kotlin og Spring Boot</a>.</td>
    </tr>
    <tr>
        <td>2: <a href="../sommer/dotnet/database-integrasjon.md">Databaseintegrasjon</a>.</td>
        <td>2: <a href="../del_1/database-integrasjon.md">Databaseintegrasjon</a>.</td>
    </tr>
    <tr>
        <td>3: <a href="../sommer/dotnet/aspnet-REST.md">REST</a>.</td>
        <td>3: <a href="../del_2/Spring-REST.md">REST</a>.</td>
    </tr>
</table>
