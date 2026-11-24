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
```

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
- **DI (Hilt)**: Repositories are bound in `RepositoryApplicationModule` behind the `@LocalRepository` qualifier, so inject them with that qualifier. `DatabaseApplicationModule` provides the Room database and an app-wide IO `CoroutineScope`. `RecipeRepositoryProvider` is a Hilt entry point for places that can't use constructor injection.
- **Reads** are exposed as `Flow`, and ViewModels turn them into a single `StateFlow` screen state with `combine`/`stateIn`. **Writes** are `suspend` functions.
- **Soft delete**: deleting a recipe sets `isDeleted` (`deleteById` / `undeleteById`), which allows undo. Queries must filter out deleted recipes.

### Room schema changes

- The schema is exported to `app/schemas/` (configured through the `room.schemaLocation` KSP arg). Commit the generated JSON whenever the schema changes.
- On every schema change, bump `version` in `CookTimeDatabase`, then add an `AutoMigration` there if Room can infer it. Otherwise add a manual `Migration` in `DatabaseApplicationModule.provideAppDatabase` (see `migration2to3`). Never drop user data with destructive migrations.

### UI shell and navigation

- `MainActivity` → `AppNavGraph`, a single `Scaffold` that owns the drawer, top bar, bottom bar, FAB and snackbar for every screen.
- Screens don't own their own `Scaffold`. Each one reports what the shell should show by calling `onComposing(AppNavGraphState(...))` from a `LaunchedEffect`. That state covers the top bar type (title / search bar / none), title, actions, the bottom bar and the FAB. When adding a screen, follow this pattern (see `ListRecipesScreen`).
- Routes are string constants in `AllDestinations`. Navigation goes through `AppNavigationActions`, and the destinations are registered in `ui/components/AppNavHost.kt`.
- Screens live in `ui/screens/<feature>/<action>/` alongside their ViewModel and `*ScreenState`, with `appbar/`, `dialogs/`, `tabs/` and `steps/` subfolders as needed. Reusable composables go in `ui/components/`.
- ViewModels are obtained with `hiltViewModel()` from `androidx.hilt.lifecycle.viewmodel.compose`.
- The app is edge-to-edge (`enableEdgeToEdge()` in `MainActivity`), so new layouts must handle window insets themselves.

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
