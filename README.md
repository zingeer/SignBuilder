# SignBuilder 
Simple bulder sign menu on kotlin.  
```kotlin
repositories {
    maven ("https://jitpack.io/")
}
dependencies {
    implementation("com.github.zingeer", "SignBuilder", "1.0.1")
}
```  
Example:
```kotlin
class Plugin: JavaPlugin(), Listener {
    override fun onEnable() {
        SignManager.initialization(this)
        Bukkit.getServer().getPluginManager().registerEvents(Listener, this)
    }
}

object Listener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        SignBuilder(arrayOf("You join!", "", "", "Hi!"), SignTexture.BIRCH) {
            player.sendMessage("You closed the sign menu!)
        }
    }
}
```
