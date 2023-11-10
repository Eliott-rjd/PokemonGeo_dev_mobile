# PokemonGeo

## Fonctionnalités

### Autorisations
Au lancement de l'application, l'utilisateur doit autoriser l'application à utiliser les données GPS. Ensuite, une alerte va s'afficher lui demandant d'activer la localisation si elle n'est pas encore activée. Une fois que l'utilisateur accepte, il est redirigé dans les paramètres du téléphone afin d'activer la localisation.
Au cours de l'utilisation de l'application, si l'utilisateur désactive sa localisation, le message d'alerte s'affichera à nouveau.

### Pokedex
Tout d'abords, nous avons un pokedex qui va afficher tous les pokemons de la 1ère génération. Pour chaque pokemon, on va pouvoir retrouver une image de celui-ci, son nom, ses types et son numéro dans le pokedex. Deplus, si le pokemon est capturé, une pokeball va s'afficher à coté de son numéro.
En cliquant sur un des pokemon, on va pouvoir afficher les détails le concernant. On retrouve alors toutes les données précédentes, mais aussi les images correspondantes aux types, le poids et la taille du pokemon.

### Database
Pour la base de donnée, on utilise SQLite.
A l'initilaisation, on va créer 3 tables:
- Une table pour tous les pokemons. Elle va contenir toutes les données vu précédemment afin de générer une liste de pokémons pour le pokedex. On va utiliser un fichier JSON contenant ces données afin de remplir notre base de donnée.
- Une table pour nos pokemons capturés. Elle va contenir les noms de pokemons capturés mais aussi leur statistiques, telle que leur level, leur attaque ou leur point de vie
- Une table pour l'inventaire. Elle va contenir nos différents items avec leur nom, leur quantité et leur prix.

Pour chaqune des ses tables différentes queries son créer afin d'accèder à son données ou de les modifier.

On utilise un singletion afin d'avoir qu'une seul instance de notre base de donnée à la fois.

### Map
Au premier accès à la map, l'utilisateur doit choisir un pokemon parmis les 3 starters.
Une fois sélecionné, il est redirigé sur la map, où on peut trouver l'utilisateur, représenté par un icon de dresseur, des pokemons, ainsi que des pokestops.
Lorsqu'on clique sur un pokemon, un combat se lance. Pour le moment le combat consiste simplment à cliquer une première fois sur un boutton combat, puis une seconde fois sur un boutton de capture. On ne prend pas encore en compte les statistiques des pokemons ou encore le taux de capture.
Si l'utilisateur n'a plus de pokeball, on ne peut plus capturé le pokemon, mais le combattre nous fournira une pokeball.


### Barre de navigation
Une barre de navigation en bas de l'application est pésentes afin de naviguer entre notre pokedex, note map et notre iventaire.
Si le GPS n'est pas activé, l'accès à la map est bloqué et un le message d'alerte demandant d'activer la localisation s'affiche de nouveau.

### Inventaire
A la manière du pokedex, une liste d'items est affichée, tel que des pokeballs, des potions. Elle comprend les noms des items, leur prix et leur quantité restantes. Pour le moment, il n'est pas possible d'acheter un item, mais cela pourrait faire parti d'améliorations futures.