# PLCommons Plugin Library
PLCommons is a small library that provides common functionality and utilities for Bukkit/Spigot/Paper plugin development for versions 1.8-1.21.

## Included Features
- **Command Framework**: A simple and easy-to-use command framework through the use of
  [aikar's](https://github.com/aikar/) [Annotation Command Framework](https://github.com/aikar/commands).
- **Languages System**: Provides a single manager class for managing multiple languages for your plugin through the use of
  aikar's [Locales](https://github.com/aikar/locales).
- **Event Callbacks**: By using `Events.callAnd()` you can easily call events and react to their
    results through a callback interface.
- **Utility Classes**:
  - `ItemStackFactory`: A fluent way of creating ItemStacks.
  - `Events`: Easy registration of listeners and calling of custom events.
  - `Scheduling`: A functional way of using Bukkit's scheduler.
  - `PluginLogger`: For quick-and-easy logging of messages and/or exceptions to the console.
  - `DebugLogger`: For logging debug messages to the console, with the ability to enable/disable debug 
    mode through the plugin configuration.

## Installation

### Maven
```xml
<repositories>
    <repository>
        <id>greenadine-snapshots</id>
        <url>https://repo.greenadine.dev/snapshots</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>dev.greenadine</groupId>
        <artifactId>plcommons</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Gradle
```gradle
repositories {
    maven { url 'https://repo.greenadine.dev/snapshots' }
}

dependencies {
    implementation 'dev.greenadine:plcommons:0.1.0-SNAPSHOT'
}
```

## License
This project is licensed under the [MIT License](LICENSE.txt). This project also references other 
projects, which are listed [here](REFERENCES.md).
