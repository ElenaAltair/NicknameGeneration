import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger counterLength3 = new AtomicInteger(0);
    static AtomicInteger counterLength4 = new AtomicInteger(0);
    static AtomicInteger counterLength5 = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {

        //Для генерации 100 000 коротких слов вы использовали:
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        /*
        Пользователь может выбрать из них только те, которые соответствуют критериям «красивого» никнейма, а именно:
          - сгенерированное слово является палиндромом, т. е. читается одинаково как слева направо,
            так и справа налево, например, abba;
          - сгенерированное слово состоит из одной и той же буквы, например, aaa;
          - буквы в слове идут по возрастанию: сначала все a (при наличии), затем все b (при наличии),
            затем все c и т. д. Например, aaccc.
         */

        /*
        Вы хотите подсчитать, сколько «красивых» слов встречается среди сгенерированных длиной 3, 4, 5,
        для чего заводите три счётчика в статических полях.
        Проверка каждого критерия должна осуществляться в отдельном потоке.
         */

        // проверка строки на палиндромность
        new Thread(() -> {
            for (String str : texts) {
                if (str.contentEquals((new StringBuilder(str)).reverse())) {
                    if (str.length() == 3) {
                        counterLength3.getAndIncrement();
                    } else if (str.length() == 4) {
                        counterLength4.getAndIncrement();
                    } else if (str.length() == 5) {
                        counterLength5.getAndIncrement();
                    }
                }
            }

        }).start();

        // проверка состоит ли строка из одинаковых символов
        new Thread(() -> {
            for (String str : texts) {
                char c = str.charAt(0);
                int count = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == c) {
                        count++;
                    } else {
                        count = 0;
                    }
                }
                if (count == str.length()) {
                    if (str.length() == 3) {
                        counterLength3.getAndIncrement();
                    } else if (str.length() == 4) {
                        counterLength4.getAndIncrement();
                    } else if (str.length() == 5) {
                        counterLength5.getAndIncrement();
                    }
                }
            }

        }).start();


        // проверка идут ли буквы в строке в алфавитном порядке
        new Thread(() -> {
            for (String str : texts) {
                String sorted = str.chars()
                        .sorted()
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                if (str.equals(sorted)) {
                    if (str.length() == 3) {
                        counterLength3.getAndIncrement();
                    } else if (str.length() == 4) {
                        counterLength4.getAndIncrement();
                    } else if (str.length() == 5) {
                        counterLength5.getAndIncrement();
                    }
                }
            }
        }).start();

        System.out.println("Красивых слов с длиной 3: " + counterLength3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + counterLength4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + counterLength5 + " шт");
    }

    //Предположим, что для реализации сервиса по подбору никнеймов
    // вы разработали генератор случайного текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
