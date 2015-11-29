package com.lendou.hbase;

import org.apache.commons.lang.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Iterator;

/**
 * Hello world!
 *
 */
public class Program {
    private static Configuration conf = null;

    private static final String TABLE_NAME = "test";

    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
    }

    public static void createTable(String tableName, String[] families) {
        Validate.notNull(tableName);
        Validate.notNull(families);

        Connection conn = null;
        HBaseAdmin admin = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin) conn.getAdmin();
            if (admin.tableExists(TABLE_NAME)) {
                System.out.println("Table " + TABLE_NAME + " already exists!");

            } else {
                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
                for (int i = 0; i < families.length; i++) {
                    tableDesc.addFamily(new HColumnDescriptor(families[i]));
                }
                admin.createTable(tableDesc);
                System.out.println("Create table " + tableName + " ok.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteTable(String tableName) {
        Validate.notEmpty(tableName);

        Connection conn = null;
        HBaseAdmin admin = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin) conn.getAdmin();
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addRecord(String tableName, String rowKey, String family, String qualifier, String value) {
        Validate.notEmpty(tableName);
        Validate.notEmpty(rowKey);
        Validate.notEmpty(family);
        Validate.notEmpty(qualifier);
        Validate.notEmpty(value);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));

            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
            System.out.println("insert record " + rowKey + " to table " + tableName + " ok.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteRecord(String tableName, String rowKey) {
        Validate.notEmpty(tableName);
        Validate.notEmpty(rowKey);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
            System.out.println("del record " + rowKey + " ok.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteRecord(String tableName, String rowKey, String family, String qualifier) {
        Validate.notEmpty(tableName);
        Validate.notEmpty(rowKey);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            table.delete(delete);
            System.out.println("del record " + rowKey + " ok.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void getRecord(String tableName, String rowKey) {
        Validate.notEmpty(tableName);
        Validate.notEmpty(rowKey);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));

            Get get = new Get(Bytes.toBytes(rowKey));
            Result rs = table.get(get);
            for (Cell cell : rs.rawCells()) {
                String rowValue = Bytes.toString(Bytes.copy(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
                String familyValue = Bytes.toString(Bytes.copy(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
                String qualifierValue = Bytes.toString(Bytes.copy(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
                String value = Bytes.toString(Bytes.copy(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));

                System.out.println(String.format("%s\t%s:%s\t%s", rowValue, familyValue, qualifierValue, value));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void getRecords(String tableName) {
        Validate.notEmpty(tableName);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));

            Scan scan = new Scan();
            ResultScanner rss = table.getScanner(scan);
            Iterator<Result> iter = rss.iterator();
            int i = 0;
            while (iter.hasNext()) {
                Result rs = iter.next();
                for (Cell cell : rs.rawCells()) {
                    String rowValue = Bytes.toString(Bytes.copy(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
                    String familyValue = Bytes.toString(Bytes.copy(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
                    String qualifierValue = Bytes.toString(Bytes.copy(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
                    String value = Bytes.toString(Bytes.copy(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));

                    System.out.println(String.format("%s\t%s:%s\t%s", rowValue, familyValue, qualifierValue, value));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main( String[] args ) throws IOException {
        // addRecord(TABLE_NAME, "r1", "x", "a", "value-a1");
        deleteRecord(TABLE_NAME, "r1", "x", "b");
        // deleteRecord(TABLE_NAME, "r1");
        getRecords(TABLE_NAME);
    }
}
