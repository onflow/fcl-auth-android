# FCL Android

FCL Android is a Kotlin library for the [Flow Client Library (FCL)](https://docs.onflow.org/fcl/)
that enables Flow wallet authentication on iOS devices.

##

The [demo app](/app) in this project shows how to use FCL inside an Android app.

<img src="/app/fcl-android-demo-dapper.gif" width="200" />

![demo-dapper](/app/fcl-android-demo-dapper.gif | width=100 )

## Installation

You can download the latest version from the [GitHub Apache Maven registry](https://github.com/onflow/fcl-android/packages).

```xml
<dependency>
  <groupId>org.onflow.fcl</groupId>
  <artifactId>fcl-android</artifactId>
  <version>0.0.1</version>
</dependency>
```

- [How to install with Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package)
- [How to install with Gradle](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package)

## Configuration

You will need to configure your app information before using the authentication library.

FCL Android ships with several built-in wallet providers (Dapper, Blocto),
but you can also define custom wallet providers if needed.

```kotlin
import org.onflow.fcl.android.AppInfo
import org.onflow.fcl.android.DefaultProvider
import org.onflow.fcl.android.FCL

val fcl = FCL(
    AppInfo(
        title = "FCL Demo App",
        icon = URL("https://foo.com/bar.png"),
    )
)

fcl.addProvider("dapper", DefaultProvider.DAPPER)
fcl.addProvider("blocto", DefaultProvider.BLOCTO)
```

## Authenticate 

```kotlin
// use inside of an activity class
val context: android.content.Context = this

fcl.authenticate(context, "dapper") { response ->
  System.out.println(response.address)
}
```

The `response` variable is of type `AuthnResponse`, which contains the user's wallet address:

```kotlin
data class AuthnResponse(val address: String)
```
