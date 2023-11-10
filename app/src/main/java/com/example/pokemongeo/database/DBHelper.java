package com.example.pokemongeo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pokemongeo.R;
import com.example.pokemongeo.model.Item;
import com.example.pokemongeo.model.POKEMON_TYPE;
import com.example.pokemongeo.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // TABLE NAME:
    private final static String POKEDEX_TABLE = "pokedex";
    private final static String INVENTORY_TABLE = "inventory";
    private final static String MY_POKEMON_TABLE = "myPokemon";

    // COLUMNS POKEDEX :
    private final static String POKEDEX_COLUMN_DEXNO = "dexNo";
    private final static String POKEDEX_COLUMN_POKEMON_NAME = "name";
    private final static String POKEDEX_COLUMN_POKEMON_IMAGE = "image";
    private final static String POKEDEX_COLUMN_TYPE1 = "type1";
    private final static String POKEDEX_COLUMN_IMAGE_TYPE1 = "imageType1";
    private final static String POKEDEX_COLUMN_TYPE2 = "type2";
    private final static String POKEDEX_COLUMN_IMAGE_TYPE2 = "imageType2";
    private final static String POKEDEX_COLUMN_HEIGHT = "height";
    private final static String POKEDEX_COLUMN_WEIGHT = "weight";
    private final static String POKEDEX_COLUMN_CAPTURE_IMAGE = "captureImage";

    // COLUMNS INVENTORY :
    private final static String INVENTORY_COLUMN_ID = "invID";
    private final static String INVENTORY_COLUMN_ITEM = "item";
    private final static String INVENTORY_COLUMN_QUANTITY = "quantity";
    private final static String INVENTORY_COLUMN_PRICE = "price";

    // COLUMNS MY POKEMON :
    private final static String MY_POKEMON_COLUMN_ID = "myPokemonID";
    private final static String MY_POKEMON_COLUMN_NAME = "pokemonName";
    private final static String MY_POKEMON_COLUMN_LVL = "LVL";
    private final static String MY_POKEMON_COLUMN_ATK = "ATK";
    private final static String MY_POKEMON_COLUMN_HP = "HP";


    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POKEDEX_TABLE = "CREATE TABLE " + POKEDEX_TABLE +
                " ( " +
                    POKEDEX_COLUMN_DEXNO + " INTEGER PRIMARY KEY NOT NULL, " +
                    POKEDEX_COLUMN_POKEMON_NAME + " VARCHAR(15) NOT NULL, " +
                    POKEDEX_COLUMN_POKEMON_IMAGE + " INTEGER, " +
                    POKEDEX_COLUMN_TYPE1 + " VARCHAR(15) NOT NULL, " +
                    POKEDEX_COLUMN_IMAGE_TYPE1 + " INTEGER NOT NULL, " +
                    POKEDEX_COLUMN_TYPE2 + " VARCHAR(15), " +
                    POKEDEX_COLUMN_IMAGE_TYPE2 + " INTEGER, " +
                    POKEDEX_COLUMN_HEIGHT + " VARCHAR(15), " +
                    POKEDEX_COLUMN_WEIGHT + " VARCHAR(15), " +
                    POKEDEX_COLUMN_CAPTURE_IMAGE + " INT(8)" +
                ");";

        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + INVENTORY_TABLE +
                " ( " +
                INVENTORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                INVENTORY_COLUMN_ITEM + " VARCHAR(30) NOT NULL, " +
                INVENTORY_COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                INVENTORY_COLUMN_PRICE + " INTEGER NOT NULL " +
                ");";

        String CREATE_MY_POKEMON_TABLE = "CREATE TABLE " + MY_POKEMON_TABLE +
                " ( " +
                MY_POKEMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                MY_POKEMON_COLUMN_NAME + " VARCHAR(15) NOT NULL, " +
                MY_POKEMON_COLUMN_LVL + " INTEGER NOT NULL, " +
                MY_POKEMON_COLUMN_HP + " INTEGER NOT NULL, " +
                MY_POKEMON_COLUMN_ATK + " INTEGER NOT NULL " +
                ");";

        db.beginTransaction();
        try {
            db.execSQL(CREATE_POKEDEX_TABLE);
            db.execSQL(CREATE_INVENTORY_TABLE);
            db.execSQL(CREATE_MY_POKEMON_TABLE);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Pokemon> getAllPokemon() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[] {
                POKEDEX_COLUMN_DEXNO, POKEDEX_COLUMN_POKEMON_NAME, POKEDEX_COLUMN_POKEMON_IMAGE,
                POKEDEX_COLUMN_TYPE1, POKEDEX_COLUMN_IMAGE_TYPE1, POKEDEX_COLUMN_TYPE2,
                POKEDEX_COLUMN_IMAGE_TYPE2,  POKEDEX_COLUMN_HEIGHT, POKEDEX_COLUMN_WEIGHT, POKEDEX_COLUMN_CAPTURE_IMAGE
        };
        Cursor cursor =  db.query(POKEDEX_TABLE, columns, null, null,
                null, null, POKEDEX_COLUMN_DEXNO + " ASC");
        List<Pokemon> pokemonList = fillPokemonList(cursor);

        return pokemonList;
    }

    /**
     * return a list of Pokemon model from what the cursor is pointing at
     * The cursor if from the table Pokedex
     * @param cursor
     * @return List<Pokemon>
     */
    private List<Pokemon> fillPokemonList(Cursor cursor) {
        List<Pokemon> pokemonList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                pokemonList.add( cursorToPokemonModel(cursor) );
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pokemonList;
    }

    /**
     * Creat a Pokemon model from the pointed pokemon by the cursor
     * the cursor if from the table pokdex
     * @param cursor
     * @return Pokemon
     */
    private Pokemon cursorToPokemonModel(Cursor cursor){
        // Index des columns
        int dexNoColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_DEXNO);
        int nameColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_POKEMON_NAME);
        int imageColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_POKEMON_IMAGE);
        int type1ColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_TYPE1);
        int imageType1ColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_IMAGE_TYPE1);
        int type2ColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_TYPE2);
        int imageType2ColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_IMAGE_TYPE2);
        int weightColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_WEIGHT);
        int heightColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_HEIGHT);
        int captureImageColumnIndex = cursor.getColumnIndex(POKEDEX_COLUMN_CAPTURE_IMAGE);

        POKEMON_TYPE type2 = null;
        if (cursor.getString(type2ColumnIndex) != null) {
            type2 = POKEMON_TYPE.valueOf(cursor.getString(type1ColumnIndex));
            // Pas besoin du test avec l'image type2 car celui-ci sera déjà stocker en -1
            // si le type est null
        }
        return new Pokemon(
                cursor.getInt(dexNoColumnIndex),
                cursor.getString(nameColumnIndex),
                cursor.getInt(imageColumnIndex),
                POKEMON_TYPE.valueOf(cursor.getString(type1ColumnIndex)),
                cursor.getInt(imageType1ColumnIndex),
                type2,
                cursor.getInt(imageType2ColumnIndex),
                cursor.getString(weightColumnIndex),
                cursor.getString(heightColumnIndex),
                cursor.getInt(captureImageColumnIndex)
        );
    }
    public void createAllPokemon(Context context) {
        //Ouverture du fichier res/raw
        InputStreamReader isr = new InputStreamReader(context.getResources().openRawResource(R.raw.poke));

        BufferedReader reader = new BufferedReader(isr);
        StringBuilder builder = new StringBuilder();
        String data = "";

        //lecture du fichier. data == null => EOF
        while(data != null) {
            try {
                data = reader.readLine();
                builder.append(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        //Traitement du fichier
        db.beginTransaction();
        try {
            JSONArray array = new JSONArray(builder.toString());
            for (int dexNo = 1; dexNo <= array.length(); dexNo++) {
                JSONObject object = array.getJSONObject(dexNo - 1);
                String name = object.getString("name");
                String weight = object.getString("weight");
                String height = object.getString("height");

                String drawableName = object.getString("image");
                int frontResource = context.getResources().getIdentifier(drawableName,"drawable",
                        context.getPackageName());

                String type1 =  object.getString("type1");
                int imageType1 = context.getResources().getIdentifier(type1.toLowerCase(),"drawable",
                        context.getPackageName());

                String type2 = null;
                int imageType2 = -1;
                if (object.has("type2")) {
                    type2 = object.getString("type2");
                    imageType2 = context.getResources().getIdentifier(type2.toLowerCase(),"drawable",
                            context.getPackageName());
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(POKEDEX_COLUMN_DEXNO, dexNo);
                contentValues.put(POKEDEX_COLUMN_POKEMON_NAME, name);
                contentValues.put(POKEDEX_COLUMN_POKEMON_IMAGE, frontResource);
                contentValues.put(POKEDEX_COLUMN_TYPE1, type1);
                contentValues.put(POKEDEX_COLUMN_IMAGE_TYPE1, imageType1);
                contentValues.put(POKEDEX_COLUMN_TYPE2, type2);
                contentValues.put(POKEDEX_COLUMN_IMAGE_TYPE2, imageType2);
                contentValues.put(POKEDEX_COLUMN_WEIGHT, weight);
                contentValues.put(POKEDEX_COLUMN_HEIGHT, height);
                contentValues.put(POKEDEX_COLUMN_CAPTURE_IMAGE, -1); //Aucune capture à l'initialisation
                db.insert(POKEDEX_TABLE, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
    public void initInventory() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(INVENTORY_COLUMN_ITEM, "Gold");
            contentValues.put(INVENTORY_COLUMN_QUANTITY, 1000);
            contentValues.put(INVENTORY_COLUMN_PRICE, 0);
            db.insert(INVENTORY_TABLE, null, contentValues);

            contentValues.put(INVENTORY_COLUMN_ITEM, "Potion");
            contentValues.put(INVENTORY_COLUMN_QUANTITY, 10);
            contentValues.put(INVENTORY_COLUMN_PRICE, 200);
            db.insert(INVENTORY_TABLE, null, contentValues);

            contentValues.put(INVENTORY_COLUMN_ITEM, "PokeBall");
            contentValues.put(INVENTORY_COLUMN_QUANTITY, 10);
            contentValues.put(INVENTORY_COLUMN_PRICE, 100);
            db.insert(INVENTORY_TABLE, null, contentValues);

            contentValues.put(INVENTORY_COLUMN_ITEM, "SuperBall");
            contentValues.put(INVENTORY_COLUMN_QUANTITY, 10);
            contentValues.put(INVENTORY_COLUMN_PRICE, 300);
            db.insert(INVENTORY_TABLE, null, contentValues);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            db.endTransaction();
        }
    }

    public List<Pokemon> getStarter() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[] {
                POKEDEX_COLUMN_DEXNO, POKEDEX_COLUMN_POKEMON_NAME, POKEDEX_COLUMN_POKEMON_IMAGE,
                POKEDEX_COLUMN_TYPE1, POKEDEX_COLUMN_IMAGE_TYPE1, POKEDEX_COLUMN_TYPE2,
                POKEDEX_COLUMN_IMAGE_TYPE2,  POKEDEX_COLUMN_HEIGHT, POKEDEX_COLUMN_WEIGHT, POKEDEX_COLUMN_CAPTURE_IMAGE
        };
        String whereClause = POKEDEX_COLUMN_POKEMON_NAME + " IN (?, ?, ?)";
        String[] whereArgs = {"Bulbizarre", "Salamèche", "Carapuce"};
        Cursor cursor =  db.query(POKEDEX_TABLE, columns, whereClause, whereArgs,
                null, null, POKEDEX_COLUMN_DEXNO + " ASC");
        List<Pokemon> starterList = fillPokemonList(cursor);
        return starterList;
    }

    public List<Item> getInventory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {
                INVENTORY_COLUMN_ITEM, INVENTORY_COLUMN_QUANTITY, INVENTORY_COLUMN_PRICE,
                INVENTORY_COLUMN_ID
        };

        Cursor cursor =  db.query(INVENTORY_TABLE, columns, null, null,
                null, null, INVENTORY_COLUMN_ID + " ASC");

        //Fill item list
        List<Item> itemList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Index des columns
                int itemNameColumnIndex = cursor.getColumnIndex(INVENTORY_COLUMN_ITEM);
                int priceColumnIndex = cursor.getColumnIndex(INVENTORY_COLUMN_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(INVENTORY_COLUMN_QUANTITY);

                Item item = new Item(
                        cursor.getString(itemNameColumnIndex),
                        cursor.getInt(priceColumnIndex),
                        cursor.getInt(quantityColumnIndex)
                );
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return itemList;
    }

    public void capturePokemon(Pokemon pokemon) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MY_POKEMON_COLUMN_NAME, pokemon.getName());
            contentValues.put(MY_POKEMON_COLUMN_LVL, pokemon.getStateLEVEL());
            contentValues.put(MY_POKEMON_COLUMN_ATK, pokemon.getStateATK());
            contentValues.put(MY_POKEMON_COLUMN_HP, pokemon.getStateHP());
            db.insert(MY_POKEMON_TABLE, null, contentValues);

            // Set capture flag to 1
            ContentValues contentValuesPokedex = new ContentValues();
            contentValuesPokedex.put(POKEDEX_COLUMN_DEXNO, pokemon.getOrder());
            contentValuesPokedex.put(POKEDEX_COLUMN_CAPTURE_IMAGE, R.drawable.pokeball);

            String whereClause = POKEDEX_COLUMN_DEXNO + "= ?";
            String[] whereArgs = {String.valueOf(pokemon.getOrder())};
            db.update(POKEDEX_TABLE, contentValuesPokedex, whereClause, whereArgs);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
    public List<Pokemon> getMyPokemon() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {
                MY_POKEMON_COLUMN_NAME, MY_POKEMON_COLUMN_LVL, MY_POKEMON_COLUMN_ATK,
                MY_POKEMON_COLUMN_HP, MY_POKEMON_COLUMN_ID
        };

        Cursor cursor =  db.query(MY_POKEMON_TABLE, columns, null, null,
                null, null, MY_POKEMON_COLUMN_ID + " ASC");

        List<Pokemon> pokemonList = new ArrayList<Pokemon>();
        if (cursor.moveToFirst()) {
            do {
                int indexName = cursor.getColumnIndex( MY_POKEMON_COLUMN_NAME);
                Pokemon pokemon = getOnePokemonByName( cursor.getString( indexName ) );

                int indexLEVEL = cursor.getColumnIndex( MY_POKEMON_COLUMN_LVL );
                int indexHP = cursor.getColumnIndex( MY_POKEMON_COLUMN_HP);
                int indexATK = cursor.getColumnIndex( MY_POKEMON_COLUMN_ATK);

                pokemon.setStats(
                        cursor.getInt(indexLEVEL)
                        , cursor.getInt( indexHP )
                        , cursor.getInt( indexATK)
                );

                pokemonList.add(pokemon);
            } while (cursor.moveToNext());
        }
        return pokemonList;
    }

    public Pokemon getOnePokemonByDexNo(int dexNo) {
        Pokemon onePokemon = new Pokemon();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {
                POKEDEX_COLUMN_DEXNO, POKEDEX_COLUMN_POKEMON_NAME, POKEDEX_COLUMN_POKEMON_IMAGE,
                POKEDEX_COLUMN_TYPE1, POKEDEX_COLUMN_IMAGE_TYPE1, POKEDEX_COLUMN_TYPE2,
                POKEDEX_COLUMN_IMAGE_TYPE2,  POKEDEX_COLUMN_HEIGHT, POKEDEX_COLUMN_WEIGHT, POKEDEX_COLUMN_CAPTURE_IMAGE
        };
        String whereClause = POKEDEX_COLUMN_DEXNO + "= ?";
        String[] whereArgs = {String.valueOf(dexNo)};
        Cursor cursor =  db.query(POKEDEX_TABLE, columns, whereClause, whereArgs,
                null, null, null);
        List<Pokemon> pokemonList = fillPokemonList(cursor);

        if (!pokemonList.isEmpty()) {
            onePokemon = pokemonList.get(0);
        }
        return onePokemon;
    }

    public Pokemon getOnePokemonByName(String name) {
        Pokemon onePokemon = new Pokemon();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {
                POKEDEX_COLUMN_DEXNO, POKEDEX_COLUMN_POKEMON_NAME, POKEDEX_COLUMN_POKEMON_IMAGE,
                POKEDEX_COLUMN_TYPE1, POKEDEX_COLUMN_IMAGE_TYPE1, POKEDEX_COLUMN_TYPE2,
                POKEDEX_COLUMN_IMAGE_TYPE2,  POKEDEX_COLUMN_HEIGHT, POKEDEX_COLUMN_WEIGHT, POKEDEX_COLUMN_CAPTURE_IMAGE
        };
        String whereClause = POKEDEX_COLUMN_POKEMON_NAME + "= ?";
        String[] whereArgs = { name };
        Cursor cursor =  db.query(POKEDEX_TABLE, columns, whereClause, whereArgs,
                null, null, null);
        List<Pokemon> pokemonList = fillPokemonList(cursor);

        if (!pokemonList.isEmpty()) {
            onePokemon = pokemonList.get(0);
        }
        return onePokemon;
    }
    public int getPokeballQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {
                INVENTORY_COLUMN_ITEM, INVENTORY_COLUMN_QUANTITY
        };
        String whereClause = INVENTORY_COLUMN_ITEM + "= ?";
        String[] whereArgs = {"PokeBall"};
        Cursor cursor =  db.query(INVENTORY_TABLE, columns, whereClause, whereArgs,
                null, null, null);

        cursor.moveToFirst();

        int quantityColumnIndex = cursor.getColumnIndex(INVENTORY_COLUMN_QUANTITY);
        int quantity = cursor.getInt(quantityColumnIndex);

        return quantity;
    }
    public void deleteOnePokeball() {
        SQLiteDatabase db = this.getWritableDatabase();
        int pokeballQuantity = getPokeballQuantity();

        if (pokeballQuantity > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(INVENTORY_COLUMN_QUANTITY, pokeballQuantity-1);

            String whereClause = INVENTORY_COLUMN_ITEM + "=?";
            String[] whereArgs = {"PokeBall"};
            db.update(INVENTORY_TABLE, contentValues, whereClause, whereArgs);
        }

    }
    public void addOnePokeball() {
        SQLiteDatabase db = this.getWritableDatabase();
        int pokeballQuantity = getPokeballQuantity();

        ContentValues contentValues = new ContentValues();
        contentValues.put(INVENTORY_COLUMN_QUANTITY, pokeballQuantity+1);

        String whereClause = INVENTORY_COLUMN_ITEM + "=?";
        String[] whereArgs = {"PokeBall"};
        db.update(INVENTORY_TABLE, contentValues, whereClause, whereArgs);
    }
}
