// Keep layer boundaries enforceable while the application uses a single Gradle module.
val verifyArchitecture = tasks.register("verifyArchitecture") {
    group = "verification"
    description = "Checks package layout and presentation/domain/data dependency boundaries."

    val sources = fileTree("src/main/java") { include("**/*.kt") }
    inputs.files(sources)

    doLast {
        val basePackage = "com.project.momentum"
        val declaration = Regex("(?m)^package\\s+([\\w.]+)")
        val dependency = Regex("(?m)^import\\s+([\\w.]+)")
        val violations = mutableListOf<String>()

        sources.sortedBy { it.path }.forEach { source ->
            val text = source.readText()
            val packageName = declaration.find(text)?.groupValues?.get(1)
            val relativePath = source.relativeTo(file("src/main/java")).invariantSeparatorsPath
            if (packageName == null) {
                violations += "$relativePath: missing package declaration"
                return@forEach
            }
            if (relativePath.substringBeforeLast('/') != packageName.replace('.', '/')) {
                violations += "$relativePath: directory must match package $packageName"
            }

            val layer = packageName.removePrefix("$basePackage.").substringBefore('.')
            if (layer !in setOf("presentation", "domain", "data", "di") &&
                !(packageName == basePackage && source.name == "MomentumApp.kt")
            ) {
                violations += "$relativePath: code must belong to presentation, domain, data or di"
            }

            val forbidden = when (layer) {
                "domain" -> listOf(
                    "$basePackage.presentation", "$basePackage.data", "$basePackage.di",
                    "$basePackage.R", "$basePackage.BuildConfig",
                    "android", "androidx", "io.ktor", "okhttp3", "retrofit2",
                    "kotlinx.serialization", "dagger", "com.google", "com.vk",
                )
                "data" -> listOf("$basePackage.presentation", "androidx.compose")
                "presentation" -> listOf(
                    "$basePackage.data", "io.ktor", "okhttp3", "retrofit2",
                    "androidx.datastore", "com.google.firebase",
                )
                else -> emptyList()
            }
            dependency.findAll(text).forEach { match ->
                val imported = match.groupValues[1]
                if (forbidden.any { imported == it || imported.startsWith("$it.") }) {
                    violations += "$relativePath: $layer must not depend on $imported"
                }
            }
            // Also catch fully qualified references that bypass imports.
            forbidden.forEach { prefix ->
                val qualified = Regex("(?<![\\w.])${Regex.escape(prefix)}(?:\\.|\\b)")
                val body = text.lineSequence()
                    .filterNot { it.trimStart().startsWith("import ") || it.trimStart().startsWith("//") }
                    .joinToString("\n")
                if (qualified.containsMatchIn(body)) {
                    violations += "$relativePath: forbidden qualified reference to $prefix"
                }
            }
        }
        check(violations.isEmpty()) {
            "Architecture violations:\n" + violations.joinToString("\n")
        }
        logger.lifecycle("Architecture verified for ${sources.files.size} Kotlin files")
    }
}

tasks.matching { it.name == "preBuild" || it.name == "check" }.configureEach {
    dependsOn(verifyArchitecture)
}
