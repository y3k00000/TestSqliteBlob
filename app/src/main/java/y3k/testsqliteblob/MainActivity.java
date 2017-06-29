package y3k.testsqliteblob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestSQLiteOpenHelper dbHelper = new TestSQLiteOpenHelper(this);
        for (int i = 0; i < 10; i++) {
            TestObject testObject = new TestObject(i, "y3k", "y3k is so cool and mad".getBytes());
            try {
                dbHelper.insertTestObject(testObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Iterator<TestObject> readIterator = dbHelper.getTestObjects();
        while (readIterator.hasNext()) {
            Log.d("TestObject", readIterator.next().toString());
        }
    }
}
