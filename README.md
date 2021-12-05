# YoutubeWithChat

## Az alkalmazásról:

Az alkalmazás feladata, hogy dedikált id-jú Youtube videókat listáz ki, api hívással lekéri a címét, 
indexképét, majd egy külső lejátszó segítségével lejátszhatóvá teszi azt.

2 függőséget használ:

```groovy
    implementation 'com.github.getstream:stream-chat-android-client:1.4.2'
    implementation 'com.pierfrancescosoffritti.androidyoutubeplayer:core:10.0.5'
```