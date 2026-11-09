package pl.firestar.klany;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Dokleja [TAG] przed nickiem na czacie. Opakowuje format ulozony przez Essentials
 * (albo inny plugin czatu), wiec jego configu nie trzeba ruszac.
 * Kolor tagu zalezy od widza: jego klan zielony, sojusznik aqua, reszta czerwony.
 */
public class ChatTagListener implements Listener {

    private final FirestarKlany plugin;

    public ChatTagListener(FirestarKlany plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {
        if (!plugin.getConfig().getBoolean("czat.tag-w-czacie", true)) return;
        ChatRenderer poprzedni = e.renderer();
        e.renderer((zrodlo, nazwa, wiadomosc, widz) -> {
            Component baza = poprzedni.render(zrodlo, nazwa, wiadomosc, widz);
            KlanManager m = plugin.manager();
            Klan klanZrodla = m.klanGracza(zrodlo.getUniqueId());
            if (klanZrodla == null) return baza;
            Klan klanWidza = widz instanceof Player p ? m.klanGracza(p.getUniqueId()) : null;
            return Text.c(m.znacznik(klanWidza, klanZrodla)).append(baza);
        });
    }
}
