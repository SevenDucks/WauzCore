## :hamster: Introduction to the Pet Module
The pet module provides a system to collect, summon and upgrade pets, based on MythicMobs and LibsDisguises (or a similar plugin that works with MM). In the default implementation, pets are stored as ItemStacks, that can be interacted with and bred through a breeding menu. However you aren't forced to use this system and can just build a wrapper around the provided API, to make your custom implementation work. It is advised to look at the [Example Configuration](../examples/Pets.yml) while reading this guide, to better understand how to setup the module properly.

### Pet Stats
Stats are levelable values on pets, that can be retrieved through the API. An example usecase would be to apply buffs to the player, based on their currently active pet's stats. While the system is designed to have multiple stats, that can be levelled seperately, you could also just have a generic global stat like "level" or "experience". Stats have the following properties:
- name = The display name of the stat. Must be a **single** word.
- description = The description of the stat on the pet ItemStacks.
- statgrowth = When multiplied with the pet rarity index, the maximum level of the skill is formed. 
- categories = Only pets in the listed categories will have the stat. A category can be any unique string.

The WauzPetStat class provides a static API to get all stats as list or by name.

To get the current value of a pet stat, you can use the static WauzActivePet.getPetStat method. If the player has no active pet or the pet doesn't have such stat, this method returns always 0.

### Pet Rarities
Rarities are a system to divide pets into different tiers. They need to be sorted from least to most rare and have the following properties:
- name = The display name of the rarity tier.
- color = The ChatColor of the pet ItemStacks.
- material = The **unique** Material of the pet ItemStacks.

The WauzPetRarity class provides a static API to get all rarities as list, by name or from a pet ItemStack.

### Skill Progression
The levels section in the configuration allows you to create a skill progression. This doesn't refer to leveling single pets, but to the player's taming / breeding skill. With these levels, you can gate the breeding of pet rarities behind an experience value. Each level has the following properties:
- exp = The amount of experience needed for this level.
- breedtime = A list of breedable rarities at this level. The number implies how many seconds a freshly bred pet needs to hatch, before you can summon it. If you don't want this, simply set it to 0.

You are responsible to handle how experience is obtained and stored. The most simple way would be awarding exp on the PetObtainEvent and storing it inside a config file or database. If you don't want a progression system at all, you could just configure a single level, that requires 0 exp and has all pet rarities listed in it.

The WauzPetBreedingLevel class provides a static API to get specific levels.

### Pet Types
The pets section in the configuration allows you to manage what types of pets can be obtained. Pets have the following properties:
- mob = The name of the MythicMob the pet is based on. Should always be of base type wolf, but can be disguised through LibsDisguises or similar plugins.
- category = A category can be any unique string. Pets that share a category can be bred together to form a higher rarity pet of the same category.
- rarity = The rarity key of the pet.
- messages = An optional list of messages the pet can say when summoned.

The WauzPet class provides a static API to get all types as list, by name or with a category / rarity filter.

### Obtaining Pets
There are four ways to obtain pets as of right now:
1. Breeding two existing pets with the BreedingMenu (or a custom implementation). This triggers a PetObtainEvent.
2. With a scroll. To create one rename a name tag to "Scroll of Summoning" and set the first line of its lore to the pet type. Right clicking it will create a pet egg and triggers a PetObtainEvent.
3. With the /getpet Command. This requires either OP or the wauz.system permission.

If you don't want to use pet ItemStacks, but want to create a custom menu instead, you can just manually generate pet eggs with the static WauzPetEgg.getEggItem method. This way you could store the items in the backend and display them in an UI when needed. The PetEggUtils class provides some additional methods to interact with the eggs. Since they are still normal Minecraft items, you can easily serialize and store them in a config file or database instead of the player's inventory.

### Summoning and Unsummoning Pets
The default implementation simply lets you summon pets by right clicking their eggs. A second click makes the pet disappear again. For custom implementations you can use the static WauzPetEgg.tryToSummon method to summon pets manually.

With the WauzActivePet class, you can access the currently active pet of a player or determine the owner of a pet entity. Its tryToUnsummon method can be used to manually unsommon pets. This is not only useful for custom implementations, but can also be used to temporarily remove the pet, for example when switching worlds in a network.

### Renaming Pets
The default implementation lets you rename pets with the /rename command, which requires the wauz.normal permission that is enabled by default. The command also makes sure that the player input doesn't exceed 16 characters and contains no invalid symbols. You can also manually rename the pet by changing the name of its egg ItemStack and resummoning it.

### Leveling Pets
The stats of your pets can be increased by food. The default implementation allows you to feed a pet by dragging food onto its egg ItemStack. The food will only be consumed, if it contains stats that are applicable for the pet and those are not maxed out yet.

If an item is seen as pet food, is determined by its lore. It needs to contain the words "Pet Food" and a line for each stat that it posseses. A stat line has to look like this: "Pet %StatName%: %Value%". The % in this example is just used to indicate variables and is not supposed to be part of the lore.

When using a custom implementation you can feed a pet by calling the static WauzPetEgg.tryToFeed method.

### Breeding Pets
ToDo
