## An example of implementing push notifications in a TWA application

<img src="https://github.com/shakurocom/twa-notifications-example/blob/master/demo.gif" alt="TWA notification example demo" height="500px" width="243px" />

> A [repository](https://github.com/shakurocom/pwa-for-twa-notifications-example) with a sample PWA application used for this example.

### About this example
This example implements notifications by native means, since notifications by means of SW do not always satisfy business requirements, asking for user permission. Because of this, application may look untrustworthy.
### How push works
> Due to security restrictions, the transfer of data from a TWA to a PWA, including a Firebase token, from TWA to PWA is possible only through query parameters.

After installing, and launching the application, a request is made for the Firebase token. After receiving the token, it gets written to the storage, and the application restarts.

When the application is opened, the token value gets added to the address string as a query parameter, namely **_notifyToken**. This also applies to opening an application with a URL different from the homepage (URL from notification, for example).

Next, your web application takes this parameter from a URL and uses it (send it to the backend, for example).

### Used in this example
- [Bubblewrap](https://github.com/GoogleChromeLabs/bubblewrap).
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging).

You can learn more from this [article](https://shakuro.com/blog).