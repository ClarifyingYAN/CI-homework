package com.clary;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[][] d = {
                {-1,3,1,2},
                {3,-1,5,4},
                {1,5,-1,2},
                {2,4,2,-1}
        };
        int m=3;
        double alpha=1,beta=2,rho=0.5;
        System.out.println(String.format("初始化参数为：α=%.1f,β=%.1f,⍴=%.1f，采用%d只蚂蚁", alpha, beta, rho, m));

        System.out.println("图的二维矩阵为: ");
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                System.out.print(String.format("%4d", d[i][j]));
            }
            System.out.println();
        }

        ACO aco = new ACO(m, d, alpha, beta, rho);
        aco.run();
        List<List<Integer>> list = aco.getSolution();
        double c[] = aco.getC();
        for (int i=0; i<list.size();i++) {
            System.out.print("第" + (i+1) + "只蚂蚁的选择为 ");
            for (int j = 0; j < list.get(i).size(); j++) {
                if (j!=0)
                    System.out.print("->");
                System.out.print(list.get(i).get(j));
            }

            System.out.println("  总路径长度为："+c[i]);
        }
    }
}
