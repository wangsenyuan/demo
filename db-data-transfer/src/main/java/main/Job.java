package main;

import java.util.function.Consumer;

public interface Job<In, Out> {
    void apply(In in, Consumer<Out> consumer);

    default <Out2> Job<In, Out2> pipe(Job<Out, Out2> another) {
        return new PipedJob(this, another);
    }
}


class PipedJob<In, Out, Out2> implements Job<In, Out2> {
    private Job<In, Out> src;
    private Job<Out, Out2> dst;

    public PipedJob(Job<In, Out> src, Job<Out, Out2> dst) {
        this.src = src;
        this.dst = dst;
    }


    @Override
    public void apply(In in, Consumer<Out2> consumer) {
        this.src.apply(in, out -> dst.apply(out, consumer));
    }
}
