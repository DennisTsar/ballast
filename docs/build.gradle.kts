import java.util.*

plugins {
    id("copper-leaf-base")
    id("copper-leaf-docs")
}

orchid {
    diagnose = true
}

val orchidServe by tasks
val orchidBuild by tasks
val orchidDeploy by tasks
val processOrchidResources by tasks

val exampleProjects = listOf(
    "web",
    "counter",
    "navigationWithEnumRoutes",
)

exampleProjects.forEach { exampleProjectName ->
    tasks.register(
        "copyExample${exampleProjectName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}Sources",
        Copy::class
    ) {
        val sourceDir = project.rootDir.resolve("examples/$exampleProjectName/build/dist/js/developmentExecutable")
        val destinationDir = project.projectDir.resolve("src/orchid/resources/assets/examples/$exampleProjectName")

        onlyIf { sourceDir.exists() }

        from(sourceDir)
        into(destinationDir)
    }
}

val copyExampleComposeWebSources by tasks.registering {
    exampleProjects.forEach { exampleProjectName ->
        dependsOn("copyExample${exampleProjectName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}Sources")
    }
}
orchidServe.dependsOn(copyExampleComposeWebSources)
orchidBuild.dependsOn(copyExampleComposeWebSources)
orchidDeploy.dependsOn(copyExampleComposeWebSources)
processOrchidResources.mustRunAfter(copyExampleComposeWebSources)
