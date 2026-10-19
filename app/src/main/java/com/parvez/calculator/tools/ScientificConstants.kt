package com.parvez.calculator.tools

data class ScientificConstant(
    val name: String,
    val symbol: String,
    val value: Double,
    val unit: String,
    val description: String,
    val exact: Boolean,
    val source: String
)

object ScientificConstants {
    // SI-defining constants are exact by definition. Measured values are labeled as approximate.
    val all: List<ScientificConstant> = listOf(
        ScientificConstant("Pi", "π", Math.PI, "dimensionless", "Ratio of circumference to diameter", false, "Mathematical constant"),
        ScientificConstant("Euler's number", "e", Math.E, "dimensionless", "Base of natural logarithms", false, "Mathematical constant"),
        ScientificConstant("Speed of light in vacuum", "c", 299792458.0, "m/s", "Speed of light in vacuum", true, "SI defining constant"),
        ScientificConstant("Planck constant", "h", 6.62607015e-34, "J·s", "Quantum of action", true, "SI defining constant"),
        ScientificConstant("Elementary charge", "e", 1.602176634e-19, "C", "Magnitude of elementary electric charge", true, "SI defining constant"),
        ScientificConstant("Boltzmann constant", "k", 1.380649e-23, "J/K", "Relates temperature and energy", true, "SI defining constant"),
        ScientificConstant("Avogadro constant", "Nₐ", 6.02214076e23, "mol⁻¹", "Number of entities per mole", true, "SI defining constant"),
        ScientificConstant("Standard gravity", "g₀", 9.80665, "m/s²", "Conventional standard acceleration", true, "Conventional standard value"),
        ScientificConstant("Gravitational constant", "G", 6.67430e-11, "m³·kg⁻¹·s⁻²", "Newtonian gravitational constant", false, "CODATA 2022 recommended value"),
        ScientificConstant("Electron mass", "mₑ", 9.1093837139e-31, "kg", "Rest mass of the electron", false, "CODATA 2022 recommended value"),
        ScientificConstant("Proton mass", "mₚ", 1.67262192595e-27, "kg", "Rest mass of the proton", false, "CODATA 2022 recommended value"),
        ScientificConstant("Vacuum permeability", "μ₀", 1.25663706127e-6, "N/A²", "Magnetic permeability of vacuum", false, "CODATA 2022 recommended value")
    )

    fun search(query: String): List<ScientificConstant> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return all
        return all.filter {
            it.name.lowercase().contains(q) ||
                it.symbol.lowercase().contains(q) ||
                it.unit.lowercase().contains(q) ||
                it.description.lowercase().contains(q)
        }
    }
}
