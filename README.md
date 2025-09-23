UHC
===

  -Este plugin actualmente esta en work in progress, por lo que puede aun ser inestable, presentar bugs, o mecanicas que no estar aun funcionando como deberian. y esta diseñado para facilitar la implementacion del modo de juego ultrahardcore.

  -Este plugin esta preparado para funcionar con paper 1.20.5


# Modulos

Este plugin ofrece múltiples módulos. Cada módulo tiene un “icono” en el inventario de UHC que puede abrirse mediante /uhc o /uhc show.

Vista de ejemplo del inventario:

![Example Inventory](images/example-inventory.png)

Al pasar el cursor sobre el icono se mostrará el estado y la configuración del módulo:

![Example config](images/example-inventory-with-config.png)

Si tienes permiso y el módulo se puede desactivar, puedes hacer clic en el icono para alternar el estado de activado del módulo. Luego deberás hacer clic en un bloque de lana verde para confirmar que deseas alternar el módulo y así evitar clics accidentales.

Lista de módulos que proporciona el plugin:

- [HardDifficulty](docs/modules/HardDifficulty.md)
- [HealthRegen](docs/modules/HealthRegen.md)
- [GhastTears](docs/modules/GhastTears.md)
- [GoldenCarrotRecipe](docs/modules/GoldenCarrotRecipe.md)
- [GlisteringMelonRecipe](docs/modules/GlisteringMelonRecipe.md)
- [NotchApples](docs/modules/NotchApples.md)
- [Absorption](docs/modules/Absorption.md)
- [ExtendedSaturation](docs/modules/ExtendedSaturation.md)
- [PVP](docs/modules/PVP.md)
- [EnderpearlDamage](docs/modules/EnderpearlDamage.md)
- [WitchSpawns](docs/modules/WitchSpawns.md)
- [DeathMessages](docs/modules/DeathMessages.md)
- [DeathLightning](docs/modules/DeathLightning.md)
- [Nether](docs/modules/Nether.md)
- [TheEnd](docs/modules/TheEnd.md)
- [DeathBans](docs/modules/DeathBans.md)
- [AutoRespawn](docs/modules/AutoRespawn.md)
- [Timer](docs/modules/Timer.md)
- [HardcoreHearts](docs/modules/HardcoreHearts.md)
- [PercentHealth](docs/modules/PercentHealth.md)
- [Tier2Potions](docs/modules/Tier2Potions.md)
- [SplashPotions](docs/modules/SplashPotions.md)
- [GoldenHeads](docs/modules/GoldenHeads.md)
- [HeadDrops](docs/modules/HeadDrops.md)
- [DeathStands](docs/modules/DeathStands.md)
- [DeathItems](docs/modules/DeathItems.md)
- [TeamManager](docs/modules/TeamManager.md)
- [Horses](docs/modules/Horses.md)
- [HorseHealing](docs/modules/HorseHealing.md)
- [HorseArmour](docs/modules/HorseArmour.md)
- [ChatHealth](docs/modules/ChatHealth.md)
- [NerfQuartzXP](docs/modules/NerfQuartzXP.md)

Cada modulo tiene su apartado en config.yml y se ve como:

```yaml
modules:
  harddifficulty:
    ... etc ..
  healthregen:
    ... etc ...
```
Todos los modulos tienen la opcion a ser deshabilitados o habilitados 

```yaml
load: true/false
```

Si esto se establece en false, entonces el módulo ni siquiera se carga al iniciar el plugin y no aparecerá en el juego. Usa esto para eliminar cualquier módulo que no quieras ejecutar en absoluto.

Cualquier módulo desactivable también contendrá:

```yaml
enabled: true 
```

Este es el estado inicial que se asignará al módulo después de cargarse. Cualquier cambio realizado mediante el comando uhc o a través del inventario de configuración se guardará en esta opción, de modo que los cambios se conserven entre partidas/recargas.

También hay una lista de pesos:

```yaml
weights:
  harddifficulty: 0
  healthregen: 40
  ghasttears: 20
  goldencarrotrecipe: 30
  ...
  ...
```
Los módulos se ordenan según sus pesos dentro del inventario de /uhc. Modificar estos números cambiará el orden.

También puedes añadir “espaciadores” (espacios vacíos falsos) al inventario modificando la opción de spacers:

```yaml
spacers: []
    V
spacers: 
- 10
- 30
```

Cada número es un peso en el que se colocará un icono de espaciador. NOTA: el inventario solo puede manejar hasta 54 iconos en total (6*9).

## Comandos

[Información sobre comandos con banderas (flags).](docs/commands/Commands.md)

- [Team Commands](docs/commands/teams/TeamCommands.md)
- [Player Reset Commands](docs/commands/PlayerResetCommands.md)
- [/timer](docs/commands/timer.md)
- [/showhealth](docs/commands/showhealth.md)
- [/ghead](docs/commands/ghead.md)
- [/border](docs/commands/border.md)
- [/uhc](docs/commands/uhc.md)
- [/tpp](docs/commands/tpp.md)
- [/h](docs/commands/h.md)
- [/wlist](docs/commands/wlist.md)
- [/permaday](docs/commands/permaday.md)

Modificación de mensajes

Muchos de los mensajes enviados por el complemento pueden modificarse. Revisa la documentación sobre la modificación de mensajes
