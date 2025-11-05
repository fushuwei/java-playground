package io.github.fushuwei.timerelated.date.issue;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日期类型线程安全问题
 */
public class DateThreadSafeIssue {

    // 定义一个共享的 Date 对象，多个线程将同时操作它
    private static final Date sharedDate = new Date();

    public static void main(String[] args) {
        // 先打印原始日期
        System.out.println("原始日期: " + sharedDate);

        // 创建一个线程池，包含 5 个线程
        try (ExecutorService executor = Executors.newFixedThreadPool(5)) {
            // 提交 10 个任务到线程池
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                executor.submit(() -> {
                    // 每个任务都会尝试修改共享的 Date 对象
                    // 这里的 setTime 方法会导致竞态条件 (race condition)
                    sharedDate.setTime(System.currentTimeMillis());
                    System.out.println(Thread.currentThread().getName() + " 修改后日期: " + sharedDate);
                });
            }
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
