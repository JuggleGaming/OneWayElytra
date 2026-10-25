## New features
- Added a command to give a onewayelytra to a specific player. (/owe give [player])
- Added two new flags: OWE-ITEM and OWE-ENTRY
  - To disable the ability to start flying everywhere, you can set the worldguard-flag `OWE-ITEM` to `false`.
  - To deny entry into an area while using your onewayelytra, set the flag `OWE-ENTRY` to `false`.

## Breaking changes
The worldguard-flag to enable the ability to fly without an owe-item (as before) has been renamed to `OWE-START` (previously: `ONEWAYELYTRA`). Please check whether this results in any issues and if you have to change the flag.

### Information
Worldguard version needed: 7.0.20
