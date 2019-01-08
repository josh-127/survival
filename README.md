Survival
========

An infinite exploration game.

![Screenshot](/screenshot.png?raw=true)

Default Controls
----------------
Keyboard and mouse controls are hard-coded for now.

| Action/Button     | Action                   |
|-------------------|--------------------------|
| W, A, S, D        | Move horizontally        |
| Space             | Jump                     |
| R                 | Spawn player             |
| T                 | Spawn human              |
| Mouse movement    | Look around              |
| Left mouse button | Break block              |
| Tab               | Toggle cursor mode       |
| 1                 | Toggle block visibility  |
| 2                 | Toggle entity visibility |
| 3                 | Toggle sky visibility    |
| 4                 | Toggle cloud visibility  |
| 5                 | Toggle UI visibility     |


Building
--------

### Requirements
| Dependency   | Version            |
|--------------|--------------------|
| Java JDK     | 1.8.0_191 or later |
| Apache Maven | 3.6.0 or later     |

### For Windows 7, 8.1, or 10 (method 1)
Install Chocolatey.
https://chocolatey.org/docs/installation#installing-chocolatey

Install SDK and Build System:
```
choco install jdk8
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

### For Windows 7, 8.1, or 10 (method 2)
**Use this method if you're building past releases with different values from the requirements list (dependency and version). Any breaking change in the JRE (even minor releases such as 1.7 to 1.8) may change game behavior, and as a result, is a different version from the release you downloaded.**
Manually install everything from the requirements list and an IDE.

### For macOS
Will support later.

### For CentOS 7
Will support later.

### Other distributions
Unsupported.
If you want support for another distribution, open up an issue in the issue tracker.
