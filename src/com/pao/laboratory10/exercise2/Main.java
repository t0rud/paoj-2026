package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;

        int N = scanner.nextInt();
        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            lista.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String command = scanner.next();

            switch (command) {
                case "UNIQUE_IDS":
                    Set<Integer> uniqueIds = new LinkedHashSet<>();
                    for (Tranzactie t : lista) {
                        uniqueIds.add(t.getId());
                    }
                    System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds);
                    break;

                case "MONTHLY_REPORT":
                    Map<String, double[]> monthly = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String month = t.getData().substring(0, 7);
                        monthly.putIfAbsent(month, new double[]{0.0, 0.0});

                        if (t.getTip() == TipTranzactie.CREDIT) {
                            monthly.get(month)[0] += t.getSuma();
                        } else {
                            monthly.get(month)[1] += t.getSuma();
                        }
                    }

                    for (Map.Entry<String, double[]> entry : monthly.entrySet()) {
                        System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON\n",
                                entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                    }
                    break;

                case "TOP":
                    int n = scanner.nextInt();
                    List<Tranzactie> copy = new ArrayList<>(lista);
                    copy.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                    System.out.println("Top " + n + ":");
                    for (int i = 0; i < n && i < copy.size(); i++) {
                        System.out.println(copy.get(i).toString());
                    }
                    break;

                case "SORT_ASC":
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    printList(lista);
                    break;

                case "SORT_DESC":
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    printList(lista);
                    break;

                case "REVERSE":
                    Collections.reverse(lista);
                    printList(lista);
                    break;

                case "MIN_MAX":
                    if (!lista.isEmpty()) {
                        Tranzactie min = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                        Tranzactie max = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                        System.out.println("MIN: " + min.toString());
                        System.out.println("MAX: " + max.toString());
                    }
                    break;

                case "CME_DEMO":
                    try {
                        for (Tranzactie t : lista) {
                            lista.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }

    private static void printList(List<Tranzactie> list) {
        for (Tranzactie t : list) {
            System.out.println(t.toString());
        }
    }
}