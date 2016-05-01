# Kale
Kotlin IRC message parsing, serialising and notifying. Provides useful abstractions with the intention of splitting message parsing and IRC state management.

[Warren](https://github.com/CarrotCodes/Warren) is the state tracking counterpart. Kale by itself does not track any IRC state.

## Why is this better than other IRC libraries?

Warren and Kale have a few advantages over other IRC libraries:

* The responsibilities of parsing and state management are separated
* Both parsing and state management are verified by hundreds of unit tests
* Messages, and state handlers, are individually encapsulated
 * Dependencies are clear, and there are no enormous, unverifiable disaster zones

## TODO
* [Tags parsing](http://ircv3.net/specs/core/message-tags-3.2.html)
  * 512 bytes for tags, 512 for main message
  * Escape values
* [RFC 1459](https://tools.ietf.org/html/rfc1459)
* [IRC v3](http://ircv3.net/irc/) (including BATCH extension)

## Code License
The source code of this project is licensed under the terms of the ISC license, listed in the [LICENSE](LICENSE.md) file. A concise summary of the ISC license is available at [choosealicense.org](http://choosealicense.com/licenses/isc/).

## Building
This project uses Gradle and IntelliJ IDEA for pretty easy setup and building.

The general idea:
* **Setup**: `./gradlew clean idea`
* **Building**: `./gradlew build`
* **Producing an all-in-one Jar**: `./gradlew build shadowJar`

If you run in to odd Gradle issues, doing `./gradlew clean` usually fixes it.
