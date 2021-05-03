## Introduction to the Pet Module
The pet module provides a system to collect, summon and upgrade pets, based on MythicMobs and LibsDisguises (or a similar plugin that works with MM). In the default implementation, pets are stored as ItemStacks, that can be interacted with and bred through a breeding menu. However you aren't forced to use this system and can just build a wrapper around the provided API, to make your custom implementation work. It is advised to look at the [Example Configuration](../examples/Pets.yml) while reading this guide, to better understand how to setup the module properly.

### Configuring Pet Stats
Stats are levelable values on pets, that can be retrieved through the API. An example usecase would be to apply buffs to the player, based on their currently active pet's stats. Stats have the following properties:
- name = The display name of the stat. Must be a **sinlge** word.
- description = The description of the stat on the pet ItemStacks.
- statgrowth = When multiplied with the pet tier, the maximum level of the skill is formed. 
- categories = Only pets in the listed categories will have the stat. A category can be any unique string.

### Configuring Pet Rarities
Rarities are a system to divide pets into different tiers. They need to be sorted from least to most rare and have the following properties:
- name = The display name of the rarity tier.
- tier = The tier number from 1 to X. Needs to be **in order** and **without interruption** for breeding to work.
- color = The ChatColor of the pet ItemStacks.
- material = The **unique** Material of the pet ItemStacks.

The WauzPetRarity class, provides a static API to get all rarities as list, by name or from a pet ItemStack.

### Configuring Skill Progression

### Configuring Pet Types

### Obtaining Pets

### Summoning and Unsummoning Pets

### Breeding Pets
