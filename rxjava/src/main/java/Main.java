import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class Main {
    public static void main(String[] args) {
        Flowable<Integer> ints = Flowable.range(1, 1000);
        Flowable<Integer> jobs = ints.flatMap(
            i -> Flowable.just(i).subscribeOn(Schedulers.io()).map(Main::createJobs)
                .doOnNext(ii -> recordThread("emmit ", ii)), 10);
        jobs.observeOn(Schedulers.io()).map(i -> -i).doOnNext(iii -> recordThread("after map ", iii))
            .subscribe(iiii -> recordThread("receive ", iiii));

        sleep(20000);
    }

    public static void recordThread(String prefix, int index) {
        System.out.println(prefix + " " + index + " on " + Thread.currentThread());
    }

    public static int createJobs(int index) {
        System.out.println("createJobs " + index + " on " + Thread.currentThread());
        sleep(1000);
        return index;
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
