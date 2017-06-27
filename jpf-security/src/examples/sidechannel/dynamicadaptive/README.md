# Networkable side-channel app profiling

## SideChannelApps
---
The `SideChannelApps` class contains the "apps" under analysis, which may have side-channel vulnerabilities. Each app is implemented as a static method that takes two int arrays and returns a string, e.g.:
```
public static String password(int[] H, int[] L) {
    ...
}
```

## SideChannelAppsDriver
---
The `SideChannelAppsDriver` class is a wrapper for SPF-based symbolic execution of the apps in the `SideChannelApps` class.

The driver takes 3 parameters through the `target.args` property: app ID, length of the high array and length of the low array.

Example invocation:
```
jpf SideChannelAppsDriver.jpf +target.args=4,4,password
```

## Profiling server
---
The `ProfilingServer` class listens for TCP connections, runs the appropriate app from the `SideChannelApps` class with the arguments provided by the client, and returns a response to the client.

The server is now automatically packed by `ant` into a .jar, which is saved to `jpf-security/build/ProfilingServer.jar`. This standalone server can be easily copied to a different machine (e.g., a NUC). To run the server, use:
```
java -jar ProfilingServer.jar
```


## Profiling client
---
`profiler.py` is a Python script that connects to the `ProfilingServer`, repeatedly interacts with it while monitoring the exchanges, and reports responses and observables.

Some example commands:
```
python profiler.py --app add --samples 20  1 2  5 2 3  1 2  5 3 3
python profiler.py --app add --file example.add.cands
python profiler.py --app password --file example.password.cands
```
See `python profiler.py --help` for more options.

