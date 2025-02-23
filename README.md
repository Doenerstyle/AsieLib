# AsieLib (with fixed Bleeding Pipe vulnerability)

This repository is a fork of [AsieLib](https://github.com/Vexatos/AsieLib) for Minecraft 1.7.10 containing an **important security vulnerability fix** and a few other improvements.

## Security vulnerability fix in this fork

This fork fixes the serious [Bleeding Pipe](https://blog.mmpa.info/posts/bleeding-pipe) vulnerability (only for this mod itself, not for others - many mods are affected!) by simply removing the affected method, only leaving an error message explaining that.

This means it might break mods that use the affected `Base64#decodeToObject` method. However, to my knowledge, at least none of asie's popular mods utilize it.

**Please make sure that you're not affected through other mods.** A good start is to also install the [serializationisbad](https://github.com/dogboy21/serializationisbad) mod and/or check out their [list of affected mods](https://github.com/dogboy21/serializationisbad/blob/master/docs/mods.md).

If AsieLib is the only affected mod that you're using, switching to this fork should suffice.

## Other improvements of this fork

- **Fixed the bug** of Showcase lids (from the [Statues Mod](https://github.com/Doenerstyle/Statues)) never closing (asiekierka/Statues#15) (thanks @TCLProject)
- Fixed potential issues with other blocks that have inventories that use AsieLib (thanks @TCLProject)
- Added a Simplified Chinese localization (thanks @M3MEMonster)

## Development/Support/Maintenance

I am not planning to further update this mod.

**However**, please open issues if you encounter bugs or incompatibilities/crashes and PRs are always welcome, including new/updated localization files!

## License

The license is the same as in the original version and can be found [here](https://wiki.vexatos.com/wiki:licensing).
