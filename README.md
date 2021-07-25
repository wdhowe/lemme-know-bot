# lemme-know-bot

[![Build Status][gh-actions-badge]][gh-actions] [![Clojars Project][clojars-badge]][clojars] [![Clojure Docs][cljdoc-badge]][cljdoc-link] [![Clojure version][clojure-v]](project.clj)

A Telegram Bot that mentions you when your specified text in a chat matches.

## Using the Code

Using the lemme-know-bot code in your own project.

To build the service JAR or use the Docker container instead, go to the next section.

Leiningen/Boot Project file

```clojure
[lemme-know-bot "0.3.1"]
```

### Include the Library

In the REPL

```clojure
(require '[lemme-know-bot.core :as lemme])
```

In your application

```clojure
(ns my-app.core
  (:require [lemme-know-bot.core :as lemme]))
```

## Configuration

Supported environment variables and whether they are required to be provided.

| Variable     | Default                            | Description                                            | Required? |
| ------------ | ---------------------------------- | ------------------------------------------------------ | --------- |
| BOT_TOKEN    | none                               | Token for bot to authenticate to the Telegram servers. | Yes       |
| LKB_SEARCHES | "/tmp/lemme-know-bot-searches.edn" | File for saving search state to.                       | No        |
| LKB_SLEEP    | 10000                              | Sleep time in ms between long polls.                   | No        |
| LKB_TIMEOUT  | 10                                 | Timeout in seconds to wait while long polling.         | No        |

## Usage

Building the Java Jar and running it or the Docker container.

### Pre-Reqs

Before building the JAR or running the docker container:

- Create a Telegram bot using the botfather.
  - Configure the bot to give it access to group chat messages. (group privacy disabled)

Proceed with either the [Docker](#run-the-app---docker) or [Jar](#run-the-app---java-jar) instructions.

### Run the App - Docker

- Setup the volume for saving the search state

```bash
# Docker volume name (docker volume ls)
DOCKER_VOL=lemme-know-bot

docker volume create ${DOCKER_VOL}

# Container path for the search state (matches the Dockerfile)
CONTAINER_SAVE_DIR=/usr/src/app/state
```

- Run the container

```bash
docker run \
--detach \
--name lemme-know-bot \
--env BOT_TOKEN="MY-TOKEN-HERE" \
--env LKB_SEARCHES=${CONTAINER_SAVE_DIR}/lemme-know-bot-searches.edn \
--volume ${DOCKER_VOL}:${CONTAINER_SAVE_DIR} \
wdhowe/lemme-know-bot
```

### Pre-Reqs - Java Jar

Pre-reqs for building the Java Jar.

- Install leiningen.
- Clone this project.
- Build the jar

```bash
lein uberjar
```

### Run the App - Java Jar

- Make your bot token available in the environment
  - Other env vars should be exported at this step if you would like to change any default settings.
  - See [Configuration](#configuration) for details.

```bash
export BOT_TOKEN="MY-TOKEN-HERE"
```

- Start the service

```bash
java -jar lemme-know-bot-VERSION-standalone.jar
```

### Telegram Group Setup

Once the service is running from one of the above methods:

- Add your bot to a Telegram group chat.
- Start chatting with your bot in the Telegram group chat.
  - Send '/help' to get started.

## License

Copyright © 2020 Bill Howe

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
<http://www.eclipse.org/legal/epl-2.0>.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at <https://www.gnu.org/software/classpath/license.html>.

----

<!-- Named page links below: /-->

[gh-actions-badge]: https://github.com/wdhowe/lemme-know-bot/workflows/ci%2Fcd/badge.svg
[gh-actions]: https://github.com/wdhowe/lemme-know-bot/actions
[cljdoc-badge]: https://cljdoc.org/badge/lemme-know-bot/lemme-know-bot
[cljdoc-link]: https://cljdoc.org/d/lemme-know-bot/lemme-know-bot/CURRENT
[clojure-v]: https://img.shields.io/badge/clojure-1.10.3-blue.svg
[clojars]: https://clojars.org/lemme-know-bot
[clojars-badge]: https://img.shields.io/clojars/v/lemme-know-bot.svg
