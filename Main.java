

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Örnek 8-bit veri
        int[] data = {1, 0, 1, 1, 0, 0, 1, 0};
        System.out.println("Original data: " + Arrays.toString(data));

        // HammingCode nesnesini oluştur (m = 8)
        HammingCode hc = new HammingCode(data.length);

        // Kodlama (encode)
        int[] coded = hc.encode(data);
        System.out.println("Encoded (" + coded.length + " bits): " + Arrays.toString(coded));

        // 3. biti boz (1-tabanlı pozisyon = 3 -> 0-tabanlı index = 2)
        coded[2] ^= 1;
        System.out.println("After injecting single error at position 3: " + Arrays.toString(coded));

        // Hata pozisyonunu bul
        int errorPos = hc.getErrorPosition(coded);
        System.out.println("Detected error position (1-based): " + errorPos);

        // Double error durumu mu?
        boolean doubleErr = hc.isDoubleError(coded);
        System.out.println("Is double error? " + doubleErr);

        // Decode (tek hata varsa düzelt ve veri çıkar; çift hataysa null döner)
        int[] decoded = hc.decode(coded);
        if (decoded != null) {
            System.out.println("Decoded data: " + Arrays.toString(decoded));
        } else {
            System.out.println("Double error detected! Cannot decode.");
        }

        // Şimdi Çift hata testine geçelim:
        // Aynı pozisyona tekrar hata ekleyip farklı bir pozisyona da hata ekleyelim
        // (Önceki tek hatayı düzeltmek için kodu tekrar eski haline getirelim)
        coded[2] ^= 1; // 3. biti eski haline getir
        // Başka bir pozisyonu boz
        coded[4] ^= 1; // 1-tabanlı pozisyon 5 -> 0-tabanlı index = 4
        System.out.println("\nAfter injecting two errors: " + Arrays.toString(coded));

        errorPos = hc.getErrorPosition(coded);
        System.out.println("Detected syndrome (as position): " + errorPos);

        doubleErr = hc.isDoubleError(coded);
        System.out.println("Is double error? " + doubleErr);

        decoded = hc.decode(coded);
        if (decoded != null) {
            System.out.println("Decoded data: " + Arrays.toString(decoded));
        } else {
            System.out.println("Double error detected! Cannot decode.");
        }
    }
}
