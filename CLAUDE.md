# SwitchBotAPI-java Project

## Project Overview

A Java wrapper library for the SwitchBot API v1.1, providing easy integration with SwitchBot devices.

## Key Information

- **Language**: Java 17
- **Build Tool**: Maven
- **Version**: 1.2.5
- **Main Package**: `com.bigboxer23.switch_bot`

## Project Structure

```
src/
├── main/java/com/bigboxer23/switch_bot/
│   ├── SwitchBotApi.java                    # Main API client
│   ├── SwitchBotDeviceApi.java             # Device-specific API operations
│   ├── IDeviceTypes.java                   # Device type constants
│   ├── IDeviceCommands.java                # Device command constants
│   └── data/                               # Data models and response classes
└── test/java/com/bigboxer23/switch_bot/    # Unit and integration tests
```

## Build Commands

- **Test**: `mvn test`
- **Integration Tests**: `mvn failsafe:integration-test -Dintegration=true`
- **Build**: `mvn compile`
- **Package**: `mvn package`
- **Format Code**: `mvn spotless:apply`
- **Coverage Report**: `mvn jacoco:report`

## Code Style

- Uses Spotless for code formatting with Google Java Format (AOSP style)
- Tab indentation (4 spaces per tab)
- Lombok for reducing boilerplate code

## Testing

- JUnit 5 for unit tests
- Mockito for mocking
- Integration tests with SwitchBot API
- Code coverage with JaCoCo

## Dependencies

- **utils**: Custom utility library (bigboxer23)
- **moshi**: JSON serialization
- **lombok**: Code generation
- **logback**: Logging (test scope)

## GitHub Actions

- Unit tests on push/PR
- CodeQL security analysis
- Code coverage reporting
- Automatic package publishing
- Auto-merge for dependency updates

## API Features

- Device listing and status retrieval
- Device control commands
- Battery status monitoring
- Support for curtains, plugs, and other SwitchBot devices
- HMAC-SHA256 authentication with SwitchBot API

## Example Usage

```java
SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
List<Device> devices = instance.getDeviceApi().getDevices();
Device status = instance.getDeviceApi().getDeviceStatus(devices.getFirst().getDeviceId());
```

