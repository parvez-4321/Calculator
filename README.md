# Calculator — Native Android

A native Android scientific calculator project using Kotlin, Jetpack Compose, and Material 3.

## Build
- JDK 17
- Android SDK Platform 35
- Open the project in Android Studio and sync Gradle.
- Run unit tests and assemble the debug APK.

## Current milestone
This commit establishes the native Android project, Material 3 interface, a safe expression parser, scientific keypad, initial unit tests, and CI workflow. This is the first development milestone, not the finished PRD implementation.

Implemented: basic arithmetic, expression precedence, parentheses, implicit multiplication, powers, factorial, constants, common trig/inverse trig/hyperbolic functions, logarithms, roots, rounding, degree/radian mode, live preview, copy result, and session-only history.

Still planned: persistent Room history and memory, variables/formulas, complete trig and combinatorics, statistics, distributions, equation and matrix solvers, complex mode, numerical calculus, programmer mode, full unit converter, finance, date/time, geometry, constants reference, full accessibility audit, and release signing verification.

Do not advertise unimplemented features as available. See PRD_CHECKLIST.md.
