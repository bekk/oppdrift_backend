Avinstallering 🧹
===

Om du ønsker, kan du avinstallere programvaren vi har brukt etter kurset.

## Docker

Om du vil fjerne containere vi har kjørt i kurset, se [oppskrift her](./del_1/Docker_Jukselapp.md#docker-kommandoer).

## Windows

[Generell beskrivelse.](https://support.microsoft.com/en-us/windows/uninstall-or-remove-apps-and-programs-in-windows-4b55f974-2cc6-2d2b-d092-5905080eaf98)

### Winget

For å få en liste over hva som er installert med [winget](https://learn.microsoft.com/en-us/windows/package-manager/winget/) skriver du:

```console
winget list
```

For å avinstallere de oppføringene som har *winget* som *source*: `winget uninstall`

## Mac

[Generell beskrivelse.](https://support.apple.com/en-us/102610)

### Homebrew

Om du har brukt [Homebrew](https://brew.sh/) til å installere noe, må det også avinstalleres med den.
For å få en liste over hva som er installert med Homebrew skriver du:

```console
brew list
```

Listen er delt opp i *formula* (software-pakker; typisk cli-applikasjoner)
og *cask* (MacOS-applikasjoner).

For å avinstallere noe fra listen brukes `brew uninstall`.

Se mer detaljer med `brew uninstall -help`.

## Verktøy

- [ ] **[IntelliJ](https://www.jetbrains.com/idea/)**: 
Om du ikke har behov for IntelliJ mer, avinstalleres den [slik](https://www.jetbrains.com/help/idea/uninstall.html#toolbox).
Eventuelt, om du installerte med Homebrew: `brew uninstall intellij-idea`.
- [ ] **[Git](https://git-scm.com/)**: Du vil antagelig ikke avinstallere Git. 😂
- [ ] **[Node](https://nodejs.org/en)**: Avinstalleres som normale program eller med `brew uninstall node`.
- [ ] **[Postman](https://www.postman.com/)**: Avinstalleres som andre normale applikasjoner.
- [ ] **Andre restklienter**: Om du installerte noen av [disse](./del_0/README.md#http-klienter).
- [ ] **[Docker desktop](https://www.docker.com/products/docker-desktop/)**: 
Om du installerte denne, kan den avinstalleres [slik](https://docs.docker.com/desktop/uninstall/)
eller med Homebrew `brew uninstall --cask docker`.
- [ ] **[Colima](https://github.com/abiosoft/colima)**:
Med homebrew `brew uninstall colima` og `brew uninstall docker`.
- [ ] **Andre docker-klienter**: Om du installerte noen av [disse](./del_1/Docker_Jukselapp.md#installasjon).
- [ ] **[Dotnet](https://dotnet.microsoft.com/en-us/)**: 
Dotnet sdk avinstalleres
[slik](https://learn.microsoft.com/en-us/dotnet/core/install/remove-runtime-sdk-versions?pivots=os-macos), 
eller med Homebrew `brew uninstall dotnet-sdk`.
- [ ] **[Fly.io](https://fly.io/docs/flyctl/install/)**: 
For å avinstallere klienten med Homebrew: `brew uninstall flyctl`.
Ellers er det en oppskrift [her](https://github.com/superfly/flyctl/issues/1099).
