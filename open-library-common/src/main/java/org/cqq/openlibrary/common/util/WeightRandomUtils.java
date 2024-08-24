package org.cqq.openlibrary.common.util;

import java.util.Random;

/**
 * Random by weight util
 *
 * @author CongQingquan
 */
public class WeightRandomUtils {
    
    private WeightRandomUtils() {}
    
    
    public static void main(String[] args) {
        Random random = new Random();
        for (;true;) {
            double v = random.nextGaussian();
            if (v < -1.5D) {
                System.err.println(v);
            }
        }
    }
    
    // 存在不公平性: 越排在前面的权重，命中的概率越大
//    public static <K extends Number, V> K random(LinkedHashMap<K, V> sourceWeightMap, boolean fair) {
//
//        if (MapUtils.isEmpty(sourceWeightMap)) {
//            return null;
//        }
//
//        Collection<Map.Entry<K, V>> entries = sourceWeightMap.entrySet();
//        if (fair) {
//            ArrayList<Map.Entry<K, V>> tempEntries = new ArrayList<>(entries);
//            Collections.shuffle(tempEntries);
//            entries = tempEntries;
//        }
//
//        TreeMap<Double, K> weightMap = new TreeMap<>();
//
//
//        double randomWeight = weight * Math.random();
//
//        return weightMap
//                .tailMap(randomWeight, false)
//                .firstEntry()
//                .getValue();
//    }
    
//    java.util.Random里的nextGaussian()，生成的数值符合均值为0方差为1的高斯/正态分布，即符合标准正态分布n(0,1)
//
//    产生0到100范围的随机数，覆盖了99%的区间
//
//    int requestSize = (int)((rand.nextGaussian()+3)*100/6);
//
//    产生a到b范围的随机数，覆盖了99%的区间
//
//    int requestSize = (int)((rand.nextGaussian()+3)*(b-a)/6+a);

// gaussian * σ + μ
// 1. nextGaussian 方法生成服从高斯分布的随机数
// 2. σ 是标准差
// 3. μ 是平均值
// 4. μ 和 σ 的取值决定了正态分布的形状和位置。当 μ 和 σ 发生变化时，分布也会相应地发生变化。增大 μ 会使分布向右平移，增大 σ 会使分布变得更平缓。

//    List<WeightRandom.WeightObj<Integer>> weightListNumber = new ArrayList<>();
//        for (int i = 0; i < weightList.size(); i++) {
//        weightListNumber.add(new WeightRandom.WeightObj<>(i,weightList.get(i).getWeight()));
//    }
//    WeightRandom<Integer> wr = RandomUtil.weightRandom(weightListNumber);
//    WeightRandomDTO weightRandomDTO = weightList.get(wr.next());
//    Random random = new Random();
//    // gaussian * σ + μ
//    int requestSize = (int)
//            (
//                    (random.nextGaussian()+3) //
//                            *
//                            (weightRandomDTO.getMax()-weightRandomDTO.getMin()) / 6 //
//                            +
//                            weightRandomDTO.getMin() //
//            );
//        return requestSize;
    
//    public static void main(String[] args) {
////        String random = random(Map.of(
////                "1", 1,
////                "5", 5,
////                "10", 10
////        ));
//        for (int i = 0; i < 10; i++) {
//            System.out.println(random(Map.of(
//                    "1", 1,
//                    "5", 5,
//                    "10", 10
//            )));
//        }
//    }
}