# PLCommons Plugin Library
PLCommons ia a small library that provides common utilities for Bukkit/Spigot/Paper plugin development. 
Works for plugins for versions 1.8-1.21.

## Included Features
- **Languages System**: Provides a single manager class for managing multiple languages for your plugin.
- **Command Framework**: A simple and easy-to-use command framework through the use of
  [Aikar's](https://github.com/aikar/) [Annotation Command Framework](https://github.com/aikar/commands).
- **Event Callbacks**: By using `BukkitUtils.callEvent()` you can easily call events and react to their
    results through a callback interface.
- **Static Utility Classes**:
  - `PluginLogger`: For quick-and-easy logging of messages and/or exceptions to the console.
  - `DebugLogger`: For logging debug messages to the console, with the ability to enable/disable debug 
    mode through the plugin configuration.
  - `SchedulerUtils`: Provides a more functional way of using Bukkit's built-in scheduler.
  - `BukkitUtils`: Provides some generic utility methods for Bukkit/Spigot/Paper plugin development.

## License
This project is licensed under the [MIT License](LICENSE.txt). This project also references other 
projects, which are listed [here](REFERENCES.md).
