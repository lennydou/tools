package com.lendou.hbase;

import org.apache.commons.lang.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.List;

/**
 * HBase的帮助类, 用于封装访问hbase的基本方法
 */
public class HBaseHelper {
    private static Configuration conf = null;

    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
    }

    /**
     * 创建HBase表
     *
     * @param tableName 待创建的表名
     * @param families  待创建表的family名称
     */
    public static void createTable(String tableName, String[] families) {
        Validate.notNull(tableName);
        Validate.notNull(families);

        Connection conn = null;
        HBaseAdmin admin = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin) conn.getAdmin();
            if (admin.tableExists(tableName)) {
                if (!admin.isTableEnabled(tableName)) {
                    admin.enableTable(tableName);
                }
            } else {
                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
                for (int i = 0; i < families.length; i++) {
                    tableDesc.addFamily(new HColumnDescriptor(families[i]));
                }
                admin.createTable(tableDesc);
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

    /**
     * 删除HBase表
     *
     * @param tableName 待删除的HBase表名称
     */
    public static void deleteTable(String tableName) {
        Validate.notEmpty(tableName);

        Connection conn = null;
        HBaseAdmin admin = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin) conn.getAdmin();
            if (!admin.tableExists(tableName)) {
                return;
            }

            if (admin.isTableEnabled(tableName)) {
                admin.disableTable(tableName);
            }

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

    /**
     * 写入一个数据
     *
     * @param tableName 待写入数据的表名称
     * @param put       要写入的部分
     */
    public static void put(String tableName, Put put) {
        Validate.notEmpty(tableName);
        Validate.notNull(put);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            table.put(put);

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

    /**
     * 批量写入一组数据
     *
     * @param tableName 待写入数据的表名称
     * @param puts      批量写入的数据
     */
    public static void put(String tableName, List<Put> puts) {
        Validate.notEmpty(tableName);
        Validate.notNull(puts);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            table.put(puts);
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

    /**
     * 删除一个数据
     *
     * @param tableName 待删除数据的表名称
     * @param delete    带删除的数据
     */
    public static void delete(String tableName, Delete delete) {
        Validate.notEmpty(tableName);
        Validate.notNull(delete);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            table.delete(delete);
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

    /**
     * 批量删除一组数据
     *
     * @param tableName 待删除数据的表名称
     * @param deletes   待删除的数据
     */
    public static void delete(String tableName, List<Delete> deletes) {
        Validate.notEmpty(tableName);
        Validate.notNull(deletes);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            table.delete(deletes);
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

    /**
     * 查询一个数据
     *
     * @param tableName 待查询数据的表名称
     * @param get       待查询的数据
     * @return          查询结果
     */
    public static Result get(String tableName, Get get) {
        Validate.notEmpty(tableName);
        Validate.notNull(get);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            return table.get(get);
//
//            Get get = new Get(Bytes.toBytes(rowKey));
//            Result rs = table.get(get);
//            for (Cell cell : rs.rawCells()) {
//                String rowValue = Bytes.toString(Bytes.copy(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
//                String familyValue = Bytes.toString(Bytes.copy(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
//                String qualifierValue = Bytes.toString(Bytes.copy(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
//                String value = Bytes.toString(Bytes.copy(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
//
//                System.out.println(String.format("%s\t%s:%s\t%s", rowValue, familyValue, qualifierValue, value));
//            }

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

        return null;
    }

    /**
     * 批量查询一组数据
     *
     * @param tableName 待查询数据的表名称
     * @param gets      需要查询的数据
     * @return          查询结果
     */
    public static Result[] get(String tableName, List<Get> gets) {
        Validate.notEmpty(tableName);
        Validate.notNull(gets);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            return table.get(gets);
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

        return null;
    }

    /**
     * 扫描一组数据
     *
     * @param tableName 待扫描数据的表名称
     * @param scan      扫描数据所使用的scan结构
     * @return          扫描结果
     */
    public static ResultScanner scan(String tableName, Scan scan) {
        Validate.notEmpty(tableName);
        Validate.notNull(scan);

        Connection conn = null;
        HTable table = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            table = (HTable) conn.getTable(TableName.valueOf(tableName));
            return table.getScanner(scan);
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

        return null;
    }
}
