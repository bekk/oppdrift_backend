Selenium testeksempler
===

Noen veldig enkle eksempler på hvordan [Selenium Web Driver](https://www.selenium.dev/) kan brukes i en dotnet-test.

Browserne som brukes (Chrome og Firefox) må være installert på maskinen.

Testene ligger i [WebDriverTests.cs](./Bekk.Oppdrift.SeleniumExample/WebDriverTests.cs).

For å kjøre testene: 

```console
dotnet test --logger "console;verbosity=detailed"
```