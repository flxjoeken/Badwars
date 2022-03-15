package bedwarsboys.badwars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class BadwarsMessages {

    //configure Teams procedure Chat Text.
    public static final TextComponent BEGIN_MESSAGE_1 = Component
            .text(Badwars.PLUGIN_NAME + "Herzlich Willkommen im Spiel-Konfigurations-Generator. Möchtest du die Konfiguration " +
                    "abbrechen, kannst du das jederzeit mit").append(Component.text(" exit ")
                    .color(NamedTextColor.RED)).append(Component.text("im Chat tun."));
    public static final TextComponent BEGIN_MESSAGE_2 = Component
            .text(Badwars.PLUGIN_NAME + "Als erstes kannst du auswählen, wie viele Teams und welche Teamfarben du in dieser " +
                    "Konfiguration verwenden möchtest. Um zu beginnen, schreibe ")
            .append(Component.text("weiter").color(NamedTextColor.GREEN)).append(Component.text("."));
    public static final TextComponent SELECT_TEAM_COLOR_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Wähle nun das Team aus, das du " +
            "konfigurieren möchtest. Schreibe dazu den Namen eines der folgenden Teams in den Chat: ");
    public static final TextComponent ADD_SPAWN_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Stelle dich nun auf den " +
                    "Wiedereinstiegspunkt des Teams und tippe ")
            .append(Component.text("spawn").color(NamedTextColor.GREEN));
    public static final TextComponent ADDED_SPAWN_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Der Wiedereinstiegspunkt von " +
            "Team %0 wurde erfolgreich erfasst");
    public static final TextComponent ADD_BED_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Klicke nun auf das Bett des Teams");
    public static final TextComponent ADD_TEAM_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Möchtest du ein weiteres Team hinzufügen dann tippe ")
            .append(Component.text("weiter ").color(NamedTextColor.RED))
            .append(Component.text("sonst tippe "))
            .append(Component.text("fertig ").color(NamedTextColor.RED))
            .append(Component.text("."));
    public static final TextComponent ADD_SPAWNER_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Nun kannst du die Spawner des Spiels " +
                    "konfigurieren. Stelle dich dazu auf den Ort an dem die Ressourcen erscheinen sollen und tippe")
            .append(Component.text("gold").color(NamedTextColor.RED))
            .append(Component.text(", "))
            .append(Component.text("eisen").color(NamedTextColor.RED))
            .append(Component.text(" oder "))
            .append(Component.text("bronze").color(NamedTextColor.RED))
            .append(Component.text(" um den Spawner zu erstellen. "));
    public static final TextComponent ADDED_SPAWNER_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Ein ");
    public static final TextComponent ADDED_SPAWNER_MESSAGE1 = Component.text(" wurde erfolgreich erstellt. ")
            .append(Component.text("Möchtest du einen weiteren Spawner erstellen, dann tippe "))
            .append(Component.text("weiter").color(NamedTextColor.RED))
            .append(Component.text(", sonst tippe "))
            .append(Component.text("fertig").color(NamedTextColor.RED))
            .append(Component.text(". "));
    public static final TextComponent FINISHED_CONFIG_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Du hast das Spiel erfolgreich konfiguriert")
            .append(Component.newline())
            .append(Component.text("goodbye!"));

    public static final String ADDED_BED_MESSAGE = Badwars.PLUGIN_NAME + "Das Bett von Team %0 wurde erfolgreich erfasst.";
    public static final TextComponent TEAM_NOT_EXIST_MESSAGE = Component.text(Badwars.PLUGIN_NAME + "Dieses Team existiert nicht.");
    //TODO Change to German
    public static final String NOT_CONFIGURING_TEAM_MESSAGE = Badwars.PLUGIN_NAME + "You are not configuring a team.";

}
