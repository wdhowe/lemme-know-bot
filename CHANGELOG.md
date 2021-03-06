# Change Log

All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

- Protect get-updates from exiting the bot with any exceptions, log the error instead.
- Send messages on a futures thread to continue processing.

## [0.3.1] - 2020-11-29

### 0.3.1 Changed

- Dependency bump telegrambot-lib 0.3.0 to 0.3.1.

## [0.3.0] - 2020-11-27

### 0.3.0 Changed

- Search lists are now chat+username specific (no longer shared across chats) ([issue-2](https://github.com/wdhowe/lemme-know-bot/issues/2))

## [0.2.1] - 2020-11-26

### 0.2.1 Changed

- Bumped openjdk version in Dockerfile from 8 to 11.

## [0.2.0] - 2020-11-25

### 0.2.0 Added

- Dockerfile for building the service in a container.
- Persistent searches ([issue-1](https://github.com/wdhowe/lemme-know-bot/issues/1))
- Catch HTTP exceptions ([issue-4](https://github.com/wdhowe/lemme-know-bot/issues/4))
- Clean duplicate searches ([issue-5](https://github.com/wdhowe/lemme-know-bot/issues/5))

## 0.1.0 - 2020-11-23

- Initial project release.

---

[Unreleased]: https://github.com/wdhowe/lemme-know-bot/compare/0.3.1...HEAD
[0.3.1]: https://github.com/wdhowe/lemme-know-bot/compare/0.3.0...0.3.1
[0.3.0]: https://github.com/wdhowe/lemme-know-bot/compare/0.2.1...0.3.0
[0.2.1]: https://github.com/wdhowe/lemme-know-bot/compare/0.2.0...0.2.1
[0.2.0]: https://github.com/wdhowe/lemme-know-bot/compare/0.1.0...0.2.0

[comment]: # (Types of changes)
[comment]: # ('Added' for new features.)
[comment]: # ('Changed' for changes in existing functionality.)
[comment]: # ('Deprecated' for soon-to-be removed features.)
[comment]: # ('Removed' for now removed features.)
[comment]: # ('Fixed' for any bug fixes.)
[comment]: # ('Security' in case of vulnerabilities.)
