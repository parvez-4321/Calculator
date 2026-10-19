# PRD implementation checklist

Legend: [x] implementation exists in source; [ ] incomplete, not fully integrated, or not verified.

## Platform and UI
- [x] Native Kotlin Android
- [x] Jetpack Compose + Material 3
- [x] System light/dark theme and Android 12+ dynamic colors
- [x] Standard/scientific keypad
- [x] Math Tools screen with statistics, quadratic equation, simple interest, circle, length conversion, number base conversion, 2×2 matrix, complex arithmetic, date/time and constants reference
- [ ] Complete tablet/foldable/landscape layouts
- [ ] Full TalkBack/accessibility audit
- [ ] Full settings and persisted customization UI
- [ ] App icon and launch screen

## Calculation engine
- [x] Arithmetic, parentheses, precedence and implicit multiplication
- [x] Powers, factorial, postfix percentage and common constants
- [x] Common trigonometric, inverse, hyperbolic, logarithmic and rounding functions
- [x] Degree/radian switch
- [x] Typed error handling for initial cases
- [ ] Full inverse hyperbolic and reciprocal trigonometric functions
- [ ] GCD/LCM, nCr/nPr, gamma, prime utilities
- [ ] Arbitrary precision and complete complex math functions
- [ ] Locale-aware input and formatting
- [ ] Full percentage addition/subtraction workflow semantics
- [ ] Complete numerical edge-case and reference test suite

## Persistence
- [x] Room entity, DAO and database for calculation history
- [x] Room history save/read/clear integration in calculator screen
- [x] DataStore preferences data access class
- [ ] History search, pinning, notes, delete/undo UI
- [ ] Persisted MC/MR/M+/M-/MS memory
- [ ] Persistent variables, saved formulas and user-defined functions

## Advanced tools
- [x] Descriptive statistics core
- [x] Matrix add/multiply/transpose/determinant/inverse/trace core
- [x] Linear and quadratic equation helpers
- [x] Bisection root solver
- [x] Basic simple/compound interest and loan-payment formulas
- [x] Probability distribution functions and linear regression core
- [x] Date/time calculations
- [x] Searchable scientific constants data
- [x] Length/mass/temperature conversion core
- [x] Programmer base conversion and bitwise core
- [x] Basic geometry calculation core
- [x] Complex arithmetic, magnitude, argument, exp and principal log
- [ ] Full statistics distributions, weighted statistics, regression and CSV import
- [x] Basic quadratic equation UI
- [ ] Systems of equations and general polynomial solver
- [x] Basic 2×2 matrix UI (determinant, transpose and inverse)
- [ ] General matrix dimensions, decomposition and eigenvalue operations
- [x] Basic complex arithmetic UI
- [ ] Complete complex function support
- [ ] Numerical differentiation/integration and optimization
- [ ] Full programmer UI and bit-width/signedness controls
- [ ] Complete unit categories and conversion definitions
- [ ] Complete financial, amortization, tax and margin tools
- [x] Basic date/time calculator UI
- [ ] Date/time pickers and timezone-aware advanced operations
- [ ] Full geometry tools
- [ ] Scientific constants library with verified sources and UI

## Production and verification
- [x] Initial unit tests
- [x] GitHub Actions test/build workflow configured
- [x] Debug APK artifact upload configured
- [ ] Confirm CI workflow passes
- [ ] Gradle wrapper committed and verified
- [ ] Full unit/property/UI/integration tests
- [ ] Release signing and verification
- [ ] Release APK/AAB built, installed and verified
- [ ] Dependency/security review and final privacy disclosure
