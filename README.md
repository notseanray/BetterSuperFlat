## Better Better Super Flat

Just... Don't use this. It's a hacky solution that technically works but should not be on any client because it compeletely breaks superflat worlds. The 1.17 version will be updated (and be much better) once quacon updates.

### Usage SinglePlayer:
Select the `BetterSuperFlat` generation option in the create world screen.


### Usage Multiplayer:
In the `server.properties` change `level-type=default` to `level-type=quaconcmp`

#### Does the following:
- All dimensions are void with barrier block floor ontop of chunk outlines
- Has all the biomes and structure bounding boxes of vanilla generation included

  #### Floor pattern in every dimension:
  ![floor](screenshots/Floor.png?raw=true "Floor")

A lot of code borrowed from [jsorrell/skyblock](https://github.com/jsorrell/skyblock)
