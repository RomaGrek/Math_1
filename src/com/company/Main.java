package com.company;

import java.util.*;

/* Есть нескоько пунктов, которые нужно выполнить
1 - Считать строку символов
2 - Разбить эту строку на массив строк, где каждый элемент это или скобка, или опертор или переменная
3 - Найти вес для каждого ОПЕРАТОРА, т е тупо пройтись по всему списку этому
4 - Ищем оператор с наибольшим весом и обрабатываем его
5 - обрабатывать дальше, и должен получиться из строки словарь
6 - пройтись по славарю и правильно напечатать ответ
для тестов:
!A&!B->!(A|B)
A->!B->!C&D|!T&U&V
*/


public class Main {


    public static boolean checkSymbols(String element) {
        return !element.equals("!") && !element.equals("&") && !element.equals("(") &&
                !element.equals(")") && !element.equals("|") && !element.equals("->") &&
                !element.equals("-") && !element.equals(">");
    }

    public static void main(String[] args) {


        String inputLine;
        Scanner scanner = new Scanner(System.in);
        inputLine = scanner.nextLine();

        /* походу нужно сделать его листом, что бы можно было удалять элементы  */
        String[] stroke = inputLine.split("");  // 1 основной массив символов


        int count = 1;
        ArrayList<Integer> weightList = new ArrayList<>();     // 2 массив весов
        ArrayList<String> operatorList = new ArrayList<>();    // 3 массив операторов

        /* Расчет весов для операторов
        * !!!!!!!!! Потом вынести в отдельную функцию */
        for (int i = 0; i < stroke.length; i ++) {
            switch (stroke[i]) {
                case "0":
                    break;
                case "(":
                    count++;
                    break;
                case ")":
                    count--;
                    break;
                case "!":
                    if (checkSymbols(stroke[i + 1])) {
                        stroke[i] = "(" + stroke[i] + stroke[i + 1] + ")";
                        stroke[i + 1] = "0";
                    } else {
                        weightList.add(6 * count);
                        operatorList.add(stroke[i]);
                    }
                    break;
                case "&":
                    weightList.add(5 * count);
                    operatorList.add(stroke[i]);
                    break;
                case "|":
                    weightList.add(4 * count);
                    operatorList.add(stroke[i]);
                    break;
                case "-":
                    stroke[i] = stroke[i] + stroke[i + 1];
                    stroke[i + 1] = "0";
                    weightList.add(3 * count);
                    operatorList.add(stroke[i]);
                    break;
            }
        }

        /* процедура удаления нулей и скобок из основного списка */

        List<String> strokeList = new ArrayList<>(Arrays.asList(stroke));

        strokeList.removeIf(element -> element.equals("0"));
        strokeList.removeIf(element -> element.equals("("));
        strokeList.removeIf(element -> element.equals(")"));



        /* теперь нужно сначала определить какой именно оператор имеет такой вес
        * затем нужно как-то склеивать элемнеты (это легко)
        * можно просто отсчитывать их по порядку слева на права
        * то есть смотрим их порядковый номер в массива operatorList и затем мы отсчитываем только операторы
        * в самом stroke используя доп счетчик и делаем выборку нужного оператора */

//        stroke = strokeList.toArray(new String[strokeList.size()]);









        /* вывод списка весов операторов */
        System.out.print("weightList: { ");
        for (Integer element : weightList) {
            System.out.print(element + " ");
        }
        System.out.println("}");


        /* вывод списка всех операторов, которые нужно обработать */
        System.out.print("operotorList: { ");
        for (String operator : operatorList) {
            System.out.print(operator + " ");
        }
        System.out.println("}");


        /* вывод основного массива stroke (там где мы будем в конце делать словарь) */
        System.out.print("stroke: { ");
        for (String s : stroke) {
            System.out.print(s + " ");
        }
        System.out.println("} size: " + stroke.length);



    }
}
