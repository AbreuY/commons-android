package cn.wandersnail.commons.observer;

/**
 * 观察者
 * <p>
 * date: 2019/8/3 13:15
 * author: zengfansheng
 */
public interface Observer {
    /**
     * 数据变化
     */
    @Observe
    default void onChanged(Object o) {}
}
