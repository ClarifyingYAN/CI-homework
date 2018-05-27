package com.clary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int[][] d = {
                {-1,3,1,2},
                {3,-1,5,4},
                {1,5,-1,2},
                {2,4,2,-1}
        };

        int m=3;

        ACO aco = new ACO(m, d);
        aco.run();
        List list = aco.getSolution();
        double c[] = aco.getC();
        for (int i=0; i<list.size();i++) {
            System.out.print(list.get(i) + " : ");
            System.out.println(c[i]);
        }
    }
}
