 BLM230-HammingSimulator
Hamming SEC-DED Simulator for BLM230 Dönem Projesi
Hamming SEC-DED Simulator

BLM230 Dönem Projesi

Öğrenci: Feride Saygı Yalçınkaya-22360859064



 📜 Proje Açıklaması

Swing tabanlı bir uygulama ile m-bit uzunluğundaki ikili diziyi alıp Hamming SEC-DED (Single Error Correction, Double Error Detection) kod kelimesine dönüştüren, tek hatayı düzelten ve çift hatayı tespit eden bir simülatör geliştirdim.

 ⚙️ Özellikler

* Veri Girişi:İstediğiniz uzunlukta (`m`) `0`/`1` dizisi.
* Encode: Hamming parite bitlerini (r adet + overall) otomatik hesaplayarak m+r+1 bitlik kod kelimesi üretir.
* Error Injection: Spinner ile seçilen pozisyondaki biti bozar; hata uygulanmış bit kırmızı kenarlıklı gösterilir.
* Decode:

  * Tek Hata: Sendrom ile hatayı tespit edip düzeltir, düzeltme pozisyonunu ve orijinal veriyi yeşil kenarlıklı gösterir.
  * Çift Hata:"Double Error Detected! Cannot correct." uyarısı verir.
 
  Dosya Yapısı

- `README.md` – Proje açıklamaları ve yönergeler.
- `src/mimari/` – Java kaynak kodları (HammingCode.java, Main.java, MainUI.java).
- `.gitignore` – Proje için yoksayılacak dosyalar.

🛠️ Kurulum & Çalıştırma

1. Java 8 veya üstü yüklü olmalı.
2. Terminal veya komut satırını açıp, projenin bulunduğu klasöre gidin. Örnek yollar:
  Windows: cd C:\Users\KullaniciAdiniz\Projeler\BLM230-HammingSimulator
   
3. Derleme:
   javac mimari/*.java
   
4. Çalıştırma:
  a)Depoyu klonlayın:
   ```bash
   git clone https://github.com/ferideyalcinkaya/BLM230-HammingSimulator.git
  b)src/mimari içindeki .java dosyalarını bir Java IDE’sine (Eclipse, IntelliJ, VS Code) aktarın.

  c)Projeyi derleyip çalıştırın:
   Simülatörü (GUI’yi) çalıştırmak için iki yöntem kullanabilirsiniz: Komut satırıyla (CMD) veya bir IDE (Eclipse/IntelliJ) üzerinden. 
   
 📋 Kullanım

1. Load to Memory: `Veri (binary)` alanına ikili diziyi yazıp tıklayın.
2. Encode: Parite bitleri eklenmiş kod kelimesini gösterir.
3. Inject Error: Spinner ile pozisyon seçip hatayı uygulayın.
4. Decode: Tek hatayı düzeltir veya çift hata durumunda uyarı verir.

 🔎 Test Örnekleri

 Tek Hata:
  Girdi: 10110010
  Encode → 1010011100100
  Inject(pos=5) → 1010111100100
  Decode → Detected Error Position: 5
           Decoded Data: 10110010
  

  Çift Hata:
  Girdi: 10110010
  Encode → 1010011100100
  Inject(pos=3) → 1000011100100
  Inject(pos=8) → 1000011100000
  Decode → Double Error Detected! Cannot correct.
  

🌐 Linkler

GitHub Repo: [https://github.com/KullaniciAdin/BLM230-HammingSimulator](https://https://github.com/ferideyalcinkaya/BLM230-HammingSimulator/tree/main/src/mimari)
Demo Video: [https://youtu.be/VideoLinkin](https://www.youtube.com/watch?v=tKnc9bJZdtg)

Proje raporu:(https://github.com/ferideyalcinkaya/BLM230-HammingSimulator/blob/main/BLM230_Proje_FerideSayg%C4%B1Yalcinkaya_22360859064.pdf)

