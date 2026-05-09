# CLAUDE.md — Regles pour ce projet

## Contexte

Projet e-commerce front-end pour apprendre React et Redux.
L'objectif de l'utilisateur est d'**apprendre React et Redux** (state, stores, slices, hooks, composants). Il ne veut pas s'occuper du design/CSS.

## Role de Claude

- **Conseiller pedagogique** sur React et Redux : expliquer, guider, faire reflechir
- **Responsable du design/UI** : Claude implemente le visuel **complet** — structure JSX (HTML) + CSS + layout + style — en respectant le mockup. Quand l'utilisateur cree la logique (state, dispatch, handlers), Claude implemente le rendu : le `.map()`, les classNames conditionnels, la structure des composants dans le JSX, et le CSS associe

## Backend / API

- Le code backend se trouve dans : `C:\Users\speri\Desktop\e-commerce-api\backend`
- Consulter les modeles, controllers et routes du backend pour connaitre la structure des donnees retournees par l'API
- Lancement du backend (depuis bash sous Windows) :
  ```
  java -Dmaven.multiModuleProjectDirectory="C:/Users/speri/Desktop/e-commerce-api/backend" -classpath "C:/Users/speri/Desktop/e-commerce-api/backend/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain -f "C:/Users/speri/Desktop/e-commerce-api/backend/pom.xml" spring-boot:run
  ```
- Le backend demarre sur le port **8095**

## Design / Mockup

- Le design de reference se trouve dans : `D:/Brain/Projets/design-mockup.html`
- Toute implementation visuelle doit respecter ce mockup (couleurs, layout, typographie, espacements)
- Quand un nouveau composant ou une nouvelle page est cree, Claude consulte le mockup et applique le style correspondant

## Vault Obsidian

- Chemin : `C:\Users\speri\Desktop\Brain`
- Quand un conseil merite d'etre note, l'ecrire dans le vault
- Fichier Tips Redux : `Redux/Redux - Tips.md`
- Les liens vers les Tips depuis le dictionnaire doivent pointer vers la **section specifique** (ex: `[[Redux - Tips#Section|Alias]]`), pas juste vers le document
- Respecter la structure existante : `React/`, `Redux/`, `Projets/E-Commerce/`
- Respecter le format des notes existantes (frontmatter YAML, callouts Obsidian, wikilinks)

## Architecture Redux

- Un slice = une entite metier (products, cart, user, orders), pas une page ou une vue
- Ne jamais creer deux slices pour la meme entite (ex: productsSlice + productSlice)
- Un seul slice gere la liste ET le detail d'une entite

## Organisation des fichiers slice

Ordre dans un fichier slice :
1. Imports
2. Thunks (createAsyncThunk)
3. Slice (createSlice avec extraReducers)
4. Exports (actions + reducer)

Le thunk doit etre declare avant le createSlice car il est reference dans extraReducers.

## Reducers et Thunks

- Les reducers sont **synchrones**. Jamais de fetch, setTimeout ou await dans un reducer
- La logique asynchrone va dans les thunks (createAsyncThunk)
- Le 2e parametre d'un reducer dans extraReducers est une **action** (avec type et payload), pas une reponse HTTP
- Gerer les erreurs HTTP dans le thunk (if (!response.ok) throw), pas dans le reducer
- response.json() est une methode : ne pas oublier les ()

## TODOs dans les coquilles UI

- Les commentaires TODO decrivent **ce qu'il faut faire** (le quoi), pas **comment le faire**
- Ne jamais mentionner de nom de hook, fonction ou API specifique dans un TODO
- Exemple correct : `// TODO : Rediriger vers '/' si connexion reussie`
- Exemple incorrect : `// TODO : useEffect — si status === 'succeeded', appeler navigate('/')`

## Pieges courants

- Arrow function sans accolades dans addCase : `(state) => state.status = 'loading'` retourne la valeur au lieu de muter via Immer. Toujours utiliser des accolades : `(state) => { state.status = 'loading' }`
- Ne pas confondre `reducers` (actions internes) et `extraReducers` (actions externes / thunks)

## Authentification

- Backend JWT : endpoints `POST /api/auth/register`, `POST /api/auth/login` retournent `{ token, email, firstName, lastName }`
- Le token est stocke dans `localStorage` cote frontend (`token` et `user`)
- `utils.getAuthHeaders()` retourne les headers avec `Authorization: Bearer <token>`
- Endpoints proteges (cart, wishlist, orders, account) necessitent le token
- Endpoints publics (products, categories, promotions, auth) ne necessitent pas de token
- Slice auth : `features/auth/auth_slice.js` — thunks `loginUser`, `registerUser`, action `logout`
- Composant `Header.jsx` : affiche "Sign In" ou le prenom + Logout selon l'etat auth
- `ProtectedRoute.jsx` : redirige vers `/login` si pas de token

## Promotions & SSE

- Backend : `Promotion` entity liee a des `Product` (ManyToMany) et `Category` (ManyToMany)
- `PromotionScheduler` verifie toutes les 30s si des promos doivent etre activees/desactivees
- SSE endpoint : `GET /api/promotions/stream` (public, pas d'auth)
- Evenements SSE : `initial_state`, `promotion_started`, `promotion_ended`, `flash_sale_started`, `flash_sale_ended`, `heartbeat`
- Hook `useSse.js` : connecte l'EventSource, dispatch les events vers le slice `promotions`
- Slice promotions : `features/promotions/promotion_slice.js` — reducers sync `setActivePromotions`, `promotionActivated`, `promotionDeactivated`
- `PromotionBanner` : banniere full-width avec countdown, gold (standard) ou rouge+pulse (flash sale)
- `PromotionBadge` : badge "-X%" sur les product cards concernes

## Routes frontend

| Route | Page | Protection |
|-------|------|-----------|
| `/` | HomePage | Public |
| `/product/:id` | ProductDetailPage | Public |
| `/login` | LoginPage | Public |
| `/register` | RegisterPage | Public |
| `/account` | AccountPage | ProtectedRoute |

## Synchronisation CLAUDE.md

- Ce fichier (`e-commerce-api/CLAUDE.md`) et `reactredux-shop/CLAUDE.md` doivent toujours etre synchronises
- Toute modification dans l'un doit etre reportee dans l'autre
