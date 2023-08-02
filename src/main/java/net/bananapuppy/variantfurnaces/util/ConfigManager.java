package net.bananapuppy.variantfurnaces.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.bananapuppy.variantfurnaces.Config;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConfigManager {
    private final Logger LOGGER;
    private final Integer schemaVersion;

    private final Class<?> configClazz;
    private Path configPath = FabricLoader.getInstance().getConfigDir();
    private final File configFile;

    public ConfigManager(Integer schemaVersion, String id, Logger logger, Class<?> clazz){
        this(schemaVersion, id, logger, clazz, null);
    }
    public ConfigManager(Integer schemaVersion, String id, Logger logger, Class<?> configClazz, @Nullable Path path){

        this.LOGGER = logger;
        this.schemaVersion = schemaVersion;

        this.configClazz = configClazz;
        if(path != null){ this.configPath = path; } else { path = this.configPath; }
        this.configFile = path.resolve(id + schemaVersion.toString() +".json").toFile();
    }

    public ConfigManager init(){
        if(!this.configFile.exists()){
            createFile(this.configFile);
            writeFieldsToFile();
        }
        else {
            writeFileToFields();
        }
        return this;
    }

    private void createFile(File file){
        boolean success = file.getParentFile().mkdirs();
        if(!success){
            this.LOGGER.info("Config createFile() returned failure");
        }
    }

    public void writeFieldsToFile(){
        try(PrintWriter writer = new PrintWriter(this.configFile, StandardCharsets.UTF_8)){
            writer.write(jsonFromFields().toString());
        } catch (IOException e) {
            this.LOGGER.info("Config writeFieldsToFile() threw IOException");
            this.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }
    public void writeFileToFields(){
        try {
            JsonObject json = jsonFromFile();

            Set<String> keys = json.keySet();
            Class<Config> configClass = Config.class;
            for (String key : keys) {
                Field field = configClass.getField(key);
                if (field.getType() == boolean.class) {
                    field.set(configClass, json.get(key).getAsBoolean());
                }
                if (field.getType() == int.class) {
                    field.set(configClass, json.get(key).getAsInt());
                }
                if (field.getType() == float.class) {
                    field.set(configClass, json.get(key).getAsFloat());
                }
                if (field.getType() == String.class) {
                    field.set(configClass, json.get(key).getAsString());
                }
                if (field.getType() == List.class) {
                    JsonArray array = json.get(key).getAsJsonArray();
                    List<?> list = jsonArrayToList(array);
                    field.set(configClass, list);
                }
            }
        } catch (NoSuchFieldException e) {
            this.LOGGER.info("Config writeFileToFields() threw NoSuchFieldException");
            this.LOGGER.info("Config option title mismatch");
            this.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            this.LOGGER.info("Config writeFileToFields() threw IllegalAccessException");
            this.LOGGER.info("Is the Field private?");
            this.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private JsonObject jsonFromFields(){
        try{
            JsonObject json = new JsonObject();
            Field[] fields = this.configClazz.getFields();
            for(Field field : fields){
                if(field.getType() == boolean.class){
                    json.addProperty(field.getName(), field.getBoolean(this.configClazz));
                }
                if(field.getType() == int.class){
                    json.addProperty(field.getName(), field.getInt(this.configClazz));
                }
                if(field.getType() == float.class){
                    json.addProperty(field.getName(), field.getFloat(this.configClazz));
                }
                if(field.getType().getSimpleName().equals("String")){
                    json.addProperty(field.getName(), (String)field.get(this.configClazz));
                }
                if(field.getType().getSimpleName().equals("List")){ //TODO: add List<List<List<ETC>>> support (embedded lists)
                    List<?> list = (List<?>)field.get(this.configClazz);
                    JsonArray array = new JsonArray();
                    String objTypeName = list.get(0).getClass().getSimpleName();
                    for(Object o : list){
                        if(objTypeName.equals("Boolean")){
                            array.add((Boolean)o);
                        }
                        if(objTypeName.equals("Integer")){
                            array.add((Integer)o);
                        }
                        if(objTypeName.equals("Float")){
                            array.add((Float)o);
                        }
                        if(objTypeName.equals("String")){
                            array.add((String)o);
                        }
                    }
                    json.add(field.getName(), array);
                }
            }
            return json;
        } catch (IllegalAccessException e) {
            this.LOGGER.info("Config jsonFromFields() threw IllegalAccessException");
            this.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private JsonObject jsonFromFile(){
        try{
            JsonObject json = new JsonObject();

            Scanner reader = new Scanner(this.configFile);
            String readerOutput = "";
            if (reader.hasNextLine()) {
                readerOutput += reader.nextLine();
            }
            if (!readerOutput.equals("")) {
                json = JsonParser.parseString(readerOutput).getAsJsonObject();
            }

            return json;
        } catch (FileNotFoundException e) {
            this.LOGGER.info("Config jsonFromFile() threw FileNotFoundException");
            this.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private static List<?> jsonArrayToList(JsonArray array){ //TODO: add List<List<List<ETC>>> support (embedded lists)
        List<?> list = List.of();

        int index = 0;
        String elementType = array.get(0).getClass().getSimpleName();
        //Boolean iter
        if(elementType.equals("Boolean")){
            ArrayList<Boolean> boolList = new ArrayList<>();
            for(JsonElement element : array){
                boolList.add(index, element.getAsBoolean());
                index++;
            }
            list = boolList;
        }
        //Int iter
        if(elementType.equals("Integer")){
            ArrayList<Integer> intList = new ArrayList<>();
            for(JsonElement element : array){
                intList.add(index, element.getAsInt());
                index++;
            }
            list = intList;
        }
        //Float iter
        if(elementType.equals("Float") || elementType.equals("Long") || elementType.equals("Double")){
            ArrayList<Float> floatList = new ArrayList<>();
            for(JsonElement element : array){
                floatList.add(index, element.getAsFloat());
                index++;
            }
            list = floatList;
        }
        //String iter
        if(elementType.equals("String")){
            ArrayList<String> stringList = new ArrayList<>();
            for(JsonElement element : array){
                stringList.add(index, element.getAsString());
                index++;
            }
            list = stringList;
        }

        return list;
    }
}
