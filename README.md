# Kale

Kotlin/Multiplatform IRC message parsing, serialising and notifying.
Provides useful abstractions with the intention of splitting message parsing and IRC state
management.
Useful for building bots, clients and servers.

## Fork

This project is a fork of [WillowChat/Kale](https://github.com/WillowChat/Kale), with some changes.
Mostly:

- updated dependencies and JDK to keep artifacts buildable and available;
- the annotation processor and main Kale class has been stripped out;
- the project has been converted to Kotlin/Multiplatform, with support for JVM and iOS.

## Goals

* Own the parsing & serialising bit of IRC clients and servers
* Let users feed raw lines in, and be notified with strongly typed output messages, covering RFC1459
  and IRCv3
* Verify the above with an extensive suite of unit tests

## Code License

The source code of this project is licensed under the terms of the ISC license, listed in
the [LICENSE](LICENSE.md) file. A concise summary of the ISC license is available
at [choosealicense.org](http://choosealicense.com/licenses/isc/).

## Building

This project uses Gradle for pretty easy setup and building.

The general idea:

* **Setup**: `./gradlew clean`
* **Building**: `./gradlew build`
* **Testing**: `./gradlew test`

If you run in to odd Gradle issues, doing `./gradlew clean` usually fixes it.
