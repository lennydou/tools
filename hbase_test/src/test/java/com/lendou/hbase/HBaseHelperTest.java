package com.lendou.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * HBaseHelper的测试类
 */
public class HBaseHelperTest {

    private static final String TABLE_NAME = "test";
    private static final String[] FAMILIES = new String[]{"x", "y"};
    private static final byte[] FAMILY_X = Bytes.toBytes("x");
    private static final byte[] FAMILY_Y = Bytes.toBytes("y");

    @Before
    public void setup() {
        HBaseHelper.deleteTable(TABLE_NAME);
        HBaseHelper.createTable(TABLE_NAME, FAMILIES);
    }

    @Test
    public void testPutAndGet() {
        final byte[] rowKey = Bytes.toBytes("r1");
        final byte[] aBytes = Bytes.toBytes("a");
        final byte[] vBytes = Bytes.toBytes("xa_value");

        // Write one record
        Put put = new Put(rowKey);
        put.addColumn(FAMILY_X, aBytes, vBytes);
        HBaseHelper.put(TABLE_NAME, put);

        // Read record
        Get get = new Get(rowKey);
        System.out.println(get.getMaxVersions());
        Result result = HBaseHelper.get(TABLE_NAME, get);

        // Parse result
        for (Cell cell : result.rawCells()) {
            Assert.assertArrayEquals(rowKey, Bytes.copy(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
            Assert.assertArrayEquals(FAMILY_X, Bytes.copy(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
            Assert.assertArrayEquals(aBytes, Bytes.copy(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
            Assert.assertArrayEquals(vBytes, Bytes.copy(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
        }
    }

    @Test
    public void testPutOverride() {
        final byte[] rowKey = Bytes.toBytes("r1");
        final byte[] aBytes = Bytes.toBytes("a");
        final byte[] vBytes = Bytes.toBytes("xa_value");
        final byte[] vBytes2 = Bytes.toBytes("xa_value2");

        long ts = System.currentTimeMillis();

        // Write one record
        Put put = new Put(rowKey);
        put.addColumn(FAMILY_X, aBytes, ts, vBytes);
        HBaseHelper.put(TABLE_NAME, put);

        // Write second record
        Put put2 = new Put(rowKey);
        put2.addColumn(FAMILY_X, aBytes, ts + 1, vBytes2);
        HBaseHelper.put(TABLE_NAME, put2);

        // Read record
        Get get = new Get(rowKey);
        System.out.println(get.getMaxVersions());
        Result result = HBaseHelper.get(TABLE_NAME, get);

        // Parse result
        for (Cell cell : result.rawCells()) {
            Assert.assertArrayEquals(rowKey, Bytes.copy(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
            Assert.assertArrayEquals(FAMILY_X, Bytes.copy(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
            Assert.assertArrayEquals(aBytes, Bytes.copy(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
            Assert.assertArrayEquals(vBytes2, Bytes.copy(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
        }
    }

    @Test
    public void testTimestamp() {
        final byte[] rowKey = Bytes.toBytes("r1");
        final byte[] aBytes = Bytes.toBytes("a");
        final byte[] vBytes = Bytes.toBytes("xa_value1");
        final byte[] vBytes2 = Bytes.toBytes("xa_value2");

        long ts = Long.MAX_VALUE - System.currentTimeMillis();

        // Write a record for 1st time
        Put put1 = new Put(rowKey);
        put1.addColumn(FAMILY_X, aBytes, ts, vBytes);
        HBaseHelper.put(TABLE_NAME, put1);

        // Read it
        Get get1 = new Get(rowKey);
        Result result1 = HBaseHelper.get(TABLE_NAME, get1);
        Assert.assertEquals(1, result1.rawCells().length);

        Cell cell1 = result1.getColumnLatestCell(FAMILY_X, aBytes);
        System.out.println(cell1.getTimestamp());
        System.out.println(System.currentTimeMillis());

        // 删除该记录
        Delete delete = new Delete(rowKey);
        delete.deleteColumn(FAMILY_X, aBytes);
        HBaseHelper.delete(TABLE_NAME, delete);

        // Read it again
        Get get2 = new Get(rowKey);
        Result result2 = HBaseHelper.get(TABLE_NAME, get2);
        Assert.assertEquals(0, result2.rawCells().length);

        // Write a record for 1st time
        Put put2 = new Put(rowKey);
        put2.addColumn(FAMILY_X, aBytes, ts, vBytes2);
        HBaseHelper.put(TABLE_NAME, put2);

        // Read it
        Get get3 = new Get(rowKey);
        Result result3 = HBaseHelper.get(TABLE_NAME, get3);
        Assert.assertEquals(1, result3.rawCells().length);

        Cell cell3 = result3.getColumnLatestCell(FAMILY_X, aBytes);
        System.out.println(cell3.getTimestamp());
        System.out.println(System.currentTimeMillis());
    }
}