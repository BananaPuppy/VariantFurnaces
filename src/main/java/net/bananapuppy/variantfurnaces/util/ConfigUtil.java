package net.bananapuppy.variantfurnaces.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.bananapuppy.variantfurnaces.Config;
import net.bananapuppy.variantfurnaces.MainClass;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConfigUtil {
    //Vars
    private static JsonObject config = new JsonObject();

    private static final Path configPath = FabricLoader.getInstance().getConfigDir();
    private static File file;


    //Public
    public static void configInit(){
        try {
            ConfigUtil.configInitPriv();
        } catch (FileNotFoundException e) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config Init() threw File Not Found Exception");
            MainClass.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        } catch (IOException e) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config Init() threw IO Exception");
            MainClass.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    public static void configRead(){
        try {
            ConfigUtil.configReadPriv();
        } catch (FileNotFoundException e) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config Read() threw File Not Found Exception");
            MainClass.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    //Private
    private static void configInitPriv() throws IOException {
        //Get file from Path
        file = configPath.resolve(MainClass.MOD_ID + ".json").toFile();

        //JsonObject with default vars


        //Make config if it doesn't exist
        if(!file.exists()) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config is missing, generating default one..." );
            // try creating missing files
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            Files.createFile(file.toPath());

            // write default config data
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);

            writer.write(stringJsonFromConfig());
            writer.close();
        }

        configRead();
    }

    private static String stringJsonFromConfig(){
        try {
            JsonObject json = new JsonObject();
            Class<Config> configClass = Config.class;
            Field[] fields = configClass.getFields();
            for(Field field : fields){
                if(field.getType() == boolean.class){
                    json.addProperty(field.getName(), field.getBoolean(configClass));
                }
                if(field.getType() == int.class){
                    json.addProperty(field.getName(), field.getInt(configClass));
                }
                if(field.getType() == float.class){
                    json.addProperty(field.getName(), field.getFloat(configClass));
                }
                if(field.getType().getSimpleName().equals("String")){
                    json.addProperty(field.getName(), (String)field.get(configClass));
                }
                if(field.getType().getSimpleName().equals("List")){
                    List<?> list = (List<?>)field.get(configClass);
                    JsonArray array = new JsonArray();
                    String elementtype = list.get(0).getClass().getSimpleName();
                    for(Object o : list){
                        if(elementtype.equals("Boolean")){
                            array.add((Boolean)o);
                        }
                        if(elementtype.equals("Integer")){
                            array.add((Integer)o);
                        }
                        if(elementtype.equals("Float")){
                            array.add((Float)o);
                        }
                        if(elementtype.equals("String")){
                            array.add((String)o);
                        }
                    }
                    json.add(field.getName(), array);
                }
            }
            return json.toString();
        } catch (IllegalAccessException e) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config StringJsonFromConfig() threw Illagal Access Exception");
            MainClass.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private static void configReadPriv() throws FileNotFoundException {

        //Read Config
        Scanner reader = new Scanner(file);
        String readerOutput = "";
        if(reader.hasNextLine()) {
            readerOutput += reader.nextLine();
        }
        if(!readerOutput.equals("")){
            config = JsonParser.parseString(readerOutput).getAsJsonObject();
        }
        configParse();
    }

    private static void configParse(){
        try{
            configParsePriv();
        } catch (NoSuchFieldException e) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config Parse() threw No Such Field Exception");
            MainClass.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            MainClass.LOGGER.info(MainClass.MOD_TITLE + " Config Parse() Illagal Access Exception");
            MainClass.LOGGER.trace(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private static void configParsePriv() throws NoSuchFieldException, IllegalAccessException {
        JsonObject config = ConfigUtil.config;
        Set<String> keys = config.keySet();
        Class<Config> configClass = Config.class;
        for(String key : keys){
            Field field = configClass.getField(key);
            if(field.getType() == boolean.class){
                field.set(configClass, config.get(key).getAsBoolean());
            }
            if(field.getType() == int.class){
                field.set(configClass, config.get(key).getAsInt());
            }
            if(field.getType() == float.class){
                field.set(configClass, config.get(key).getAsFloat());
            }
            if(field.getType() == String.class){
                field.set(configClass, config.get(key).getAsString());
            }
            if(field.getType() == List.class){
                JsonArray array = config.get(key).getAsJsonArray();
                List<?> list = listFromJsonArray(array);
                field.set(configClass, list);
            }
        }
    }

    private static List<?> listFromJsonArray(JsonArray array){
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
