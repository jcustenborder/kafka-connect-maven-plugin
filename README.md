# Introduction

This project is used to generate configuration classes for Kafka Connect Plugins. 

# Extended types

The generator allows configuration items to be tagged with extended types which are used when the 
generator is defining the member variable of the config class. 

## Enum

```json
{
  "configKey": "enum.test",
  "type": "STRING",
  "documentation": "Location of the Java keystore to use.",
  "importance": "HIGH",
  "width": "MEDIUM",
  "extendedType": "Enum",
  "enumType": "TestEnum",
  "enumValues": [
    "First",
    "Second",
    "Third"
  ]
}
```

The above configuration generates this config.

```java
private final EnumSourceConnectorConfig.TestEnum sslEnumTest;

/**
 * Location of the Java keystore to use.
 * 
 * @return
 *     Location of the Java keystore to use.
 */
public EnumSourceConnectorConfig.TestEnum sslEnumTest() {
    return this.sslEnumTest;
}

public enum TestEnum {
    Second,
    Third,
    First;
}
```

## Uri

```json
{
  "configKey": "uri.test",
  "type": "STRING",
  "documentation": "Location of the Java keystore to use.",
  "importance": "HIGH",
  "width": "MEDIUM",
  "extendedType": "Uri"
}
```

```json
{
  "configKey": "uris.test",
  "type": "LIST",
  "documentation": "Location of the Java keystore to use.",
  "importance": "HIGH",
  "width": "MEDIUM",
  "extendedType": "Uri"
}
```

```java
    private final URI sslUriTest;
    private final List<URI> sslUrisTest;
    
    /**
     * Location of the Java keystore to use.
     * 
     * @return
     *     Location of the Java keystore to use.
     */
    public URI sslUriTest() {
        return this.sslUriTest;
    }

    /**
     * Location of the Java keystore to use.
     * 
     * @return
     *     Location of the Java keystore to use.
     */
    public List<URI> sslUrisTest() {
        return this.sslUrisTest;
    }    
```

## Url



## HostAndPort

## Charset

## Pattern

## Set

## PasswordBytes

## PasswordCharArray

## PasswordString

## KeyStore

## KeyManagerFactory

## TrustManagerFactory

## SSLContext

## File
