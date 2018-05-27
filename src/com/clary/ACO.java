package com.clary;

import java.util.*;

public class ACO {
    final private double alpha = 1.0;
    final private double beta = 2.0;
    final private double rho = 0.5;
    final private int m; // 蚂蚁数
    final private double tauZero;
    final private int cityCnt;

    private int[][] d; // distance
    private double[][] pheromone; // 信息素
    private List<Ant> ants = new ArrayList<>();
    private List<List<Integer>> solution = new ArrayList<>();
    private double[] c; // 路径长度

    public ACO(int m, int[][] d) {
        this.d = d;
        this.m=m;
        cityCnt = d.length;
        c = new double[m];

        tauZero = m/getPathByGreedy();

        // 信息素初始化
        pheromone = new double[cityCnt][cityCnt];
        for (int i=0; i<d.length; ++i)
            for (int j=0; j<d.length;j++)
                if (i!=j)
                    pheromone[i][j] = tauZero;
    }

    /**
     * 开始执行 ACO
     */
    public void run() {
        // 蚂蚁初始化
        int[] initCities = Util.randomArray(0, cityCnt-1, m);

        for (int i=0; i<m; i++) {
            Ant ant = new Ant(initCities[i], cityCnt);
            ants.add(ant);
        }

        int z=1;
        while (z<cityCnt-1) {
            for (int i=0; i<m; ++i) {
                Map map = calculateProbabilties(ants.get(i).getVisitedCity(),ants.get(i).getRestCity());
                int city = rouletteWheelSelection(map);
                ants.get(i).visitCity(city);
            }

            ++z;
        }

        for (int i=0; i<m;i++) {
            int city = ants.get(i).getRestCity().get(0);
            ants.get(i).visitCity(city);
        }

        // 得出结果
        for (int i=0; i<m; ++i) {
            solution.add(ants.get(i).getVisitedCity());
        }

        // 计算路径长度
        int t=0;
        for (List l:solution) {
            double sum=0;
            int tmp=-1;
            for (int j=0; j<l.size();++j) {
                if (j!=0) {
                    sum+=d[tmp][(int) l.get(j)];
                }
                tmp=(int) l.get(j);
            }
            sum+=d[(int) l.get(l.size()-1)][(int) l.get(0)];
            c[t] = sum;
            t++;
        }

        // 更新信息素
        refreshPheromone();
    }

    /**
     * 计算各个访问对象的概率
     *
     * @param visitedCities 已走过的城市
     * @param restCities 未访问的城市
     * @return 概率数组
     */
    private HashMap<Integer, Double> calculateProbabilties(List<Integer> visitedCities, List<Integer> restCities) {

        int now = visitedCities.get(visitedCities.size()-1);

        double[] a = new double[restCities.size()];
        int i=0;
        double sum=0.0;
        HashMap<Integer, Double> result = new HashMap<>();
        Iterator iter = restCities.iterator();

        while (iter.hasNext()) {
            int j = (int) iter.next();
            double x = Math.pow(pheromone[now][j], alpha);
            double y = 1.0/Math.pow(d[now][j], beta);
            a[i] = x * y;
            sum+=a[i];
            ++i;
        }

        iter = restCities.iterator();
        i=0;
        while (iter.hasNext()) {
            int j = (int) iter.next();
            result.put(j, a[i]/sum);
            ++i;
        }

        return result;
    }

    public List<List<Integer>> getSolution() {
        return solution;
    }

    public double[] getC() {
        return c;
    }

    /**
     * 轮盘赌法则
     * @param posibilities 概率
     * @return 选取的城市
     */
    private int rouletteWheelSelection(Map<Integer, Double> posibilities) {
        double sum=0.0;
        Iterator iter = posibilities.entrySet().iterator();
        double rand = Math.random();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            sum+=(double) entry.getValue();
            if (rand<sum) {
                return (int) entry.getKey();
            }
        }

        return -1;
    }

    /**
     * 贪婪构造得到的路径长度
     * @return 路径长度
     */
    private double getPathByGreedy() {
        int i,j,k,l;
        int[] S = new int[4];
        double sum = 0;
        int Dtemp;
        int flag;
        i = 1;
        S[0] = 0;
        do{
            k = 1;Dtemp = 10000;
            j=0;
            do{
                l = 0;flag = 0;
                do{
                    if(S[l] == k){
                        flag = 1;
                        break;
                    }else
                        l++;
                }while(l < i);
                if(flag == 0&&d[k][S[i - 1]] < Dtemp){
                    j = k;
                    Dtemp = d[k][S[i - 1]];
                }
                k++;
            }while(k < 4);
            S[i] = j;
            i++;
            sum += Dtemp;
        }while(i < 4);
        sum += d[0][j];

        return sum;
    }

    /**
     * 更新信息素
     */
    private void refreshPheromone() {
        pheromone = new double[cityCnt][cityCnt];
        for (int i=0; i<d.length; ++i)
            for (int j=0; j<d.length;j++)
                if (i!=j)
                    pheromone[i][j] = (1-rho) * pheromone[i][j] + calculateSumTauK(i, j);
    }

    /**
     * 计算k只蚂蚁释放信息量的和
     * @param i 城市i
     * @param j 城市j
     * @return k只蚂蚁释放信息量的和
     */
    private double calculateSumTauK(int i, int j) {
        double sum=0;
        for (int k=0; k<m; ++k) {
            List<Integer> sol = ants.get(k).getVisitedCity();

            if (sol.get(sol.size()-1)==i && sol.get(0)==j) {
                sum+=1.0/c[k];
                break;
            }
            for (int n=0;n<sol.size()-1;++n) {
                if (sol.get(n)==i && sol.get(n+1)==j)
                    sum+=1.0/c[k];
            }
        }

        return sum;
    }
}
