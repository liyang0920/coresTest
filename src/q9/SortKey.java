package q9;

import java.util.List;

import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData.Record;

public class SortKey implements Comparable<SortKey> {
    private Record record;
    private int keyNO;

    public SortKey(Record record, int keyNO) {
        this.record = record;
        this.keyNO = keyNO;
    }

    public Record getRecord() {
        return record;
    }

    public String getValue() {
        StringBuilder res = new StringBuilder();
        res.append(record.get(0));
        for (int i = 1; i < record.getSchema().getFields().size(); i++) {
            res.append("|" + record.get(i));
        }
        return res.toString();
    }

    public int getKeyNO() {
        return keyNO;
    }

    @Override
    public int compareTo(SortKey o) {
        assert (keyNO == o.keyNO);
        List<Field> fs = record.getSchema().getFields();
        for (int i = 0; i < keyNO; i++) {
            if (isInteger(fs.get(i))) {
                long k1 = Long.parseLong(record.get(i).toString());
                long k2 = Long.parseLong(o.record.get(i).toString());
                if (k1 > k2) {
                    return 1;
                } else if (k1 < k2) {
                    return -1;
                }
            } else {
                String k1 = record.get(i).toString();
                String k2 = o.record.get(i).toString();
                if (k1.compareTo(k2) > 0) {
                    return 1;
                } else if (k1.compareTo(k2) < 0) {
                    return -1;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return (compareTo((SortKey) o) == 0);
    }

    boolean isInteger(Field f) {
        switch (f.schema().getType()) {
            case LONG:
            case INT:
                return true;
            case STRING:
            case BYTES:
                return false;
            default:
                throw new ClassCastException("This type is not supported for Key type: " + f.schema());
        }
    }
}