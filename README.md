<br />
<p align="center">
  <a href="">
    <img src="./fcl-android.svg" alt="Logo" width="400" height="auto">
  </a>

  <p align="center">
    <i>Android Kotlin library for the Flow Client Library (FCL).</i>
    <br />
    <br />
    <a href="https://github.com/onflow/fcl-android/issues">Report a Bug</a>
</p>
<br />
<br />

FCL Android is a Kotlin library for the [Flow Client Library (FCL)](https://docs.onflow.org/fcl/)
that enables Flow wallet authentication on Android devices.

## Demo

The [example app](/example) in this project demonstrates how to use FCL inside an Android app.

<img src="/example/fcl-android-demo-dapper.gif" width="200" />

## Installation

You can download the latest version from the [GitHub Apache Maven registry](https://github.com/onflow/fcl-android/packages).

```xml
<dependency>
  <groupId>org.onflow.fcl</groupId>
  <artifactId>fcl-android</artifactId>
  <version>0.1.0</version>
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

fcl.providers.add(DefaultProvider.DAPPER)
fcl.providers.add(DefaultProvider.BLOCTO)
```

## Authenticate 

```kotlin
// use inside of an activity class
val context: android.content.Context = this

fcl.authenticate(context, DefaultProvider.DAPPER) { response ->
  System.out.println(response.address)
}
```

The `response` variable is of type `AuthnResponse`, which contains the user's wallet address:

```kotlin
/**
 * Authentication response
 * 
 * @property [address] address of the authenticated account
 * @property [status] status of the authentication (approved or declined)
 * @property [reason] if authentication is declined this property will contain more description
 */
data class AuthnResponse(
    val address: String?,
    val status: ResponseStatus,
    val reason: String?
)
```
