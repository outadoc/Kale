# Kale
Kotlin IRC message parsing, serialising and notifying. Provides useful abstractions with the intention of splitting message parsing and IRC state management.

Written for fun and personal projects, and open sourced.

## Project goals
* [Tags parsing](http://ircv3.net/specs/core/message-tags-3.2.html)
  * 512 bytes for tags, 512 for main message
  * Escape values
* [RFC 1459](https://tools.ietf.org/html/rfc1459)
* [IRC v3](http://ircv3.net/irc/) (including BATCH extension)
* Channel abstraction
* User abstraction

## Code License
The source code of this project is licensed under the terms of the ISC license, listed in the [LICENSE](LICENSE.md) file. A concise summary of the ISC license is available at [choosealicense.org](http://choosealicense.com/licenses/isc/).

## Building
This project uses Gradle and IntelliJ IDEA for pretty easy setup and building. There are better guides around the internet for using them, and I don't do anything particularly special.

The general idea:
* **Setup**: `./gradlew clean idea`
* **Building**: `./gradlew build`
* **Producing an all-in-one Jar**: `./gradlew build shadowJar`

If you run in to odd Gradle issues, doing `./gradlew clean` usually fixes it.