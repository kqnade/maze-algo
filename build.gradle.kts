plugins {
    id("java")
    id("application")
    id("checkstyle")
}

group = "personal.kqnade"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("personal.kqnade.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "personal.kqnade.Main"
    }
    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

checkstyle {
    toolVersion = "10.12.0"
    // Use Google checks by default if no config file is found, 
    // but we will create a basic one or rely on default behavior.
    // Usually it looks for config/checkstyle/checkstyle.xml
}

// Register a 'lint' task that runs checkstyle
tasks.register("lint") {
    dependsOn("checkstyleMain", "checkstyleTest")
    group = "verification"
    description = "Runs linting checks."
}
