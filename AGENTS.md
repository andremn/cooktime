# AGENTS.md

Guidance for coding agents working in this repository.

Cook Time is a single-module Android app (`:app`) for managing recipes, written in Kotlin with Jetpack Compose. It's a personal practice project, so prefer current, idiomatic Android APIs over workarounds kept for older platforms.

## Environment

- Running `./gradlew` needs a JDK 17 or newer on `JAVA_HOME` or `PATH`. Android Studio's bundled JBR works.
- Gradle's daemon JVM (21, JetBrains) is pinned in `gradle/gradle-daemon-jvm.properties`. The foojay plugin downloads it if it's missing.
- The Android SDK path is in `local.properties`, which is gitignored.

## Commands

```bash
./gradlew assembleDebug                      # build the debug APK
./gradlew testDebugUnitTest                  # JVM unit tests
./gradlew testDebugUnitTest --tests "com.nicoapps.cooktime.ExampleUnitTest"   # single test class
./gradlew connectedDebugAndroidTest          # instrumented tests (needs a running emulator/device)
./gradlew lintDebug                          # Android lint
./gradlew installDebug                       # install on the connected emulator/device
adb shell am broadcast -n com.nicoapps.cooktime/.debug.SampleRecipesReceiver   # add sample recipes (debug builds)
```

The sample recipes live in `app/src/debug/` (`SampleRecipes`). The command only adds the ones missing by name, so it's safe to run again. `connectedDebugAndroidTest` uninstalls the app when it finishes, which wipes its data, so run the command again after it.

To run on an emulator, list the available AVDs with `emulator -list-avds` and start one with `emulator -avd <name>`. The binary is in the SDK's `emulator/` directory.

Before handing work back, the build must pass with no new Kotlin compiler warnings, deprecations included.

## Toolchain

- Gradle 9, AGP 9, Kotlin 2.x, KSP 2 (Room and Hilt use KSP, not kapt).
- AGP 9 has built-in Kotlin. Don't apply `org.jetbrains.kotlin.android` in the module: the root `build.gradle.kts` declares it only to pin the Kotlin Gradle plugin version. Configure Kotlin through `kotlin { compilerOptions { ... } }`, not `kotlinOptions`.
- The Compose compiler comes from the `org.jetbrains.kotlin.plugin.compose` plugin, whose version must match Kotlin's.
- `compileSdk` uses minor API levels (`release(37) { minorApiLevel = 2 }`). New Compose releases may require a higher minor level.
- Dependency versions are declared inline in the `build.gradle.kts` files. There's no version catalog. Compose artifacts take their versions from the Compose BOM.

## Architecture

Package root: `app/src/main/java/com/nicoapps/cooktime/`.

### Data flow

`ViewModel` → `RecipeRepository` / `RecipeExecutionRepository` (interfaces in `data/`) → `Local*Repository` → Room DAOs → entities.

- **Model vs. entity**: UI and ViewModels work only with `model/` classes (`Recipe`, `Ingredient`, `Instruction`). Room classes live in `data/entity/`, and `mapper/` converts between the two. Don't leak entities above the repository layer.
- **DI (Hilt)**: Repositories are bound in `RepositoryApplicationModule` behind the `@LocalRepository` qualifier, so inject them with that qualifier. `DatabaseApplicationModule` provides the Room database. `ApplicationScopeModule` provides an app-wide scope behind the `@ApplicationScope` qualifier for work that must outlive screens; only the debug sample-recipe receiver uses it. ViewModels run writes in `viewModelScope`. `RecipeRepositoryProvider` is a Hilt entry point for places that can't use constructor injection.
- **Reads** are exposed as `Flow`, and ViewModels turn them into a single `StateFlow` screen state with `combine`/`stateIn`. **Writes** are `suspend` functions.
- **Soft delete**: deleting a recipe sets `isDeleted` (`deleteById` / `undeleteById`), which allows undo. Queries must filter out deleted recipes.

### Room schema changes

- The schema is exported to `app/schemas/` by the Room Gradle plugin (`room { schemaDirectory(...) }`), which also exposes it to `androidTest`. Commit the generated JSON whenever the schema changes.
- On every schema change, bump `version` in `CookTimeDatabase`, then add an `AutoMigration` there if Room can infer it. Otherwise add a manual `Migration` to `data/migrations/Migrations.kt` and include it in `ALL_MIGRATIONS`. Never drop user data with destructive migrations.
- Cover every new version with a test in `app/src/androidTest/.../data/MigrationTest.kt` (`MigrationTestHelper`), starting from the oldest version.

### UI shell and navigation

- `MainActivity` → `AppNavGraph`, which owns only the navigation drawer, the `NavHost` and a shared `SnackbarHostState` (with the `CoroutineScope` that shows snackbars, so a message requested right before navigating away is still displayed on the destination).
- Each screen owns its own Material 3 `Scaffold`, declaring its top bar, bottom bar and FAB directly in the composition, with no effect publishing UI state to a parent. Use `AppTitleTopBar` for a title with a back button. When adding a screen, follow this pattern (see `ViewRecipeScreen`).
- The shared snackbar reaches screens as an explicit `AppSnackbarState` parameter; the screen that displays the message renders `SnackbarHost` with `appSnackbarState.snackbarHostState`.
- Effects are reserved for events (showing a snackbar, navigating after saving or deleting, syncing the pager). Never publish UI state from an effect: predictive back composes the outgoing screen during the gesture, and stale shell state shows the previous screen's bars.
- Destinations are `@Serializable` classes in `ui/Destinations.kt` (`Home`, `Settings`, `NewRecipe`, `ViewRecipe`, `ExecuteRecipe`) used as typed Navigation routes (`composable<T>`, `navigate(T)`, `popUpTo<T>`, `toRoute<T>()`, `hasRoute<T>()`); there are no route strings. Screens take intent callbacks with just the data they need (an id and a name, never a `Recipe`), and `ui/components/AppNavHost.kt` is the only place that turns them into `navigate`/`popBackStack`, registering each destination with its generic `screen<T>(...)` helper, which provides the scopes shared element transitions need. ViewModels read their arguments with `SavedStateHandle.toRoute<T>()`; the keys they write themselves (recipe draft, editing mode, checked items) stay as they are.
- Screen transitions live in `ui/transitions/ScreenTransitions.kt`. Linked screens use a container transform: the element the user tapped carries `Modifier.sharedContainer(key)` and the destination's `Scaffold` the same key, and an element that moves from one screen to a different place on the other (the recipe name, from its card to the title) carries `Modifier.sharedElement(key)`. Keys are `SharedElementKey` values built from data known on the first frame (pass it in the route, like `recipeTitle`), or the transition won't match. Add a new linked pair to `containerTransformPairs`, a set of destination `KClass` pairs; every other pair of screens uses Material's fade through, and destinations are identified by type with `hasRoute`, never by comparing `destination.route` strings. Check transitions at a slow animator duration scale and with a cancelled predictive back gesture.
- Screens live in `ui/screens/<feature>/<action>/` alongside their ViewModel and `*ScreenState`, with `appbar/`, `dialogs/`, `tabs/` and `steps/` subfolders as needed. Reusable composables go in `ui/components/`.
- ViewModels are obtained with `hiltViewModel()` from `androidx.hilt.lifecycle.viewmodel.compose`.
- The app is edge-to-edge (`enableEdgeToEdge()` in `MainActivity`), so new layouts must handle window insets themselves. A `Scaffold`'s bars apply the system bar insets; content with text fields applies `imePadding()` after `consumeWindowInsets(paddingValues)` so the focused field stays above the keyboard, and the activity uses `adjustResize` so the system doesn't pan the whole window.

### Localization

The app ships English (`values/strings.xml`) and Portuguese (`values-pt/strings.xml`), limited by `androidResources.localeFilters` and `res/xml/locales_config.xml`. Any user-facing string must be a string resource and exist in both files.

## Git workflow (GitHub flow)

- `main` is always releasable. Never commit directly to `main`.
- Create a short-lived branch from an up-to-date `main` for each change, named `<type>/<short-kebab-description>` with the same types as commits, for example `feat/search-by-ingredient` or `fix/delete-dialog-dismiss`.
- Open a pull request to `main`. PRs are merged with **squash merge**, so the PR title becomes the commit on `main` and must follow the commit format below.
- Delete the branch after merging.
- Only commit, push or open PRs when the user asks.

## Commit messages (Conventional Commits)

Format:

```
<type>(<optional scope>): <summary>

<optional body: what and why, wrapped at 72 columns>

<optional footer: Closes #123, BREAKING CHANGE: ...>
```

- Write in English, with the summary in the imperative mood and lowercase, no trailing period, and about 72 characters at most.
- Types: `feat`, `fix`, `refactor`, `perf`, `test`, `docs`, `build` (Gradle and dependencies), `ci`, `chore`, `style`.
- The scope is the feature area, for example `search`, `edit`, `delete`, `execute`, `star` or `db`.
- Examples from the history: `feat(search): search recipes by name`, `fix: delete confirmation dialog not being dismissed`.
- Keep each commit focused on one logical change.
- Don't commit `.DS_Store`, `local.properties`, `.idea/` or build outputs.
