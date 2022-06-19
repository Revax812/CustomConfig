
![Header](./header.png)
# CustomConfig
This Custom Config allows you to create custom configs.

The following should be roughly in the main class:

```java
//Creating two separate custom configs
private final Config config = new Config(this, "config.yml");
private final Config locations = new Config(this, "settings/", "locations.yml", true);
```

The first config is named config.yml and located in YourPlugin/config.yml.
The second config is named locations.yml and located in YourPlugin/settings/locations.yml.

In addition, the defaults are also stored in the second config.
This means that you can create folders and YML files in "ressources", which are thus automatically saved.

After that, you should create these methods:

```java
//Creating methods for the custom configs
public Config getConfiguration() {
        return config;
    }

public Config getLocations() {
        return locations;
    }
```

Now you can call these custom configs via the main class.
