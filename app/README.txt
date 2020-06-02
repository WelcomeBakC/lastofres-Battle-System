by Sean Park (seanp1225@hotmail.ca)

Features
  - turn based game
  - configuring players and npc through json files
  - 4 different character classes/schools:
  	Infantry, Command, Magic, Divinity
  	(could not use Korean file names)

Missing Features/Known Bugs:
  - Korean:
  	certain consoles may output '?' instead of Korean characters when printing to output
  - targetting types:
  	cannot target multiple actors
  	cannot restrict targetting (e.g. self targeted spells), so any 'spell' can be targetted towards anyone, including attacking yourself
  - npc actions:
  	cannot configure actions
  	no multi-hit skill
  - database connectivity:
  	cannot connect member and pull their character from database
  	cannot pull inventory from database
  - server-client socket networking:
  	cannot connect to like a server
  	no client web application
  - multiple npcs
  - game end:
  	game does not end, must be determined manually


Installation:
Unzip into a directory


Configuration:
Adding and configuring players:
	Edit players.json, following the json format of
	[
	  {
	    ...
	  },
	  {
	    "playerClass":"InfantryClass",
	    "focus": 100,
	    "intelligence": 50,
	    "agility": 55,
	    "name": "dummy infantry player",
	    "maxHealth": 80,
	    "health": 80,
	    "attack": 50,
	    "defence": 50
	  },
	  ...
	]
	To setup the player's character class, set "playerClass": to either "InfantryClass", "CommandClass", "MagicClass", or "DivinityClass".
	
	The rest of the stats are as named.

Configuring npc:
	Edit npc.json, following the json format of 
	{
	  "name": "dummy npc",
	  "maxHealth": 32000,
	  "health": 32000,
	  "attack": 100,
	  "defence": 300
	}
	The starts are as named.

