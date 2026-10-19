# Calculator — Native Android

A native Android calculator project built with Kotlin, Jetpack Compose and Material 3.

Repository: https://github.com/parvez-4321/Calculator

## Build requirements
- JDK 17
- Android SDK Platform 35
- Gradle 8.13 or compatible
- Android Studio recommended

Open the repository folder in Android Studio and allow Gradle sync. Run unit tests and then build the debug APK. GitHub Actions is configured to run tests, assemble the debug APK, and upload it as the `calculator-debug-apk` workflow artifact.

## Implemented in the current milestone
- Native Kotlin Android application and Material 3 Compose UI
- System light/dark theme and Android 12+ dynamic colors
- Standard/scientific keypad with expression preview
- Safe mathematical expression parser (does not execute arbitrary code)
- Arithmetic, parentheses, operator precedence, implicit multiplication, powers, factorial, percentage postfix and Ans
- Common constants, trigonometric/inverse trigonometric and hyperbolic functions, logarithms, roots, rounding
- Degree/radian mode, copy result and typed error messages
- Room database and DAO for persistent calculation history, search, pinning API, delete and clear
- DataStore preference storage APIs for theme, angle mode and precision
- Interactive tool screen for descriptive statistics, quadratic equations, simple interest, circle geometry, length conversion and number-base conversion
- Pure Kotlin engines for descriptive statistics, matrix arithmetic/determinant/inverse, quadratic equation and bisection solving, finance formulas, length/mass/temperature conversion, programmer bitwise/base operations, geometry, and complex-number arithmetic
- Unit tests for initial parser and mathematical tools
- GitHub Actions CI with debug APK artifact upload

## Known limitations — not yet complete
This is still an actively developed project, not a finished implementation of every PRD requirement. Some calculation engines are implemented but do not yet have dedicated UI. The following remain incomplete: all advanced trigonometric/combinatorics functions; complete statistical distributions, weighted statistics and regression; full equation and simultaneous-system solver UI; matrix and complex-number screens; numerical calculus UI and algorithms beyond bisection; complete unit categories; complete financial amortization tools; date/time calculator; complete geometry catalog; scientific constants reference UI and sourced data; fully customizable settings; persistent variables and memory keys; history search/pin UI; localization; app icon and launch screen; complete accessibility/performance audit; full edge-case test coverage; and verified release APK/AAB signing.

Do not represent a planned or engine-only feature as a completed end-user feature.

## Android build outputs
Typical paths:
- `app/build/outputs/apk/debug/app-debug.apk`
- `app/build/outputs/apk/release/app-release.apk`
- `app/build/outputs/bundle/release/app-release.aab`

A release signing configuration must be set up securely before distributing release artifacts.

## Security and privacy
- Core calculation logic runs locally.
- The expression parser does not evaluate arbitrary source code.
- Do not commit keystores, passwords or private credentials.
- Calculation history is stored locally in the app database.
