package globant.bairesparking.Commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author s.ruiz
 */

public class ParkingsDBHelper extends SQLiteOpenHelper {
    private static String dbName = "ParkingsFavoriteDB";

    public ParkingsDBHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ParkingsFavoriteTable(id integer primary key, parkingid bigint)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ParkingsFavoriteTable");
        onCreate(sqLiteDatabase);
    }

    public boolean insertFavorite(long parkingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("parkingid", parkingId);
        db.insert("ParkingsFavoriteTable", null, contentValues);
        return true;
    }

    public int deleteFavorite(long parkingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ParkingsFavoriteTable","parkingid = ? ", new String[] {String.valueOf(parkingId)});
    }

    public List getFavorites() {
        List favoritesList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("select * from ParkingsFavoriteTable", null);
        while (data.moveToNext()) {
            favoritesList.add(data.getLong(1));
        }
        return favoritesList;
    }
}
