*This library is for Minecraft Fabric 1.20.2. Ports for other versions will come in time. If you wish to port it yourself, pull requests are welcome!*

# VideoLib

Ever since its inception, Minecraft's engine has been... interesting, to say the least. It seems to lack many standard features such as GPU mesh caching and, in this case, simple video playback. This mod aims to remedy this, providing a clean API for mod-makers to use pre-rendered video playback in their mods.

*Note: this mod provides no functionality on its own. It is simply a library for other mods to make use of.*

# Setup

An installation guide will be provided once the maven dependencies are setup. It's worth noting that the default implementation requires [VLC Media Player](https://www.videolan.org/) to be installed on the user's system.

# Usage

To use VideoLib, you must get a reference to a `VideoManager`. This video manager is the basis for all interactions with the library, and is vital to the functionality of the mod:

```java
VideoManager videoManager = VideoLib.getInstance().getVideoManager();
```

The `VideoManager` can be used to create multiple `VideoPlayer`s. Due to the performance overhead of video playback, each video player is registered with the video manager using a unique ID. Only the video manager can create new `VideoPlayer` instances.

```java
 VideoPlayer videoPlayer = videoManager.getOrCreate(new Identifier("mymod", "my_video_player"));
```

Each `VideoPlayer` has a `getTexture()` function, which returns a Minecraft texture identifier that can be used in an entity renderer (or anywhere else that non-atlas textures are used). This texture is automatically updated for every frame of the video, and it can be applied like any other texture.

```java
Identifier texture = videoPlayer.getTexture();
VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture));

// render geometry however you want
```

## Video Handles

There are a number of ways to tell a video player to begin playing, but most of them revolve around the `VideoHandle`. A video handle is essentially a pointer to a video file stored in an external location, whether it be on the local disk or somewhere on the internet. To create a `VideoHandle`, you need a `VideoHandleFactory`. This can be obtained from the `VideoManager`.

It's important to note that not all video handles will work with all video managers. Make sure you're only using video handles provided by the video manager's factory. (Most API functions already do this).

```java
VideoHandleFactory factory = videoManager.getVideoHandleFactory();
```

The `VideoHandleFactory` provides two primary functions: `getVideoHandle(Identifier)` and `getVideoHandle(URL)`.

- `getVideoHandle(Identifier)` expects the resource identifier of a video file from any mod. This file must be in the `videos` folder of your resourcepack. (ex: `mymod:videos/myvideo.mp4`)

- `getVideoHandle(URL)` expects the URL where a video can be found. This may be over the internet or on the local filesystem. (ex: `http://mywebsite.com/myvideo.mp4`). This file must be directly accessible via a `GET` request. Google Drive links and other services where the user must navigate a download page are not allowed.

The video handle can then be passed to the video player for playback.

```java
VideoHandle idHandle = factory.getVideoHandle(new Identifier("mymod", "videos/myvideo.mp4"))
VideoHandle urlHandle = factory.getVideoHandle(new URL("http://mywebsite.com/myvideo.mp4"));


videoPlayer.getMediaInterface().play(idHandle);
```

*Media players also provide overloads of `play()` that take identifiers and urls directly. These are simply shortcuts for creating video handles and playing them.*

## Playback Control

Video players provide a set of interfaces allowing for finer control of media playback: `MediaInterface`, `ControlsInterface`, and `CodecInterface`. These can be accessed as follows:

```java
MediaInterface mediaInterface = videoPlayer.getMediaInterface();
ControlsInterface controlsInterface = videoPlayer.getControlsInterface();
CodecInterface codecInterface = videoPlayer.getCodecInterface();
```

These three interfaces provide a multitude of essential functions, such as `pause`, `play`, and `setTime`. Details on these API features can be found in the [javadoc](https://sam54123.github.io/VideoLib/com/igrium/videolib/api/playback/package-summary.html).

## Events

VideoLib provides a number of callbacks that trigger at certain events during video playback. Regardless of where they're triggered in native code, all event listeners are called on the Minecraft client thread.

```java
VideoEvents events = videoPlayer.getEvents();
events.onBuffering(e -> {
    LogManager.getLogger().info("Buffering!");
})
```

Like, the playback interfaces, event functions are detailed in the [javadoc](https://sam54123.github.io/VideoLib/com/igrium/videolib/api/playback/VideoEvents.html).

# A Note on Licenses

The default implementation of VideoLib relies on VLCJ, which is licensed under the GNU GPL. This means that everything in the `vlc` package is licensed under the GPL. The rest of the repo, however, provided it doesn't directly depend on the `vlc` package, is licensed under the MIT license. Additionally, the `vlc` package is not considered a part of the public API, and it may be moved to a separate Gradle project at a later time.

# Planned Features

- Audio control.
  
  - Right now, VLC directly pipes audio to your operating system, bypassing Minecraft all-together. I intend to change this.

- Standardize supported formats.

- Warn users when VideoLib fails to initialize (usually due to missing natives).

- Rendering utility functions.
