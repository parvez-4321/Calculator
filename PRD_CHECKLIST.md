# PRD implementation checklist

## Platform and UI
- [x] Native Kotlin Android
- [x] Jetpack Compose + Material 3
- [x] System light/dark theme and Android 12+ dynamic colors
- [x] Standard/scientific keypad
- [ ] Complete tablet/foldable/landscape responsive layouts
- [ ] Full TalkBack and accessibility audit
- [ ] Persisted customization settings

## Calculation engine
- [x] Arithmetic, precedence, parentheses, implicit multiplication
- [x] Powers, factorial, common constants
- [x] Common trigonometric, inverse, hyperbolic, logarithmic and rounding functions
- [x] Degree/radian switch and initial error handling
- [ ] Complete reciprocal trig/inverse hyperbolic functions, GCD/LCM, nCr/nPr, gamma
- [ ] Arbitrary precision and complex arithmetic
- [ ] Full locale-aware input and number formatting
- [ ] Exhaustive edge-case and reference tests

## Persistence and advanced modules
- [ ] Room-backed persistent history, search, pinning, notes, deletion/undo
- [ ] MC/MR/M+/M-/MS memory and persistent variables/formulas
- [ ] Statistics, probability distributions, regression/correlation
- [ ] Equation solver and matrix operations
- [ ] Complex calculator
- [ ] Numerical differentiation, integration and root finding
- [ ] Programmer/base converter
- [ ] Full unit converter
- [ ] Financial, date/time, geometry tools
- [ ] Scientific constants reference and CSV import

## Production
- [x] Initial unit tests
- [ ] Full unit/property/UI/integration tests
- [ ] Gradle wrapper checked in
- [ ] App icon and launch screen
- [ ] CI test and build passing
- [ ] Release signing configured securely
- [ ] APK/AAB generated, installed, and verified
