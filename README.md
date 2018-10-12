Survival
========

An infinite exploration game.

![Screenshot](/screenshot.png?raw=true)

Default Controls
----------------
Keyboard and mouse controls are hard-coded for now.

| Action/Button     | Action            |
|-------------------|-------------------|
| W, A, S, D        | Move horizontally |
| Space             | Jump              |
| R                 | Spawn player      |
| T                 | Spawn human       |
| Y                 | Spawn slime       |
| U                 | Spawn goat        |
| Mouse movement    | Look around       |
| Left mouse button | Break block       |

Building
--------

### Requirements
| Dependency   | Version            |
|--------------|--------------------|
| Java JDK     | 1.8.0_181 or later |
| Apache Maven | 3.5.4 or later     |

### For Windows 7, 8, 8.1, or 10 (method 1)
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

### For Windows 7, 8, 8.1, or 10 (method 2)
**Use this method if you're building past releases with different values from the requirements list (dependency and version). Any breaking change in the JRE (even minor releases such as 1.7 to 1.8) may change game behavior, and as a result, is a different version from the release you downloaded.**
Manually install everything from the requirements list and an IDE.

### For macOS
Will support later.

### For Ubuntu 18.04 LTS
Will support later.

### Other distributions
Unsupported.
If you want support for another distribution, open up an issue in the issue tracker.
