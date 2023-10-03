# beanstalkt

![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-blue.svg?logo=kotlin)
![GitHub](https://img.shields.io/github/license/virusbear/beanstalkt)
<!---![Maven Central](https://img.shields.io/maven-central/v/com.virusbear.beanstalkt/beantalkt)-->
<!---TODO: add badges for code coverage, build state, test results, maven link, kdoc-->

A simple async kotlin client for [beanstalkd](https://github.com/beanstalkd/beanstalkd) work queue.

## Description

This client implements all operations of beanstalkd
as described in the [protocol documentation](https://raw.githubusercontent.com/beanstalkd/beanstalkd/master/doc/protocol.txt).

All operations are implemented to be suspending functions, suspending the caller until a response is received by beanstalkd.

## Usage

```kotlin
suspend fun main() = coroutineScope {
    val client = DefaultClient()

    while(true) {
        val job = client.reserve()
        println(job.id)
    }
}
```

### Known Issues

Due to the blocking nature of the beanstalk protocol, it is currently not possible to have a client reserve a job, whilst performing other operations asynchronously.
All operations are added to an internal queue and processed sequentially. To avoid blocking issues with the reserve operation, use the `reserveWithTimeout()` operation to execute other operations after a suitable timeout.

```kotlin
suspend fun main() = coroutineScope {
    val client = DefaultClient()

    val producerJob = launch {
        while(true) {
            client.put("Hello World!".toByteArray())
        }
    }
    
    val consumerJob = launch {
        while(true) {
            try {
                val job = client.reserveWithTimeout(10.0.seconds)
                println(job.id)
            } catch(ex: TimedOutException) {
                //Ignored
            }
        }
    }
    
    producerJob.join()
    consumerJob.join()
}
```

## Features

- [X] beanstalkd protocol completely implemented
- [ ] Connection Pooling
- [ ] suspending iterator similar to kotlin `Channel` implementation
- [ ] Kotlin Multiplatform

## Using in your projects

### Maven

Add dependencies (you can also add other modules that you need):

```xml
<dependency>
    <groupId>com.virusbear.beanstalkt</groupId>
    <artifactId>beanstalkt</artifactId>
    <version>1.0.0</version>
</dependency>
```

And make sure that you use the latest Kotlin version:

```xml
<properties>
    <kotlin.version>1.9.0</kotlin.version>
</properties>
```

### Gradle

Add dependencies (you can also add other modules that you need):

```kotlin
dependencies {
    implementation("com.virusbear.beanstalkt:beanstalkt:1.0.0")
}
```

And make sure that you use the latest Kotlin version:

```kotlin
plugins {
    // For build.gradle.kts (Kotlin DSL)
    kotlin("jvm") version "1.9.0"
    
    // For build.gradle (Groovy DSL)
    id "org.jetbrains.kotlin.jvm" version "1.9.0"
}
```

Make sure that you have `mavenCentral()` in the list of repositories:

```kotlin
repositories {
    mavenCentral()
}
```

## State of Dev & Contributing

This project is only a side project of mine, which originated from another project. 
I will try to fix any bugs I encounter during my own use cases,
but will most likely be unable to work on parts that I am not personally using.

If you encounter any bugs or have ideas on how to improve this project, please file an issue or create a pull request. 

Any help is appreciated.

## Building

To build this project run the gradle `build` task.


### CLI

```shell
./gradlew build
```
