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

(A&B)&C
*/


public class Main {


    /* метод для проверки на то, что символ НЕ является операндом или скобкой  */
    public static boolean checkSymbols(String element) {
        return !element.equals("!") && !element.equals("&") && !element.equals("(") &&
                !element.equals(")") && !element.equals("|") && !element.equals("->") &&
                !element.equals("-") && !element.equals(">");
    }

    /* метод для проверки на то, является ли это оператором или нет */
    public static boolean checkOperator(String element) {
        return element.equals("!") || element.equals("->") || element.equals("|") || element.equals("&")
                || element.equals("_");
    }

    /* метод получения индекса максмимального веса оператора,
    * модифицируев выборкой макс веса, даже если несколько макимальных */
    public static int indexOfMaxElement(ArrayList<Integer> list, ArrayList<String> listOperators) {
        Integer[] array =  list.toArray(new Integer[list.size()]);
        int indexMax = 0;
        int countMaxElements = 0;
        int maxElement = -1;
        for (int i = 0; i < list.size(); i++) {
            if (array[i] >= maxElement) {
                maxElement = array[i];
                indexMax = i;
                countMaxElements++;
            }
        }



        // что то тут не так...
        if (countMaxElements > 1) {
            String operator = listOperators.get(indexMax);
            if (operator.equals("&") || operator.equals("|")) {
                indexMax = 0;
                maxElement = -1;
                for (int i = 0; i < list.size(); i++) {
                    if (array[i] > maxElement) {
                        maxElement = array[i];
                        indexMax = i;
                    }
                }
            }
        }
        return indexMax;


    }


    public static void main(String[] args) {

        /* считываем строку */
        String inputLine;
        Scanner scanner = new Scanner(System.in);
        inputLine = scanner.nextLine();

        /* походу нужно сделать его листом, что бы можно было удалять элементы  */
        String[] stroke = inputLine.split("");  // 1 основной массив символов

        /* соеднинеие элементов из нескольких символов */

        int count = 1;
        ArrayList<Integer> weightList = new ArrayList<>();     // 2 массив весов
        ArrayList<String> operatorList = new ArrayList<>();    // 3 массив операторов



        /* проблема с первым тестом в отрицании !(a|b) - после удлаения все ок */
        int indexGeneralElement = -1;
        boolean flagGeneralElement = false;
        for (int i = 0; i < stroke.length; i++) {
            if (!stroke[i].equals("!") && !stroke[i].equals("&") && !stroke[i].equals("|") && !stroke[i].equals("-")
            && !stroke[i].equals(">") && !stroke[i].equals("(") && !stroke[i].equals(")")) {

                if (!flagGeneralElement) {
                    indexGeneralElement = i;
                    flagGeneralElement = true;
                }else {
                    stroke[indexGeneralElement] = stroke[indexGeneralElement] + stroke[i];
                    stroke[i] = "_";
                }
            }else {
                flagGeneralElement = false;
            }
        }


        /* Расчет весов для операторов
        * !!!!!!!!! Потом можно вынести в отдельную функцию*/
        for (int i = 0; i < stroke.length; i ++) {
            switch (stroke[i]) {
                case "_":
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
                        stroke[i + 1] = "_";
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
                    stroke[i + 1] = "_";
                    weightList.add(3 * count);
                    operatorList.add(stroke[i]);
                    break;
            }
        }

        List<String> strokeList = new ArrayList<>(Arrays.asList(stroke));


        strokeList.removeIf(element -> element.equals("_"));
        strokeList.removeIf(element -> element.equals("("));
        strokeList.removeIf(element -> element.equals(")"));


        /* теперь нужно сначала определить какой именно оператор имеет такой вес
        * затем нужно как-то склеивать элемнеты (это легко)
        * можно просто отсчитывать их по порядку слева на права
        * то есть смотрим их порядковый номер в массива operatorList и затем мы отсчитываем только операторы
        * в самом stroke используя доп счетчик и делаем выборку нужного оператора */

        stroke = strokeList.toArray(new String[strokeList.size()]);

        int sumWeight = weightList.size();   // колчиество весов в начале
        for (int i = 0; i < sumWeight; i++) {

            // нужна проверка на то, что есть ли несколько операторов одного веса:


            int maxIndex = indexOfMaxElement(weightList, operatorList); // инедкс наибольшего веса в массиве weightList

            // ------------------------------------------------------------------- конец проверки на то, что есть нексолько операторов одного веса

            String operator = operatorList.get(maxIndex);
            
            ////// ----- в четвертой итерации до этого момента все хорошо
            int countOperatorNumber = -1;           // порялковый номер текцщего оператора в главном массиве
            for (int j = 0; j < stroke.length; j++) {
                if (checkOperator(stroke[j])) {
                    countOperatorNumber++;
                    if (stroke[j].equals(operator) && countOperatorNumber == maxIndex) {
                        if (stroke[j].equals("!")) {
                            stroke[j] = "(" + stroke[j] + stroke[j + 1] + ")";
                            stroke[j + 1] = "_";
//                            System.out.print("обработка отрицания: ");
//                            System.out.println(stroke[j]);

                        }else {
//                            System.out.print("обработка других опертаоров ");
                            stroke[j] = "(" + stroke[j] + "," + stroke[j - 1] + "," + stroke[j + 1] + ")";
                            stroke[j - 1] ="_";
                            stroke[j + 1] = "_";
//                            System.out.println(stroke[j]);

                        }
                    }

                }
            }
            // очищаем второстепенные массиы
            weightList.remove(maxIndex);
            operatorList.remove(maxIndex);


            // нужно отчистить

            List<String> strokeListo = new ArrayList<>(Arrays.asList(stroke));
            strokeListo.removeIf(element -> element.equals("_"));
            stroke = strokeListo.toArray(new String[0]);


        }


//
//        /* вывод списка весов операторов */
//        System.out.print("weightList: { ");
//        for (Integer element : weightList) {
//            System.out.print(element + " ");
//        }
//        System.out.println("}");
//
//
//        /* вывод списка всех операторов, которые нужно обработать */
//        System.out.print("operotorList: { ");
//        for (String operator : operatorList) {
//            System.out.print(operator + " ");
//        }
//        System.out.println("}");


        /* вывод основного массива stroke (там где мы будем в конце делать словарь) */
//        System.out.print("Ответ:  ");
        for (String s : stroke) {
            System.out.print(s + " ");
        }
    }
}
