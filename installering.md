Installering
===

Dette er programvaren vi trenger for kurset.

På Mac kan man bruke [homebrew](https://brew.sh/) til det meste av installasjonen.

## Del[0]

### IntelliJ
[Nettside](https://www.jetbrains.com/idea/)

Om alt har gått som det skal, har du allerede lisens
på Ultimate-utgaven. Husk å logge inn i programmet
(Register… i Help-menyen) med *bekk-epostadressen* din.

![Mac](./img/mac_logo.png)

`brew install --cask intellij-idea`

![Windows](./img/win_logo.png)

`winget install JetBrains.IntelliJIDEA.Ultimate`

### Node.js
[Nettside](https://nodejs.org/en/)

![Mac](./img/mac_logo.png)
`brew install node`

![Windows](./img/win_logo.png)
`winget install OpenJS.NodeJS`

### Git
[Nettside](https://git-scm.com/downloads)

![Mac](./img/mac_logo.png)
`brew install git`

![Windows](./img/win_logo.png)
`winget install Git.Git`

### En http-klient (for eksempel Bruno)

[Nettside](https://www.usebruno.com/)

![Mac](./img/mac_logo.png)
`brew install --cask bruno`

![Windows](./img/win_logo.png)
`winget install Bruno.Bruno`

## Del[1]

### Docker

Vi har behov for å kjøre docker-containere.
Om du allerede har lisens på Docker desktop, anbefales denne.

Ellers finnes det gratis alternativ.

[Se detaljer om installering og bruk av docker](./del_1/Docker_Jukselapp.md)

## Bruk av Brewfile
![Mac](./img/mac_logo.png)

Om du bruker Homebrew, kan du håndtere alle installasjone
med et manifest. Du kan kopiere/laste ned det [herfra](./Brewfile).

Deretter installeres alt i filen med
`brew bundle --file ` og plasseringen til BrewFile.

Dokumentasjon finnes [her](https://docs.brew.sh/Manpage#bundle-subcommand).

