## 1.10.2

- fix modded sword enchants appearing in the Enchanting Table or on loot even with "Enable the above in the Enchanting Table too" off and even if they are not primary for swords

[1.20.5/6] backport:

- add back in the removed "Enable the above in the Enchanting Table too" option which was not actually vanilla behavior

[1.19.3 - 1.20.4] backport:

- check item tags as well
- damage enchantments can be added to any weapon (not just axes) via books
- bump minimum Fabric loader version to 0.16.14
- update MixinExtras on Forge to 0.5.4

[1.18 - 1.19.2] backport:

- same as above but check `"c"` item tags

[1.16 - 1.17] backport:

- same as above but don't check item tags (and don't use MixinExtras on Forge)

## 1.10.1

- add `"c:enchantable/mob_loot_primary"` and `"c:enchantable/knockback_primary"` tags to be able to control whether Looting and Knockback appear in the Enchanting Table or on loot
- Looting and Knockback won't appear in the Enchanting Table or on loot when "Enable the above in the Enchanting Table too" option is off