package y3k.testsqliteblob;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

public class TestSQLiteOpenHelper extends SQLiteOpenHelper {
    public TestSQLiteOpenHelper(Context context) {
        super(context, "TestBlobDatabase", null, 2);
    }

    private static TestObject parseTestObject(byte[] blob) throws IOException, ClassNotFoundException {
        return (TestObject) new ObjectInputStream(new ByteArrayInputStream(blob)).readObject();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists test(obj blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists test;");
        sqLiteDatabase.execSQL("create table if not exists test(obj blob);");
    }

    public void insertTestObject(TestObject testObject) throws IOException, SQLException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(testObject);
        this.getWritableDatabase().execSQL("insert into test (obj) values(?);", new Object[]{byteArrayOutputStream.toByteArray()});
    }

    public TestObjectIterator getTestObjects() {
        final Cursor cursor = this.getReadableDatabase().query("test", new String[]{"obj"}, null, null, null, null, null);
        return new TestObjectIterator(cursor);
    }

    public class TestObjectIterator implements Iterator<TestObject> {
        private final Cursor dbCursor;

        private TestObjectIterator(Cursor dbCursor) {
            this.dbCursor = dbCursor;
            this.dbCursor.moveToFirst();
        }

        @Override
        public final boolean hasNext() {
            if (this.dbCursor.isAfterLast()) {
                this.dbCursor.close();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public final TestObject next() {
            try {
                byte[] blob = this.dbCursor.getBlob(this.dbCursor.getColumnIndex("obj"));
                this.dbCursor.moveToNext();
                return TestSQLiteOpenHelper.parseTestObject(blob);
            } catch (IOException | ClassNotFoundException e) {
                Log.d(TestSQLiteOpenHelper.class.getSimpleName(), "TestObjectIterator", e);
                return null;
            }
        }
    }
}
