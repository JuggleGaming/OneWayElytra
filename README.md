
# OneWayElytra
Do you want to glied from your spawn into the unknown? Even without ever entering the end? This plugin adds a one-way-elytra for your spawn. Players are able to fly into the air and boost once they are in the air.

<img src="/github/example.gif" width="400">

## Features
- 🪽 fly into the air without an elytra
- 🚀 boost once while flying
- ▦ worldguard flag

## Commands
**/onewayelytra | /owe**
_Shows you all the commands._

**/onewayelytra location <add/delete> [name]**
_Add or delete a location._

**/onewayelytra location set [name] radius [number]**
_Set the locations radius._

**/onewayelytra location list**
_List all locations._

## Worldguard
**/rg flag [regionname] onewayelytra allow**
_Set the flag for a specific region._
## Configurations

<details>
<summary>config.yml</summary>

```
##
## OneWayElytra | config.yml
##

# Do not change anything unless you know what you are doing!
config-version: 1

# Available languages (stored inside the jar-file): EN, DE
language: EN

# default radius in blocks
radius: 16

# multiplier for the boost (standard: 5)
boostMultiplier: 5

# allow gliding in adventure mode
adventure: true

locations: {}
```


</details>
