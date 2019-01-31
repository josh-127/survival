Survival
========

An infinite exploration game.

![Screenshot](/screenshot.png?raw=true)

Default Controls
----------------
Keyboard and mouse controls are hard-coded for now.

| Action/Button     | Action                     |
|-------------------|----------------------------|
| W, A, S, D        | Move horizontally          |
| Space             | Jump                       |
| T                 | Spawn human                |
| Y                 | Spawn particles            |
| Mouse movement    | Look around                |
| Left mouse button | Break block                |
| 1                 | Toggle block visibility    |
| 2                 | Toggle entity visibility   |
| 3                 | Toggle particle visibility |
| 4                 | Toggle sky visibility      |
| 5                 | Toggle cloud visibility    |
| 6                 | Toggle UI visibility       |


Building
--------

### Requirements
| Dependency   | Version         |
|--------------|-----------------|
| Java JDK     | 11.0.2 or later |
| Apache Maven | 3.6.0 or later  |

### For Windows 7, 8.1, or 10
Install Chocolatey.
https://chocolatey.org/docs/installation#installing-chocolatey

Install SDK and Build System:
```
choco install jdk11
choco install maven
```

Install any IDE:
```
choco install eclipse
choco install intellijidea-(community|ultimate)
choco install netbeans
```

To build on command line:
```
mvn install
```

### For macOS
Will support later.

### For Ubuntu (server only)
Will support later.

### Other distributions
Unsupported.
