package com.lendou.file.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Program {

    private static String url = "https://d1opms6zj7jotq.cloudfront.net/idea/ideaIU-15.0.4.tar.gz";
    // private static String url = "http://a.hiphotos.baidu.com/zhidao/pic/item/a044ad345982b2b7e0e2328b30adcbef76099b7b.jpg";
    // private static String url = "https://upload.wikimedia.org/wikipedia/commons/3/3f/Fronalpstock_big.jpg";
    // private static String url = "https://kssv-w-yz01.kuaipandata.com/kss_node_web/XiaoMi/download_file?attachment=0&fn=6YCa6K+d5b2V6Z+zQDEzMjQxODQ3MzczKDEzMjQxODQ3MzczKV8yMDE1MTIyNDE3NTQ1Ny5tcDM=&x_meta=poDIhNL18q51CwttLAg4LLHv-FqrSaiNX8XPaj3vDCRz0DBc3VvuHgf_y33VpJDbFjNF52jqJqh3KlYnwLT1L1m9PqTKADIzwYOpGbEXLp2ROt35Xjn6zKXIrLK6RWDG3Mzzv_nP_Z3NX2Z0dH1WDSrAwHmdhJ6xNFye_H1teI-NTnBzdn_y5OwNOslELiYIRQc4z6DU1arfrtC3gsNOlFVvmsa6GOaRRuWoDPBuMdd4td1wZa9Qd8qdCk8qyoKcrOOspzKbhgS_C0fr2HBrQp1DHAgbi9Qy7a9raYPD7QoIFl0WWOgkB_yZLLaTNmvqez-BLJdghUwG4GuHkWYzpxUO5BwMVcGAL4QgkgXLZ3d2oJKqp8UFeOIrhjQLxSBSl88Pe6pSsp8pfgKoVuonkKwifylqipn6_IT433J6XCpv0pW-a5OnyUvfE8VBVFRQYGKM5MObE32qZYIRRsNb3XxxNwBkKrpCtGNeUwe_L0ObrzmX-rhCeqpVxGXPMFLGo1S51mNSch1PMMMnRnOdZMO-MrhUhmr3vW4acJ_CFrcMI-sLtwuUmSrgB1Uaxz2k_rajcUXT2pRE3RMpsbkvHS3PjEzyTJqUfFtjI7kMrx1CINFGajeWU_nwRjzRwDg%3D&x_expires=1457609520&signature=6x0s5cz1h-OMJSTREuGJOvFLrtM=&ct=audio%2Fmpeg";
    // private static final String url = "http://42.62.61.37/kss_node_web/XiaoMi/download_file?attachment=0&fn=d2ViX2ZpbGVfbmFtZQ==&x_meta=poDIhNL18q51CwttLAg4LLG4-AepHqaFU5rIPWa0UiEihDEL2Q7oHlT5wH7RoZKBFjNF52jqJqh3KlYnwLT1L1m9PqTKADIzloP-TeFAKciXOIn6WW3_yPDO_-G9QmWe3cb4vK_Mr53KDWZxcn5SDCrAwHmdhJ6xNFye_H1teI-NTnBzdn_y5fAQJshdMGRBCy0wy4iLkuGfvdGsg85FqlNcl8m-RO_CS-qsMPZUKNMxtoN7Zvlbe56kW18a9rbQrcGU_z29tUS9RASV_FlCH7JcBjl2gsUP55Bucb3CuWsgOD1lFoQ4cY_5VtGeDkr8SD6KHZlgm1NZ3w2P9FVLlmEC1j8AXMyJMok_mgWzd3B21cm1-c8aI74n2D4aiwNniIETe5Jv07QhAACPNqM81LABMktZ6IL7rrWMwWs2FydN3YuZGImm9H3USO81TmUtUgHj7rGEYBynMp5vOpwigx03ZkQZZeIEuGAdGnfVWTXytk2Ji-BRLfFXkVHoORqcvQGltm5db20ef8A4RSKdZJKhMbgFhm_rtHhya4rZUO9EduQFpQ3OzSuyBBgjiw%3D%3D&x_expires=1457680333&signature=pn0opMYhki5Rr5yIQnd-DAgGD9s=&ct=application%2Foctet-stream";

    public static void main(String[] args) throws IOException {
        download(getSize());
    }

    public static void download(int fileSize) throws IOException {
        FileContext context = FileContext.createFileInfo(url, "testFileId", fileSize);

        Stat.start();
        FileDownloadManager.getInstance().download(context);

        if (context.isInMemory()) {
            FileUtils.writeByteArrayToFile(new File("/data/soft/transfer/files/" + System.currentTimeMillis()), context.getContent());
        }
    }

    public static int getSize() {

        int size = FileSizeHelper.getFileSize(url);
        System.out.println("fileSize: " + size);
        return size;
    }
}
