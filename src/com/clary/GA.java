package com.clary;

import java.util.*;

public class GA {
    private final double matingRate;
    private final double variationRate;
    private final int scale; // 规模
    private final int cityNum; // 城市数目

    private int[][] d; // distance
    private List<List<Integer>> chromosomes = new ArrayList<>();
    private List<Ant> ants = new ArrayList<>();
    private int[] c; // 路径长度
    private int best = -1;

    public GA(int[][] d, int scale, double matingRate, double variationRate) {
        this.d = d;
        this.scale = scale;
        this.matingRate = matingRate;
        this.variationRate = variationRate;
        this.cityNum = d.length;
        c = new int[scale];
    }

    /**
     * 运行主流程
     *
     */
    public void run() {
        // 初始化
        initChromosome();

        // 评估
        eval();

        // 选择
        select();

        // 交配
    }

    /**
     * 初始化染色体
     *
     */
    private void initChromosome() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < cityNum; i++) {
            arr.add(i);
        }
        for (int i=0; i<scale; ++i) {
            Collections.shuffle(arr);
            List<Integer> tmpList = new ArrayList<>();
            tmpList.addAll(arr);
            tmpList.add(arr.get(0));
            chromosomes.add(tmpList);
        }
    }

    /**
     * 适应值评价
     *
     */
    private void eval() {
        for (int i = 0; i < scale; i++) {
            c[i] = 0;
            for (int j = 0; j < chromosomes.get(i).size()-1; j++) {
                c[i] += d[chromosomes.get(i).get(j)][chromosomes.get(i).get(j+1)];
            }
        }

        int tmp = Integer.MAX_VALUE;
        for (int i = 0; i < scale; i++) {
            if (c[i] < tmp) {
                best = i;
                tmp = c[i];
            }
        }
    }

    /**
     * 选择
     *
     */
    private void select() {
        int randSelectChromosomes[] = new int[scale];
        for (int i = 0; i < randSelectChromosomes.length; i++) {
            randSelectChromosomes[i] = rouletteWheelSelection();
        }

        List<List<Integer>> tmpChromosomes = new ArrayList<>();
        for (int i = 0; i < scale; i++) {
            List<Integer> arr = new ArrayList<>();
            arr.addAll(chromosomes.get(randSelectChromosomes[i]));
            tmpChromosomes.add(arr);
        }

        chromosomes.clear();
        chromosomes.addAll(tmpChromosomes);

    }

    /**
     * 轮盘赌法则
     * @return 选取的染色体
     */
    private int rouletteWheelSelection() {
        double sum = 0;
        for (int i = 0; i < scale; i++) {
            sum += 1.0/c[i];
        }

        double rand = Math.random()*sum;
        sum = 0;
        for (int i = 0; i < scale; i++) {
            sum += 1.0/c[i];
            if (rand <= sum)
                return i;
        }

        return -1;
    }

    /**
     * 交配
     *
     */
    private void mating() {
        List<List<Integer>> tmpChromosomes = new ArrayList<>();
        for (int i = 0; i < scale; i++) {
            if (Math.random() < matingRate)
                tmpChromosomes.add(chromosomes.get(i));
        }

        for (int i = 0; i < tmpChromosomes.size()/2; i++) {
            Random rand = new Random();
            int num = rand.nextInt(cityNum-1);

            for (int j = num+1; j < cityNum; j++) {
                int tmp;
                tmp = tmpChromosomes.get(2*i).get(j);
                tmpChromosomes.get(2*i).set(j, tmpChromosomes.get(2*i+1).get(j));
                tmpChromosomes.get(2*i+1).set(j, tmp);
            }
        }

        // TODO: to be continued
    }

    public static void main(String[] args) {
        int[][] d = {
                {-1,3,1,2},
                {3,-1,5,4},
                {1,5,-1,2},
                {2,4,2,-1}
        };
        GA g = new GA(d, 3, 0.88, 0.1);
        g.run();

        g.select();
    }
    
}
