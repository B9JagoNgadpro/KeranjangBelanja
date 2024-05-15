plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.sonarqube") version "3.5.0.2730"
}

group = "jagongadpro"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}

tasks.register<Test>("unitTest"){
	description = "Runs unit tests."
	group = "verification"

	filter{
		excludeTestsMatching("*FunctionalTest")
	}
}

tasks.register<Test>("functionalTest"){
	description = "Runs functional tests."
	group = "verification"

	filter{
		includeTestsMatching("*FunctionalTest")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) 
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required = true
		html.required = true
	}
}

sonar {
    properties {
        property("sonar.projectKey", "B9JagoNgadpro_KeranjangBelanja")
        property("sonar.organization", "b9jagongadpro")
        property("sonar.java.binaries", ".")
        property("sonar.gradle.skipCompile", "true")
    }
}
