# JanConf
Simple configs for your projects

# Features
* Comments are attached to the key and do not disappear after saving 
* Very simple, because any value is a string, but it is also possible to get another one from it!
* There is a version for minecraft plugins Bukkit-Spigot-Paper


# File structure
## Keys
```jconf
# Simple comments!
key1:value1
key2: value2
volume:1.0
auto: true
```
**Any value can be obtained as a string!**

## Groups
```jconf
# This is settings
settings:
  # Comments
  # is available! (programmatically)
  volume: 1.0
  color: RED
  hello_message: [{"text":"Hello <3", "bold":true}]
  custom:
    brightness: 100
```


# Usage
## Create
```java
JanConf conf = new JanConf();
conf.put("key1", "value1", "Simple comments!");
conf.put("volume", 1.0f);

// Or

JanConf conf = new JanConf()
        .put("key1", "value1", "Simple comments!")
        .put("volume", 1.0f);
```

## Import/Export
```java
String input = "# Simple comments!\n"+
        "key1:value1\n"+
        "key2: value2\n"+
        "volume:1.0\n"+
        "auto: true\n";

JanConf conf = new JanConf(input);
conf.put("key1", "new value 2", "Simple comments!");

final int INDENT_SPACES = 2;
System.out.println(conf.toString(INDENT_SPACES));
// # Simple comments!
// key1: new value 2
// key2: value2
// volume:1.0
// auto: true
```

## Groups
```java
// FileUtil from JavaNeoUtil library!

JanConf c = new JanConf(FileUtil.getText("main.jconf", ""));
JanConf settings = c.getGroup("settings");
float volume = settings.getFloat("volume", 0.5f);
String color = settings.get("color", "GREEN");
```
