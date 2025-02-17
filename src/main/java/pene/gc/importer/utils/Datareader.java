package pene.gc.importer.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.player.Player;
import pene.gc.importer.GenshinImporter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class Datareader {

    public static int getAvatarId(String name){
        ClassLoader classLoader = GenshinImporter.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("avatars.json");
             InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            return new JsonParser().parse(reader).getAsJsonObject().get(name).getAsInt();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            Grasscutter.getLogger().warn("Possibly unknown avatar: " + name);
            e.printStackTrace();
        }
        return 0;
    }
    public static int getArtifactCode(String setKey) {
        ClassLoader classLoader = GenshinImporter.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("artifacts.json");
             InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            return new JsonParser().parse(reader).getAsJsonObject().get(setKey).getAsInt();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            Grasscutter.getLogger().warn("Possibly unknown artifact set: " + setKey);
            e.printStackTrace();
        }

        return 0;
    }
    public static void artifacts(Player targetPlayer, String filename) {
        String filepath = String.format("%s/GenshinImporter/Data/%s.json",Grasscutter.getConfig().folderStructure.plugins, filename);
        File file1 = new File(filepath);
            try (
                InputStream inputStream = new DataInputStream(new FileInputStream(file1));
                InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)) {
                JsonArray artifactsCodes = new JsonParser().parse(reader).getAsJsonObject().get("artifacts").getAsJsonArray();
                ArtifactConverter.main(artifactsCodes, targetPlayer);
                reader.close();
                streamReader.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                Grasscutter.getLogger().error("File not found: " + filepath);
                CommandHandler.sendMessage(targetPlayer, "File not found: " + filepath);
            } catch (IOException e) {
                 e.printStackTrace();
        }
    }
    //How the fuck am I supposed to get characters from artifacts?????
    // Answer - You don't. There's an option in Inventory Kamera to parse characters.
    public static void characters(Player targetPlayer, String filename) {
        String filepath = String.format("%s/GenshinImporter/Data/%s.json", Grasscutter.getConfig().folderStructure.plugins, filename);
        File file1 = new File(filepath);
        try (
                InputStream inputStream = new DataInputStream(new FileInputStream(file1));
                InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)) {
            JsonArray charactersArray = new JsonParser().parse(reader).getAsJsonObject().get("characters").getAsJsonArray();
            CharacterConverter.main(charactersArray, targetPlayer);
            reader.close();
            streamReader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            Grasscutter.getLogger().error("File not found: " + filepath);
            CommandHandler.sendMessage(targetPlayer, "File not found: " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
