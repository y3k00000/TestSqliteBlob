package y3k.testsqliteblob;

import java.io.Serializable;

/**
 * Serializable Object For tTest.
 */
public class TestObject implements Serializable {
    public final int id;
    public final String name;
    public final byte[] data;

    public TestObject(int id, String name, byte[] data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    @Override
    public String toString() {
        return "\"" + this.id + "," + this.name + "," + new String(this.data) + "\"";
    }
}
