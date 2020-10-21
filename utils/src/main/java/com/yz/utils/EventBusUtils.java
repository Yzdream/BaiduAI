package com.yz.utils;


import org.greenrobot.eventbus.EventBus;

/*<p>
 * EventBus是为了给已经存在的对象传递信息，而且订阅者必须要注册且不能被注销了，否则接收不到消息
 * 给还未创建的对象传递信息需要用粘性事件
 * <p>
 */
public class EventBusUtils {

    public static void initEventBusIndex() {
        //修改默认实现的配置，记住，必须在第一次EventBus.getDefault()之前配置，且只能设置一次。建议在application.onCreate()调用
//        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).addIndex(new MyEventBusIndex()).installDefaultEventBus();

    }

    /**
     * 注册EventBus
     *
     * @param subscriber 订阅者
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 注消EventBus
     *
     * @param subscriber 订阅者
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布订阅事件
     *
     * @param Publisher 发布者
     */
    public static void post(Object Publisher) {
        EventBus.getDefault().post(Publisher);
    }

    /**
     * 发布粘性订阅事件
     *
     * @param Publisher 发布者
     */
    public static void postSticky(Object Publisher) {
        EventBus.getDefault().postSticky(Publisher);
    }

    /**
     * 移除指定的粘性订阅事件
     *
     * @param eventType 移除的内容
     * @param <T>
     */
    public static <T> void removeStickyEvent(Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    /**
     * 取消事件传送 事件取消仅限于ThreadMode.PostThread下才可以使用
     * 不取消事件就会一直存在
     *
     * @param event
     */
    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * 移除所有的粘性订阅事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

}
